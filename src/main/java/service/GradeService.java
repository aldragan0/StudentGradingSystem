package service;

import domain.Assignment;
import domain.Grade;
import domain.GradeDTO;
import domain.Student;
import repo.CrudRepository;
import utils.events.AbstractEntityChangeEvent;
import utils.events.EventType;
import utils.events.GradeChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GradeService implements Observable, Service<Grade>{
    private CrudRepository<String, Grade> repo;
    static private LocalDate facultyStart =
            LocalDate.of(2018, 10, 1);
    static public String storeDirectoryPath = "src/main/resources/Students/";
    private List<Observer> observers;

    /**
     * compute student grade on assignment reducing by 2.5
     * for each week after the deadline
     * @return grade after penalization
     */
    public static float calcGrade(Grade entity){
        float oldGrade = entity.getGrade();
        int deadline = entity.getAssignment().getDeadline();

        if(entity.getWeekGraded() < deadline) return oldGrade;
        int time_delta = entity.getWeekGraded() - deadline;
        oldGrade -= (2.5f * time_delta);
        oldGrade = Math.max(1f, oldGrade);
        return oldGrade;
    }

    public GradeService(CrudRepository<String, Grade> repo){
        this.repo = repo;
        observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers(AbstractEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }

    /**
     * @return current week
     */
    static public int getCurrentWeek(){
        LocalDate today = LocalDate.now();
        int numWeeks = (int)facultyStart.until(today, ChronoUnit.WEEKS) + 1;
        return Math.min(numWeeks, 14);
    }

    /**
     * @return all items inside the repo
     */
    public Collection<Grade> getAll(){
        return repo.findAll();
    }

    /**
     * @param entity entity whose id is searched in the repo
     * @return object with that id
     */
    public Grade getGrade(Grade entity){
        return repo.findOne(entity.getID());
    }

    /**
     * @param entity entity whose id is searched in the repo
     * @return object with that id
     */
    public Grade getGrade(GradeDTO entity){
        return repo.findOne(entity.getGradeId());
    }

    /**
     * @param grade grade to be added to file
     * @param feedback feedback on the grade
     * @return true - information was added to file
     *         false - filePath does not exist
     */
    public boolean addStudentGrade(Grade grade, String feedback){
        Student student = grade.getStudent();
        Assignment assignment = grade.getAssignment();
        String filePath = storeDirectoryPath + student.getName() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))){
            bw.write("Assignment: " + assignment.getID() + '\n');
            bw.write("Grade: " + grade.getGrade() + '\n');
            bw.write("Graded in week: " + grade.getWeekGraded() + '\n');
            bw.write("Deadline: " + assignment.getDeadline() + '\n');
            bw.write("Feedback:\n" + feedback);
            bw.write("\n\n");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param grade grade to be added
     * @param isPermitted student is permitted to hand the assignment
     *                    late without any penalties
     * @return null - if the grade was added
     *         grade - otherwise
     */
    public Grade addGrade(Grade grade, boolean isPermitted){
        if(!isPermitted) grade.setGrade(calcGrade(grade));
        Grade response = repo.save(grade);
        if(response == null){
            GradeChangeEvent addEvent =
                    new GradeChangeEvent(EventType.ADD, grade);
            notifyObservers(addEvent);
        }
        return response;
    }

    /**
     * remove grade from the repo
     * @param grade grade to be deleted
     * @return grade - if the grade was deleted
     *         null - otherwise
     */
    public Grade deleteGrade(Grade grade) {
        Grade response = repo.delete(grade.getID());
        if(response != null){
            GradeChangeEvent deleteEvent =
                    new GradeChangeEvent(EventType.REMOVE, response);
            notifyObservers(deleteEvent);
        }
        return response;
    }

    /**
     * @param predicate function to filter the items by
     * @return items filtered by the predicate
     */
    public Collection<Grade> filterBy(Predicate<Grade> predicate){
        return getAll().stream().filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * for each student returns true if he/she handed all assignments
     * on time, false otherwise.
     * @param numAssignments number of all assignments
     * @return map containing studentName and if he/she handed
     * all assignments on time
     */
    public Map<String, Boolean> allAssignmentsOnTime(int numAssignments){
        Map<Student, List<Grade>> gradesPerStudent = getAll().stream()
                .collect(Collectors.groupingBy(Grade::getStudent));
        Map<String, Boolean> handedOnTime = new HashMap<>();
        gradesPerStudent.forEach((student, gradeList) ->{
            boolean onTime = gradeList.stream()
                    .noneMatch(reg -> reg.getWeekGraded() > reg.getAssignment().getDeadline());
            onTime &= gradeList.size() == numAssignments;
            handedOnTime.put(student.getName(), onTime);
        });
        return handedOnTime;
    }

    /**
     * given a list of all the assignments, it returns for each student
     * if he/she has passed the class
     * @param assignments list of all the assignments
     * @return map containing the studentName and if he/she passed
     */
    public Map<String, Boolean> studentsEnteringExam(Collection<Assignment> assignments){
        Map<String, Float> averagePerStudent = studentFinalGrade(assignments);
        Map<String, Boolean> entersExam = new HashMap<>();
        averagePerStudent.forEach((student, averageGrade)->
                entersExam.put(student, averageGrade > 4f)
        );
        return entersExam;
    }

    /**
     * given the number of students, it returns the average grade for each assignment
     * if the assignment has at least one grade
     * @param numStudents number of students in the current year
     * @return map containing the id of the assignment and its average grade
     */
    public Map<String, Float> averagePerAssignment(Integer numStudents){
        Map<String, List<GradeDTO>> gradesPerAssignment = GradeDTO.toGradeDTOList(getAll()).stream()
                .collect(Collectors.groupingBy(GradeDTO::getAssignmentId));
        Map<String, Float> averagePerAssignment = new HashMap<>();
        gradesPerAssignment.forEach((assignment, gradeList)->{
            Float gradeSum = gradeList.stream()
                    .reduce(new GradeDTO(0f),(x, grade)->
                    {
                        x.setGrade(x.getGrade() + grade.getGrade());
                        return x;})
                    .getGrade();
            averagePerAssignment.put(assignment, gradeSum / numStudents);
        });
        return averagePerAssignment;
    }

    /**
     * given a list of assignments, it returns the weighted average of all grades
     * for each student which has at least a grade
     * @param assignments all current assignments
     * @return map containing the studentName and its weighted average grade
     */
    public Map<String, Float> studentFinalGrade(Collection<Assignment> assignments){
        Map<String,Integer> assignmentWeight = assignments.stream()
                .collect(Collectors.toMap(
                        Assignment::getID,
                        (Assignment assignment)->
                                assignment.getDeadline() - assignment.getWeekReceived()));
        Integer gradeWeightSum =
                assignmentWeight.values().stream()
                        .reduce(0, (x, y)-> x + y);
        Map<Student,List<Grade>> gradesPerStudent = getAll().stream()
                .collect(Collectors.groupingBy(Grade::getStudent));
        Map<String, Float> avgGradePerStudent = new HashMap<>();
        gradesPerStudent.forEach((student, gradeList)->{
            Float weightedGrades = gradeList.stream()
                    .map(GradeDTO::toGradeDTO)
                    .reduce(new GradeDTO(0f), (x, grade)->{
                        x.setGrade(x.getGrade() +
                                assignmentWeight.get(grade.getAssignmentId()) * grade.getGrade());
                        return x;})
                    .getGrade();
            avgGradePerStudent.put(student.getName(), weightedGrades / gradeWeightSum);
        });
        return avgGradePerStudent;
    }

    /**
     * given a list of assignments, it returns all the assignments
     * for which there is no grade
     * @param assignments list of all current assignments
     * @param accountName name of the account to get the assignments for
     * @return list of ungraded assignments
     */
    public List<Assignment> getUngradedAssignments(Collection<Assignment> assignments, String accountName) {
        Collection<String> studentHandedAssignments = filterBy(
                grade -> grade.getStudent().getID().equals(accountName))
                .stream()
                .map(grade->grade.getAssignment().getID())
                .collect(Collectors.toList());
        return assignments.stream()
                .filter(assignment -> !studentHandedAssignments
                        .contains(assignment.getID()))
                .sorted(Comparator.comparing(Assignment::getID))
                .collect(Collectors.toList());
    }
}
