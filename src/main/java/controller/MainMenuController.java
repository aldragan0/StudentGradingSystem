package controller;

import domain.Account;
import domain.AccountType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import service.AccountService;
import service.AssignmentService;
import service.GradeService;
import service.StudentService;
import utils.AlertMessage;

import java.io.IOException;


public class MainMenuController {
    private static final String studentView = "/views/studentView.fxml";
    private static final String gradesView = "/views/mainGradeView.fxml";
    private static final String readOnlyView = "/views/studentReadView.fxml";
    private static final String loginView = "/views/loginView.fxml";

    public Stage primaryStage;

    private StudentService studentService;
    private GradeService gradeService;
    private AssignmentService assignmentService;
    private AccountService accountService;

    private void studentView() throws IOException {

        //set student layout
        FXMLLoader studentLoader = new FXMLLoader();
        studentLoader.setLocation(getClass().getResource(studentView));
        AnchorPane studentLayout = studentLoader.load();
        Scene studentScene = new Scene(studentLayout);

        StudentController studController = studentLoader.getController();
        studController.setService(studentService);
        studController.setMainController(this);

        primaryStage.setScene(studentScene);
    }

    private void gradesView() throws IOException {
        FXMLLoader gradesLoader = new FXMLLoader();
        gradesLoader.setLocation(getClass().getResource(gradesView));
        BorderPane gradesLayout = gradesLoader.load();
        Scene gradeScene = new Scene(gradesLayout);

        GradesController gradesController = gradesLoader.getController();
        gradesController.setService(studentService, assignmentService, gradeService);
        gradesController.initGradeView(gradesLayout);
        gradesController.setMainScene(gradeScene);
        gradesController.setMainController(this);

        primaryStage.setScene(gradeScene);
    }

    private void loginView() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource(loginView));
        AnchorPane loginLayout = loginLoader.load();
        Scene loginScene = new Scene(loginLayout);

        AccountController accountController = loginLoader.getController();
        accountController.setService(accountService, studentService);
        accountController.setMainController(this);

        primaryStage.setScene(loginScene);
    }

    private void studentReadView(String username) throws IOException {
        FXMLLoader studentReadLoader = new FXMLLoader();
        studentReadLoader.setLocation(getClass().getResource(readOnlyView));
        AnchorPane studentReadLayout = studentReadLoader.load();
        Scene studentReadScene = new Scene(studentReadLayout);

        StudentReadController studentReadController = studentReadLoader.getController();
        studentReadController.setAccountName(username);
        studentReadController.setService(gradeService, studentService, assignmentService);
        studentReadController.setMainController(this);

        primaryStage.setScene(studentReadScene);
    }

    public void handleStudentsCRUD(){
        try {
            studentView();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void handleGradesCRUD(){
        try {
            gradesView();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void handleLogin(){
        try {
            loginView();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void handleStudentReadOnly(Account account){
        try{
            studentReadView(account.getAccountName());
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void loginUser(Account user){
        if(user == null){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error", "Invalid username and password combination");
        } else if(user.getAccountType().equals(AccountType.TEACHER)) {
            handleGradesCRUD();
        } else if(user.getAccountType().equals(AccountType.SECRETARY)){
            handleStudentsCRUD();
        } else if(user.getAccountType().equals(AccountType.STUDENT)){
            handleStudentReadOnly(user);
        }
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setService(StudentService studentService,
                           AssignmentService assignmentService,
                           GradeService gradeService,
                           AccountService accountService){
        this.studentService = studentService;
        this.accountService = accountService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }
}
