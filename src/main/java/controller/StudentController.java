package controller;

import domain.Student;
import domain.StudentDTO;
import exceptions.ValidationException;
import gui.MainGui;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import service.StudentService;
import utils.AlertMessage;
import utils.FileChooserType;
import utils.GuiUtils;
import utils.events.AbstractEntityChangeEvent;
import utils.events.EventType;
import utils.observer.Observer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class StudentController implements Observer {
    private StudentService service;
    private final int rowsPerPage = 13;
    private MainMenuController mainController;
    private ObservableList<StudentDTO> model = FXCollections
            .observableArrayList();
    private ObservableList<String> listTeacher = FXCollections
            .observableArrayList(Arrays.asList("Camelia", "Adriana"));

    @FXML private TableView<StudentDTO> tableView;
    @FXML private TableColumn<StudentDTO, String> tableColumnId;
    @FXML private TableColumn<StudentDTO, String> tableColumnName;
    @FXML private TableColumn<StudentDTO, Integer> tableColumnGroup;
    @FXML private TableColumn<StudentDTO, String> tableColumnEmail;
    @FXML private TableColumn<StudentDTO, String> tableColumnTeacher;
    @FXML private TableColumn<StudentDTO, Boolean> studentSelectTableColumn;

    @FXML private Pagination tablePagination;

    @FXML private Button importStudentsBtn;
    @FXML private Button logoutBtn;

    private Node createPage(int pageIndex){
        service.setCurrentPageNumber(pageIndex);
        setStudents();
        return new BorderPane(tableView);
    }

    private void setNumPages(){
        int numPages = service.getSize() / rowsPerPage;
        numPages += (service.getSize() % rowsPerPage == 0)? 0 : 1;
        tablePagination.setPageCount(numPages);
        tablePagination.setMaxPageIndicatorCount(numPages);
    }


    /**
     * creates a handle for the columnChangesCommit event
     * @param tableColumn column to set the edit changes to
     * @param setter function to set a field to a value
     * @param <T> type of the field to modify
     */
    private <T>
    void columnEditCommit(TableColumn<StudentDTO, T> tableColumn, BiConsumer<StudentDTO, T> setter) {
        tableColumn.setOnEditCommit(cell -> {
            StudentDTO student = StudentDTO.toStudentDTO(cell.getRowValue());
            setter.accept(student, cell.getNewValue());
            if(updateStudentHandler(student)) {
                setter.accept(cell.getRowValue(), cell.getOldValue());
                reloadModel(service.getCurrentPageNumber() - 1);
            }});
    }

    private void setStudents(){
        List<StudentDTO> students;
        students = StudentDTO.toStudentDTOList(service.getNextRepoPage());
        if(students == null && service.getAll().size() == 0) model.clear();
        else if(students != null) {
            model.setAll(students);
        }
    }

    private void setEditHandlers(){
        columnEditCommit(tableColumnName, StudentDTO::setName);
        columnEditCommit(tableColumnGroup, StudentDTO::setGroup);
        columnEditCommit(tableColumnEmail, StudentDTO::setEmail);
        columnEditCommit(tableColumnTeacher, StudentDTO::setTeacher);
    }

    private void setEditableTableColumns() {
        tableView.setEditable(true);
        tableColumnName.setEditable(true);
        tableColumnGroup.setEditable(true);
        tableColumnEmail.setEditable(true);
        tableColumnTeacher.setEditable(true);
    }

    private void setTableCellFactory() {
        tableColumnName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnGroup.setCellFactory(TextFieldTableCell.forTableColumn(
                new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                try{
                    return Integer.parseInt(string);
                } catch (NumberFormatException | NullPointerException ex){
                    return -1;
                }
            }
        }));
        tableColumnEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnTeacher.setCellFactory(ComboBoxTableCell.forTableColumn(listTeacher));
        studentSelectTableColumn.setCellFactory(tableCell -> new CheckBoxTableCell<>());
    }

    private void setTooltips(){
        Tooltip logOutTooltip = new Tooltip("Log Out");
        logoutBtn.setTooltip(logOutTooltip);
    }

    @FXML
    private void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnTeacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        studentSelectTableColumn.setCellValueFactory(cellData ->{
            StudentDTO studentDTO = cellData.getValue();
            BooleanProperty property = studentDTO.getSelected();

            property.addListener(((observable, oldValue, newValue) ->
                    studentDTO.setSelected(newValue)));

            return property;
        });

        tablePagination.setPageFactory(this::createPage);

        setEditableTableColumns();
        setTableCellFactory();
        setEditHandlers();
        setTooltips();

        tableView.setItems(model);
    }

    //set the service, add this as observable to service and initialize the model
    public void setService(StudentService studentService){
        this.service = studentService;
        studentService.addObserver(this);
        reloadModel(0);

        service.setPageSize(rowsPerPage);
    }

    private void reloadModel(int pageNumber){
        service.setCurrentPageNumber(pageNumber);
        setStudents();
        setNumPages();
    }

    @Override
    public void update(AbstractEntityChangeEvent event) {
        if(event.getEventType() == EventType.ADD)
            reloadModel(0);
        else if(event.getEventType() == EventType.UPDATE) {
            reloadModel(service.getCurrentPageNumber() - 1);
        }
        tablePagination.setCurrentPageIndex(service.getCurrentPageNumber() - 1);
    }

    @FXML
    public void importStudentsHandler(){
        File dataFile = GuiUtils.showFileChooser(MainGui.filePath, FileChooserType.OPEN,
                (Stage)tableView.getScene().getWindow(),
                new FileChooser.ExtensionFilter("storage Files","*.csv"));
        if(dataFile != null) {
            System.out.println("Import Student data");
            try {
                int numStudents = service.getSize();
                service.loadStudents(dataFile);
                numStudents = service.getSize() - numStudents;
                AlertMessage.showMessage(null, Alert.AlertType.INFORMATION,
                        "Added " + numStudents + " students",
                        numStudents + " new students were imported from file");
            }catch (ValidationException ex){
                AlertMessage.showMessage(null, Alert.AlertType.WARNING,
                        "Warning",ex.getMessage());
            }
            importStudentsBtn.setDisable(true);
        }
        else{
            System.out.println("User exited file open dialog.");
        }
    }

    @FXML
    public void addStudentHandler(){
        showStudentAddDialog(service);
    }

    @FXML
    public void deleteStudentHandler(){
        int deleted = 0;
        for(final StudentDTO student : tableView.getItems()){
            if(student.getSelected().get()) {
                ++deleted;
                service.removeStudent(student);
            }
        }
        if(deleted > 0){
            reloadModel(0);
            tablePagination.setCurrentPageIndex(0);
            AlertMessage.showMessage(null, Alert.AlertType.INFORMATION,
                    deleted + " student(s) deleted", "Selected students were deleted");
        }
        else{
            AlertMessage.showMessage(null, Alert.AlertType.WARNING,
                    "No student selected",
                    "Select one or more students to be deleted");
        }
    }

    private boolean updateStudentHandler(Student student){
        try {
            service.updateStudent(student);
            return false;
        }
        catch(ValidationException e){
            AlertMessage.showMessage(null, Alert.AlertType.ERROR,
                    "Error", e.getMessage());
        }
        return true;
    }

    /**
     * creates a pop-up window for student addition
     * @param service service to bind to the pop-up
     */
    private void showStudentAddDialog(StudentService service) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/studentAddPopup.fxml"));

            AnchorPane root = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            StudentPopupController studentPopupController = loader.getController();
            studentPopupController.setService(service, dialogStage);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMainController(MainMenuController mainController){
        this.mainController = mainController;
    }

    @FXML
    private void handleLogOut(){
        mainController.handleLogin();
    }

    @FXML
    public void handleClear(){
        tableView.getSelectionModel().clearSelection();
        tableView.refresh();
    }
}