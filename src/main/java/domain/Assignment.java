package domain;

import java.util.Objects;

public class Assignment implements HasID<String> {

    private String assignmentId;
    private String description;
    private Integer deadline;
    private Integer received;

    /**
     * @param assignmentId assignment id
     * @param description assignment description
     * @param deadline assignment deadline
     * @param received assignment week received
     */
    public Assignment(String assignmentId, String description, Integer deadline, Integer received) {
        this.assignmentId = assignmentId;
        this.description = description;
        this.deadline = deadline;
        this.received = received;
    }
    public Assignment(String assignmentId, Integer deadline){
        this.assignmentId = assignmentId;
        this.deadline = deadline;
    }

    public Assignment(String assignmentId){
        this.assignmentId = assignmentId;
    }

    @Override
    public String getID(){
        return assignmentId;
    }

    @Override
    public void setID(String id) {
       this.assignmentId = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Integer getWeekReceived() {
        return received;
    }

    public void setWeekReceived(Integer received) {
        this.received = received;
    }

    @Override
    public String toString() {
        return assignmentId + " " + description + " " +
                deadline + " " + received;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment assignment = (Assignment) o;
        return Objects.equals(assignmentId, assignment.assignmentId) &&
                Objects.equals(description, assignment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentId, description);
    }
}
