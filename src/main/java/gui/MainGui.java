package gui;

import controller.MainMenuController;
import domain.Account;
import domain.Grade;
import domain.Student;
import domain.Assignment;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repo.*;
import service.AccountService;
import service.GradeService;
import service.StudentService;
import service.AssignmentService;
import validator.AccountValidator;
import validator.GradeValidator;
import validator.StudentValidator;
import validator.AssignmentValidator;

import java.io.IOException;

public class MainGui extends Application {
    private static final String studentXMLFilePath = "src/main/resources/storage/studentsXml.xml";
    private static final String gradeXMLFilePath = "src/main/resources/storage/gradesXml.xml";
    private static final String assignmentXMLFilePath = "src/main/resources/storage/assignmentsXml.xml";
    private static final String accountXMLFilePath = "src/main/resources/storage/accountsXml.xml";

    private static final String mainView = "/views/mainView.fxml";
    public static final String filePath = "C:/Users/Alex/Desktop/";

    private MainMenuController mainController;

    PagedCrudRepository<String, Student> studentRepo;
    PagedCrudRepository<String, Grade> gradeRepo;
    PagedCrudRepository<String, Assignment> assignmentRepo;
    PagedCrudRepository<String, Account> accountRepo;

    StudentService studentService;
    GradeService gradeService;
    AssignmentService assignmentService;
    AccountService accountService;

    private void mainView(Stage primaryStage) throws IOException {

        //set main layout
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(getClass().getResource(mainView));
        BorderPane rootLayout = mainLoader.load();
        Scene scene = new Scene(rootLayout);
        mainController = mainLoader.getController();
        primaryStage.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        studentRepo = new StudentXMLFileRepo(new StudentValidator(), studentXMLFilePath);
        gradeRepo = new GradeXMLFileRepo(new GradeValidator(), gradeXMLFilePath);
        assignmentRepo = new AssignmentXMLFileRepo(new AssignmentValidator(), assignmentXMLFilePath);
        accountRepo = new AccountXMLFileRepo(new AccountValidator(), accountXMLFilePath);

        studentService = new StudentService(studentRepo);
        gradeService = new GradeService(gradeRepo);
        assignmentService = new AssignmentService(assignmentRepo);
        accountService = new AccountService(accountRepo);

        mainView(primaryStage);
        mainController.setPrimaryStage(primaryStage);
        mainController.setService(studentService, assignmentService, gradeService, accountService);

        mainController.handleLogin();

        primaryStage.setTitle("Application");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
