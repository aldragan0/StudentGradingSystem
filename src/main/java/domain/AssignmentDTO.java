package domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AssignmentDTO {
    private String assignmentName;
    private Integer deadline;
    private Integer weekGraded;
    private Float grade;

    public static Collection<AssignmentDTO> toAssignmentDTOList(Collection<Grade> gradeList){
        return gradeList.stream()
                .map(AssignmentDTO::toAssignmentDTO)
                .sorted(Comparator.comparing(AssignmentDTO::getAssignmentName))
                .collect(Collectors.toList());
    }

    public static AssignmentDTO toAssignmentDTO(Grade grade){
        return new AssignmentDTO(grade.getAssignment().getID(),
                grade.getAssignment().getDeadline(),
                grade.getWeekGraded(),
                grade.getGrade());
    }

    public AssignmentDTO(String assignmentName, Integer deadline, Integer weekGraded, Float grade) {
        this.assignmentName = assignmentName;
        this.deadline = deadline;
        this.weekGraded = weekGraded;
        this.grade = grade;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public Integer getWeekGraded() {
        return weekGraded;
    }

    public Float getGrade() {
        return grade;
    }
}
