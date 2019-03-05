package controller;

import domain.Grade;
import exceptions.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import service.GradeService;
import utils.AlertMessage;

public class ConfirmationController {
    private Stage confirmationStage;
    private GradeService service;
    private Grade grade;
    private String feedback;
    private boolean isPermitted;

    @FXML private Label studentLabel;
    @FXML private Label assignmentLabel;
    @FXML private Label gradeLabel;
    @FXML private Label isAllowedLabel;
    @FXML private TextArea feedbackTextArea;

    private void initFields(){
        studentLabel.setText(grade.getStudent().getName());
        assignmentLabel.setText(grade.getAssignment().getID());
        gradeLabel.setText(grade.getGrade().toString());
        isAllowedLabel.setText((isPermitted)? "Yes" : "No");
        feedbackTextArea.setText(feedback);
    }

    public void setService(GradeService service, Stage stage) {
        this.service = service;
        this.confirmationStage = stage;
    }
    public void setGrade(Grade grade, String feedback, boolean isPermitted) {
        this.grade = grade;
        this.isPermitted = isPermitted;
        this.feedback = feedback;
        initFields();
    }

    @FXML
    private void handleCancel(){
        this.confirmationStage.close();
    }

    @FXML
    private void handleConfirm(){
        try {
            Grade response = service.addGrade(grade, isPermitted);
            if (response != null) {
                AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                        "Error", "Grade already exists");
                return;
            }
            service.addStudentGrade(grade, feedback);
        } catch(ValidationException ex){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error", ex.getMessage());
        }
        confirmationStage.close();
    }
}
