package domain;

import java.util.Objects;

public class Grade implements HasID<String> {
    private String id;
    private Student student;
    private Assignment assignment;
    private Integer weekGraded;
    private Float grade;

    public Grade(){}

    /**
     * constructor Grade
     * @param student - student receiving the grade
     * @param assignment - assignment on which the student was graded
     * @param weekGraded - the week in which the student was graded
     * @param grade - grade on assignment
     */
    public Grade(Student student, Assignment assignment, int weekGraded, Float grade) {
        this.student = student;
        this.assignment = assignment;
        this.id = student.getID() + assignment.getID();
        this.weekGraded = weekGraded;
        this.grade = grade;
    }

    public Integer getWeekGraded() {
        return weekGraded;
    }

    public void setWeekGraded(int weekGraded) {
        this.weekGraded = weekGraded;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    @Override
    public String toString(){
        return student.toString() + " " + assignment.toString()
                + " " + weekGraded + " " + getGrade();
    }

    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        else if(other == null || other.getClass() != getClass()) return false;
        return id.equals(((Grade)other).getID());
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
