package controller;

import domain.Grade;
import gui.MainGui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import service.AssignmentService;
import service.GradeService;
import service.StudentService;
import utils.ExportPDF;
import utils.FileChooserType;
import utils.GuiUtils;
import utils.events.AbstractEntityChangeEvent;
import utils.observer.Observer;

import java.io.File;
import java.util.*;

public class GradeReportController implements Observer {
    private GradeService service;
    private AssignmentService assignmentService;
    private StudentService studentService;
    private Map<String, ?> currentModel;
    private String currentFileName;
    private String currentTitle;
    private Map<String, Pair<String, Runnable>> functionMap = new HashMap<>();
    private ObservableList<Grade> model = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML private TabPane chartTabPane;
    @FXML private BarChart<String, Integer> barChartStudentAvg;
    @FXML private LineChart<String, Float> lineChartAssignmentAvg;
    @FXML private BarChart<String, Integer> barChartEnteringExam;
    @FXML private PieChart pieChartAssignmentTime;

    private void drawChart1(){
        System.out.println("Draw chart 1 - student average");
        currentTitle = "Student Average Grade";
        Map<String, Float> avgPerStudent = service.studentFinalGrade(
                assignmentService.getAll());
        currentModel = avgPerStudent;

        //bar chart
        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Number of students with avg. grade");

        int[] numGrades = new int[5];
        avgPerStudent.forEach((student, avgGrade)->{
            int index = Math.min(4, avgGrade.intValue() / 2);
            numGrades[index]++;
        });
        int start = 0;
        for(int index : numGrades){
            int end = start + 2;
            dataSeries.getData().add(new XYChart.Data<>(start + "-" + end, index));
            start += 2;
        }

        barChartStudentAvg.getData().clear();
        barChartStudentAvg.getData().add(dataSeries);
    }

    private void drawChart2(){
        System.out.println("Draw chart 2 - assignment average");
        currentTitle = "Average Grade per Assignment";
        Map<String, Float> avgPerAssignment = service.averagePerAssignment(
                studentService.getSize());
        currentModel = avgPerAssignment;

        //line chart
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        series.setName("Average Grade per Assignment");

        avgPerAssignment.forEach((assignment, avgGrade)->
            series.getData().add(new XYChart.Data<>(assignment,
                    avgGrade)));

        lineChartAssignmentAvg.getData().clear();
        lineChartAssignmentAvg.getData().add(series);
    }

    private void drawChart3(){
        System.out.println("Draw chart 3 - student entering exam");
        currentTitle = "Students Entering Exam";
        Map<String, Boolean> enterExam = service.studentsEnteringExam(assignmentService.getAll());
        currentModel = enterExam;

        //bar chart
        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Number of students entering the exam");

        int[] numEnterExam = new int[2];
        enterExam.forEach((student, entersExam)->{
            int index = (entersExam)? 1 : 0;
            numEnterExam[index]++;
        });
        dataSeries.getData().add(new XYChart.Data<>("Enter Exam", numEnterExam[1]));
        dataSeries.getData().add(new XYChart.Data<>("Don't enter exam", numEnterExam[0]));

        barChartEnteringExam.getData().clear();
        barChartEnteringExam.getData().add(dataSeries);
    }

    private void drawChart4(){
        System.out.println("Draw chart 4 - all assignments on time");
        currentTitle = "Students Handling all Assignments on Time";

        int numAssignments = assignmentService.getSize();
        Map<String, Boolean> assignmentOnTime = service.allAssignmentsOnTime(numAssignments);
        currentModel = assignmentOnTime;

        long numOnTime = assignmentOnTime.values().stream()
                .filter(val-> val).count();
        long numLate = assignmentOnTime.values().size() - numOnTime;
        //pie chart
        if(pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("On time", numOnTime));
            pieChartData.add(new PieChart.Data("Late", numLate));
        }

        pieChartData.get(0).setPieValue(numOnTime);
        pieChartData.get(1).setPieValue(numLate);

        pieChartAssignmentTime.setLabelLineLength(10);
        pieChartAssignmentTime.setLegendSide(Side.LEFT);

        pieChartAssignmentTime.setData(pieChartData);
    }

    @FXML
    private void initialize(){
        functionMap.put("chart1", new Pair<>("studentAvg.pdf" ,this::drawChart1));
        functionMap.put("chart2", new Pair<>("assignmentAvg.pdf" ,this::drawChart2));
        functionMap.put("chart3", new Pair<>("enteringExam.pdf" ,this::drawChart3));
        functionMap.put("chart4", new Pair<>("assignmentOnTime.pdf" ,this::drawChart4));
        chartTabPane.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    Pair<String, Runnable> pair = functionMap.get(newValue.getId());
                    currentFileName = pair.getKey();
                    pair.getValue().run();
                }
        );
    }

    private void initModel(){
        model.setAll(new ArrayList<>(service.getAll()));
    }

    @Override
    public void update(AbstractEntityChangeEvent event) {
        initModel();
        drawChart1();
        drawChart2();
        drawChart3();
        drawChart4();
    }

    public void setService(GradeService service, AssignmentService assignmentService,
                           StudentService studentService){
        this.service = service;
        this.assignmentService = assignmentService;
        this.studentService = studentService;
        this.service.addObserver(this);
        drawChart1();
        chartTabPane.getSelectionModel().selectFirst();
        currentFileName = functionMap.get("chart1").getKey();
    }

    @FXML
    private void exportPDF(){
        System.out.println("Export PDF");

        File destination = GuiUtils.showFileChooser(currentFileName, MainGui.filePath,
                FileChooserType.SAVE,
                (Stage) chartTabPane.getScene().getWindow(),
                new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        if(destination != null){
            ExportPDF.generatePDF(destination, currentModel, currentTitle);
        }
        else{
            System.out.println("User exited file chooser window");
        }
    }
}
