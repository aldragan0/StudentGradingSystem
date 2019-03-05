package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import service.AssignmentService;
import service.GradeService;
import service.StudentService;

import java.io.IOException;

public class GradesController {
    private static final String gradeAddView = "/views/gradeAddView.fxml";
    private static final String gradeFilterView = "/views/gradeFilterView.fxml";
    private static final String gradeReportView = "/views/gradeReportView.fxml";

    private AnchorPane addView;
    private AnchorPane filterView;
    private AnchorPane reportView;
    private BorderPane gradeRoot;
    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    private MainMenuController mainController;

    @FXML
    public void handleAddGrade(){
        gradeRoot.setCenter(addView);
    }

    @FXML
    public void handleFilterGrade(){
        gradeRoot.setCenter(filterView);
    }

    @FXML
    public void handleReportGrade(){
        gradeRoot.setCenter(reportView);
    }

    private void setCenterAddLayout(AnchorPane addView){
        this.addView = addView;
    }

    private void setCenterFilterLayout(AnchorPane filterView){
        this.filterView = filterView;
    }

    private void setCenterReportLayout(AnchorPane reportView){
        this.reportView = reportView;
    }

    public void setMainScene(Scene scene){
        gradeRoot = (BorderPane) scene.getRoot();
    }

    private void gradeAddView() throws IOException {

        //load view from xml
        FXMLLoader gradeAddLoader = new FXMLLoader();
        gradeAddLoader.setLocation(getClass().getResource(gradeAddView));
        AnchorPane addLayout = gradeAddLoader.load();

        //set controller params
        GradeAddController gradeAddController = gradeAddLoader.getController();
        gradeAddController.setService(studentService, assignmentService, gradeService);

        //set grade add layout
        setCenterAddLayout(addLayout);
    }

    private void gradeFilterView() throws IOException {

        //load view from xml
        FXMLLoader gradeFilterLoader = new FXMLLoader();
        gradeFilterLoader.setLocation(getClass().getResource(gradeFilterView));
        AnchorPane filterLayout = gradeFilterLoader.load();

        //set controller params
        GradeFilterController gradeFilterController = gradeFilterLoader.getController();
        gradeFilterController.setService(assignmentService, gradeService);

        //set grade filter layout
        setCenterFilterLayout(filterLayout);
    }

    private void gradeReportView() throws IOException {

        //load view from xml
        FXMLLoader gradeReportLoader = new FXMLLoader();
        gradeReportLoader.setLocation(getClass().getResource(gradeReportView));
        AnchorPane reportLayout = gradeReportLoader.load();

        //set controller params
        GradeReportController gradeReportController = gradeReportLoader.getController();
        gradeReportController.setService(gradeService, assignmentService, studentService);

        //set grade report layout
        setCenterReportLayout(reportLayout);
    }

    public void initGradeView(BorderPane gradeRoot){
        this.gradeRoot = gradeRoot;
         try {
            gradeAddView();
            gradeFilterView();
            gradeReportView();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        this.handleAddGrade();
    }

    public void setMainController(MainMenuController mainController){
        this.mainController = mainController;
    }

    @FXML
    private void handleLogOut(){
        mainController.handleLogin();
    }

    public void setService(StudentService studentService,
                           AssignmentService assignmentService,
                           GradeService gradeService){
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }
}