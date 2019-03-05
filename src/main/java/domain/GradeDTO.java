package domain;

import java.util.Collection;
import java.util.stream.Collectors;

public class GradeDTO {
    private String gradeId;
    private String studentName;
    private Integer group;
    private String assignmentId;
    private float grade;


    public static Collection<GradeDTO> toGradeDTO(Collection<Grade> iterable) {
        return iterable.stream()
                .map(GradeDTO::toGradeDTO)
                .collect(Collectors.toList());
    }

    public static GradeDTO toGradeDTO(Grade reg) {
        return new GradeDTO(reg.getID(), reg.getStudent().getName(),
                reg.getStudent().getGroup(),
                reg.getAssignment().getID(),
                reg.getGrade());
    }

    public static Collection<GradeDTO> toGradeDTOList(Collection<Grade> reg) {
        return reg.stream()
                .map(GradeDTO::toGradeDTO)
                .collect(Collectors.toList());
    }

    public GradeDTO(String gradeId, String studentName, Integer group,
                    String assignmentId, Float grade) {
        this.gradeId = gradeId;
        this.studentName = studentName;
        this.group = group;
        this.assignmentId = assignmentId;
        this.grade = grade;
    }

    public GradeDTO(Float grade) {
        this.grade = grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getGradeId(){
        return gradeId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getGroup() {
        return this.group;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public Float getGrade() {
        return grade;
    }
}