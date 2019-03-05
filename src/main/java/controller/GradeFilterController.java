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
import service.AssignmentService;
import service.GradeService;
import utils.AlertMessage;
import utils.GuiUtils;
import utils.events.AbstractEntityChangeEvent;
import utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static domain.GradeDTO.toGradeDTO;
import static domain.StudentGradeDTO.toStudentGradeDTO;
import static utils.GuiUtils.initComboBox;
import static utils.GuiUtils.setComboBoxItemsStyle;

public class GradeFilterController implements Observer {
    private GradeService gradeService;
    private AssignmentService assignmentService;

    private ObservableList<GradeDTO> model = FXCollections.observableArrayList();
    private List<Integer> groupList = Arrays.asList(221, 222, 223, 224, 225, 226, 227);
    private ObservableList<String> assignmentFilterList = FXCollections.observableArrayList();

    @FXML private TableView<GradeDTO> gradeTableView;
    @FXML private TableColumn<GradeDTO, String> studentTableColumn;
    @FXML private TableColumn<GradeDTO, Integer> groupTableColumn;
    @FXML private TableColumn<GradeDTO, String> assignmentIdTableColumn;
    @FXML private TableColumn<GradeDTO, Float> gradeTableColumn;

    @FXML private TextField studentTextBox;
    @FXML private ComboBox<String> assignmentComboBox;
    @FXML private ComboBox<Integer> groupComboBox;
    @FXML private Button showTableBtn;

    private void handleFilter(){
        Predicate<GradeDTO> p1 = grade -> studentTextBox.getText().isEmpty()
                || grade.getStudentName()
                .contains(studentTextBox.getText());
        Predicate<GradeDTO> p2 = grade -> (assignmentComboBox.getSelectionModel().isEmpty())
                || grade.getAssignmentId()
                .equals(assignmentComboBox.getSelectionModel().getSelectedItem());
        Predicate<GradeDTO> p3 = grade -> (groupComboBox.getSelectionModel().isEmpty())
                || grade.getGroup()
                .equals(groupComboBox.getSelectionModel().getSelectedItem());

        model.setAll(toGradeDTO(gradeService.getAll()).stream()
                .filter(p1.and(p2).and(p3))
                .collect(Collectors.toList()));
    }

    @FXML
    private void showStudentTable(){
        Integer selectedGroup = groupComboBox.getSelectionModel().getSelectedItem();
        Predicate<Grade> currentGroup = grade -> grade.getStudent()
                .getGroup().equals(selectedGroup);
        List<StudentGradeDTO> listaNote = toStudentGradeDTO(gradeService.filterBy(currentGroup));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/filterTable.fxml"));
        try{
            AnchorPane root = loader.load();
            Stage tableStage = new Stage();

            //set a name and ...
            tableStage.setTitle("Filter Table");
            tableStage.initModality(Modality.WINDOW_MODAL);

            //set a scene for the stage
            Scene scene = new Scene(root);
            tableStage.setScene(scene);

            StudentGradesController controller = loader.getController();
            controller.setModelItems(listaNote);

            tableStage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setComboBoxItems(){
        GuiUtils.loadComboBoxItems(assignmentService, Assignment::getID,
                assignmentFilterList);
    }

    private void initComboBoxes(){
        initComboBox(assignmentFilterList, assignmentComboBox, false);
        setComboBoxItems();
        groupComboBox.getItems().addAll(groupList);
        setComboBoxItemsStyle(assignmentComboBox);
        setComboBoxItemsStyle(groupComboBox);
    }

    @FXML
    private void initialize(){
        studentTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        groupTableColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        assignmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("assignmentId"));
        gradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        gradeTableView.setItems(model);
        studentTextBox.textProperty()
                .addListener(observable -> handleFilter());
        assignmentComboBox.valueProperty()
                .addListener(observable -> handleFilter());
        groupComboBox.valueProperty()
                .addListener(observable -> {
                    showTableBtn.setDisable(false);
                    handleFilter();});

        showTableBtn.setDisable(true);
    }

    @FXML
    private void handleClearFilter(){
        studentTextBox.setText("");
        groupComboBox.getSelectionModel().clearSelection();
        assignmentComboBox.getSelectionModel().clearSelection();
        showTableBtn.setDisable(true);
    }

    private void initModel(){
        model.setAll(new ArrayList<>(toGradeDTO(gradeService.getAll())));
    }

    public void setService(AssignmentService assignmentService, GradeService service){
        this.gradeService = service;
        this.assignmentService = assignmentService;

        service.addObserver(this);
        initModel();
        initComboBoxes();
    }


    @FXML
    private void handleDelete(){
        GradeDTO gradeDTO = gradeTableView.getSelectionModel().getSelectedItem();
        Grade response;
        if(gradeDTO != null){
            Grade grade = gradeService.getGrade(gradeDTO);
            response = gradeService.deleteGrade(grade);
            if(response != null){
                AlertMessage.showMessage(null, Alert.AlertType.INFORMATION,
                        "Grade Deleted", "Grade successfully deleted");
            }
        } else{
            AlertMessage.showMessage(null, Alert.AlertType.WARNING,
                    "Select Grade", "A grade must be selected");
        }
    }


    @Override
    public void update(AbstractEntityChangeEvent event) {
        initModel();
        setComboBoxItems();
    }
}
