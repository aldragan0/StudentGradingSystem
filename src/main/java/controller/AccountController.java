package controller;

import domain.Account;
import domain.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.AccountService;
import service.StudentService;
import utils.AlertMessage;
import utils.events.AbstractEntityChangeEvent;
import utils.events.EventType;
import utils.events.StudentChangeEvent;
import utils.observer.Observer;

public class AccountController implements Observer {
    private AccountService service;

    @FXML private TextField accountNameTextField;
    @FXML private PasswordField passPasswordField;
    @FXML private Button loginBtn;
    private MainMenuController mainController;

    public void setService(AccountService service, StudentService studentService){
        this.service = service;
        studentService.addObserver(this);
        loginBtn.setDefaultButton(true);
    }

    public void setMainController(MainMenuController mainController){
        this.mainController = mainController;
    }

    @FXML
    private void handleLogin(){
        String accountName = accountNameTextField.getText();
        String password = passPasswordField.getText();
        if(accountName.equals("")
            || password.equals("")) {
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error","Fields must not be empty");
            return;
        }
        Account account = service.accountExists(new Account(accountName, password));
        mainController.loginUser(account);
    }

    private void addAccount(Student student){
        Account account = Student.accountFromStudent(student);
        if(service.addAccount(account) == null){
            System.out.println("ADDED----" + account.toString());
        }
    }

    private void deleteAccount(Student student){
        Account account = Student.accountFromStudent(student);
        Account response;
        try {
            response = service.removeAccount(account);
        } catch (IllegalArgumentException ex){
            System.out.println("invalid account processed");
            return;
        }
        if(response != null){
            System.out.println("DELETED----" + account.toString());
        }
    }

    @Override
    public void update(AbstractEntityChangeEvent event) {
        if(event.getEventType() == EventType.ADD){
            Student addedStudent = ((StudentChangeEvent) event).getData();
            addAccount(addedStudent);
        }
        else if(event.getEventType() == EventType.REMOVE){
            Student removedStudent = ((StudentChangeEvent) event).getData();
            deleteAccount(removedStudent);
        }
    }
}
