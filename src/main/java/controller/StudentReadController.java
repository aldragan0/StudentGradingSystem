package controller;

import domain.Assignment;
import domain.AssignmentDTO;
import domain.Grade;
import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.AssignmentService;
import service.GradeService;
import service.StudentService;
import utils.events.AbstractEntityChangeEvent;
import utils.observer.Observer;

import java.util.Collection;
import java.util.List;

public class StudentReadController implements Observer {
    private String accountName;
    private GradeService gradeService;
    private StudentService studentService;
    private AssignmentService assignmentService;
    private MainMenuController mainController;
    private ObservableList<AssignmentDTO> gradeList = FXCollections.observableArrayList();
    private ObservableList<Assignment> assignments = FXCollections.observableArrayList();

    @FXML private TableView<AssignmentDTO> gradeTable;
    @FXML private TableView<Assignment> assignmentTable;

    @FXML private TableColumn<AssignmentDTO, String> assignmentTableColumn;
    @FXML private TableColumn<AssignmentDTO, Integer> deadlineTableColumn;
    @FXML private TableColumn<AssignmentDTO, Integer> weekGradedTableColumn;
    @FXML private TableColumn<AssignmentDTO, Float> gradeTableColumn;

    @FXML private TableColumn<Assignment, String> assignmentIdTableColumn;
    @FXML private TableColumn<Assignment, String> descriptionTableColumn;
    @FXML private TableColumn<Assignment, Integer> assignmentDeadlineTableColumn;
    @FXML private TableColumn<Assignment, Integer> weekReceivedTableColumn;

    @FXML private Label nameLabel;

    @Override
    public void update(AbstractEntityChangeEvent event) {
        reloadGradeList();
        reloadAssignmentList(assignmentService, accountName);
        setStudentNameLabel();
    }

    private void reloadGradeList(){
        Collection<Grade> studentGradeList = gradeService.filterBy(
                grade -> grade.getStudent().getID().equals(accountName));
        Collection<AssignmentDTO> assignmentList = AssignmentDTO.toAssignmentDTOList(studentGradeList);
        gradeList.setAll(assignmentList);
    }

    private void reloadAssignmentList(AssignmentService assignmentService, String accountName){
        List<Assignment> ungradedAssignments = gradeService.getUngradedAssignments(assignmentService.getAll(),
                accountName);
        assignments.setAll(ungradedAssignments);
    }


    @FXML
    private void initialize(){
        assignmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("assignmentName"));
        deadlineTableColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        weekGradedTableColumn.setCellValueFactory(new PropertyValueFactory<>("weekGraded"));
        gradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        assignmentIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        assignmentDeadlineTableColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        weekReceivedTableColumn.setCellValueFactory(new PropertyValueFactory<>("weekReceived"));

        gradeTable.setItems(gradeList);
        assignmentTable.setItems(assignments);
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public void setService(GradeService gradeService, StudentService studentService,
                           AssignmentService assignmentService){
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        setStudentNameLabel();
        gradeService.addObserver(this);
        assignmentService.addObserver(this);
        reloadGradeList();
        reloadAssignmentList(assignmentService, accountName);
    }

    private void setStudentNameLabel() {
        Student student = studentService.getStudent(accountName);
        nameLabel.setText(student.getName());
    }

    @FXML
    private void handleLogOut(){
        mainController.handleLogin();
    }

    public void setMainController(MainMenuController mainController){
        this.mainController = mainController;
    }
}