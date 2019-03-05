package controller;

import domain.Student;
import exceptions.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.StudentService;
import utils.AlertMessage;
import utils.GuiUtils;

import java.util.Arrays;

public class StudentPopupController {
    private StudentService service;
    private Stage studentStage;
    private ObservableList<Integer> listGroup = FXCollections.observableArrayList();
    private ObservableList<String> listTeacher = FXCollections.observableArrayList();

    @FXML private TextField nameTextBox;
    @FXML private TextField emailTextBox;
    @FXML private ComboBox<Integer> groupComboBox;
    @FXML private ComboBox<String> teacherComboBox;

    @FXML private Button confirmAddBtn;

    /**
     * set comboBox items and drop-down style
     */
    private void setComboBoxes(){
        groupComboBox.setItems(listGroup);
        GuiUtils.setComboBoxItemsStyle(groupComboBox);
        teacherComboBox.setItems(listTeacher);
        GuiUtils.setComboBoxItemsStyle(teacherComboBox);
    }

    public void setService(StudentService service, Stage stage) {
        this.service = service;
        this.studentStage = stage;

        listGroup.setAll(Arrays.asList(221, 222, 223, 224, 225, 226, 227));
        listTeacher.setAll(Arrays.asList("Camelia", "Adriana"));

        setComboBoxes();
        confirmAddBtn.setDefaultButton(true);
    }
    @FXML
    private void cancelAddHandler() {
        this.studentStage.close();
    }

    private Student createStudentFromFields(){
        String name = nameTextBox.getText();
        Integer group = groupComboBox.getSelectionModel().getSelectedItem();
        String email = emailTextBox.getText();
        String teacher = teacherComboBox.getSelectionModel().getSelectedItem();

        return new Student(name, group, email, teacher);
    }

    @FXML
    public void addStudentHandler(){
        try{
            Student currentStud = createStudentFromFields();
            Student response = service.addStudent(currentStud);
            if(response != null){
                AlertMessage.showMessage(null, Alert.AlertType.WARNING,
                        "Existent Student", "Student already exists");
            }
            else{
                studentStage.close();
            }
        } catch (ValidationException | NumberFormatException e){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                            "Error", e.getMessage());
        } catch (IllegalArgumentException ex){
            System.out.println("Student does not exist");
        }
    }
}
