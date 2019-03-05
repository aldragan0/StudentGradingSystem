package controller;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.GradeService;
import service.StudentService;
import service.AssignmentService;
import utils.AlertMessage;
import utils.events.AbstractEntityChangeEvent;
import utils.observer.Observer;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static domain.GradeDTO.toGradeDTO;
import static utils.GuiUtils.initComboBox;
import static utils.GuiUtils.loadComboBoxItems;
import static utils.GuiUtils.setComboBoxItemsStyle;

public class GradeAddController implements Observer {
    private GradeService gradeService;
    private StudentService studService;
    private AssignmentService assignmentService;
    private ObservableList<GradeDTO> model = FXCollections.observableArrayList();
    private ObservableList<String> assignmentEditList = FXCollections.observableArrayList();
    private ObservableList<String> studentEditList = FXCollections.observableArrayList();

    @FXML private TableView<GradeDTO> gradeTableView;
    @FXML private TableColumn<GradeDTO, String> studentTableColumn;
    @FXML private TableColumn<GradeDTO, Integer> groupTableColumn;
    @FXML private TableColumn<GradeDTO, String> assignmentIdTableColumn;
    @FXML private TableColumn<GradeDTO, Float> gradeTableColumn;

    @FXML private ComboBox<String> studentEditComboBox;
    @FXML private ComboBox<String> assignmentEditComboBox;
    @FXML private TextField gradeEditTextBox;
    @FXML private TextArea feedbackEditTextArea;
    @FXML private CheckBox allowedEditCheckBox;

    public void setCurrentAssignment(){
        int currentWeek = GradeService.getCurrentWeek();
        Assignment currentAssignment = assignmentService.getCurrentAssignment(currentWeek);
        assert(currentAssignment != null);
        int index = assignmentEditComboBox.getItems().indexOf(currentAssignment.getID());
        assignmentEditComboBox.getSelectionModel().select(index);
    }

    @FXML
    private void handleClear(){
        feedbackEditTextArea.clear();
        gradeEditTextBox.clear();
        studentEditComboBox.setValue("");
        setCurrentAssignment();
    }

    private void initModel(){
        model.setAll(new ArrayList<>(toGradeDTO(gradeService.getAll())));
    }

    private void setComboBoxItems(){
        loadComboBoxItems(studService, stud -> stud.getName() + "(" + stud.getID() + ")",
                studentEditList);
        loadComboBoxItems(assignmentService, Assignment::getID,
                assignmentEditList);
    }

    private void initComboBoxes() {
        initComboBox(assignmentEditList, assignmentEditComboBox,  false);
        initComboBox(studentEditList, studentEditComboBox, true);
        setComboBoxItems();
        setComboBoxItemsStyle(assignmentEditComboBox);
        setComboBoxItemsStyle(studentEditComboBox);
    }

    @FXML
    private void initialize(){
        studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        groupTableColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        assignmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("assignmentId"));
        gradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        gradeTableView.setItems(model);
        studentEditComboBox.getEditor().setOnKeyTyped(event ->{
                Predicate<Grade> filterPredicate = (grade ->
                {
                    String studentName = grade.getStudent().getName().toLowerCase()
                            + '(' + grade.getStudent().getID() + ')';
                        return studentName.contains(studentEditComboBox.getEditor().getText().toLowerCase())
                        || studentEditComboBox.getEditor().getText().equals("");
                });
                model.setAll(toGradeDTO(gradeService.filterBy(filterPredicate)));
        });
    }

    public void setService(StudentService studService, AssignmentService assignmentService,
                           GradeService service){
        this.studService = studService;
        this.assignmentService = assignmentService;
        this.gradeService = service;
        service.addObserver(this);
        studService.addObserver(this);

        initModel();
        initComboBoxes();
        setCurrentAssignment();
    }

    @FXML
    private void handleAdd(){
        String studSelector = studentEditComboBox.getSelectionModel().getSelectedItem();
        String assignmentId = assignmentEditComboBox.getSelectionModel().getSelectedItem();
        String strGrade = gradeEditTextBox.getText();
        String feedback = feedbackEditTextArea.getText();
        int currentWeek = GradeService.getCurrentWeek();
        boolean isPermitted = allowedEditCheckBox.isSelected();

        if(studSelector == null
                || assignmentId == null
                || strGrade.equals("")
                || feedback.equals("")
                || assignmentId.equals("")){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error", "All fields are obligatory");
            return;
        }
        try{
            String idStudent = studSelector.split("[()]")[1];
            Student student = studService.getStudent(new Student(idStudent));
            Assignment assignment = assignmentService.getAssignment(new Assignment(assignmentId));
            float grade = Float.parseFloat(strGrade);
            if(grade > 10f || grade < 1f) throw new NumberFormatException();
            Grade newGrade = new Grade(student, assignment, currentWeek, grade);
            if(!isPermitted && currentWeek > assignment.getDeadline()){
                //if the assignment is handed in late
                newGrade.setGrade(GradeService.calcGrade(newGrade));
                float gradeDelta = grade - newGrade.getGrade();
                if(gradeDelta > 0)
                    feedback = String.format("Grade was lowered with %.1f point(s) -", gradeDelta)
                        + feedback;
            }
            showConfirmationWindow(gradeService, newGrade, feedback, isPermitted);
        }
        catch(ArrayIndexOutOfBoundsException ex){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error", "Select a student from the student box");
        }
        catch (NumberFormatException ex){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error", "Grade must be a positive number" +
                            " between 1 and 10");
        }
    }

    @Override
    public void update(AbstractEntityChangeEvent event) {
        initModel();
        setComboBoxItems();
    }

    /**
     * creates a pop-up window for the grade addition
     * @param gradeService service to bind to the pop-up
     * @param grade object being displayed
     * @param feedback info about the grade
     * @param isPermitted info about the student's grade
     */
    private void showConfirmationWindow(GradeService gradeService, Grade grade, String feedback,
                                              boolean isPermitted){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/confirmationView.fxml"));

            //create a new stage for the pop-up
            AnchorPane root = loader.load();
            Stage confirmationStage = new Stage();

            confirmationStage.setTitle("Confirmation");
            confirmationStage.initModality(Modality.APPLICATION_MODAL);

            //set a scene for the stage
            Scene scene = new Scene(root);
            confirmationStage.setScene(scene);

            //set controller for the pop-up window
            ConfirmationController controller = loader.getController();
            controller.setService(gradeService, confirmationStage);
            controller.setGrade(grade, feedback, isPermitted);

            confirmationStage.show();
            confirmationStage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}