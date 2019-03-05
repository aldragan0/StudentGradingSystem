package controller;

import domain.StudentGradeDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class StudentGradesController {
    private ObservableList<StudentGradeDTO> list = FXCollections.observableArrayList();
    @FXML private TableView<StudentGradeDTO> tableView;
    @FXML private TableColumn<StudentGradeDTO, String> nameTableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment1TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment2TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment3TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment4TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment5TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment6TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment7TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment8TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment9TableColumn;
    @FXML private TableColumn<StudentGradeDTO,Float> assignment10TableColumn;

    @FXML
    private void initialize(){
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        assignment1TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade1"));
        assignment2TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade2"));
        assignment3TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade3"));
        assignment4TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade4"));
        assignment5TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade5"));
        assignment6TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade6"));
        assignment7TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade7"));
        assignment8TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade8"));
        assignment9TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade9"));
        assignment10TableColumn.setCellValueFactory(new PropertyValueFactory<>("grade10"));

        tableView.setItems(list);
    }

    public void setModelItems(List<StudentGradeDTO> iterable){
        list.setAll(iterable);
    }
}
