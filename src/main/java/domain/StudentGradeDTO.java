package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentGradeDTO {
    private String studentName;
    private Float grade1;
    private Float grade2;
    private Float grade3;
    private Float grade4;
    private Float grade5;
    private Float grade6;
    private Float grade7;
    private Float grade8;
    private Float grade9;
    private Float grade10;

    public static List<StudentGradeDTO> toStudentGradeDTO(Iterable<Grade> iterable){
        Map<Student, List<Grade>> gradesPerStudent =
                StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.groupingBy(Grade::getStudent));
        List<StudentGradeDTO> gradeListDTO = new ArrayList<>();
        gradesPerStudent.forEach((student, grades)->
            gradeListDTO.add(toStudentGradeDTO(student, grades))
        );
        return gradeListDTO;
    }

    public static StudentGradeDTO toStudentGradeDTO(Student student, List<Grade> grades){
        List<Float> gradeList = new ArrayList<>();
            for (int i = 1; i <= 10; ++i){
                Float totalGrade = 0f;
                for(Grade grade : grades){
                    if(grade.getAssignment().getID().equals("Lab"+i)){
                        totalGrade = grade.getGrade();
                    }
                }
                gradeList.add(totalGrade);
            }
            return new StudentGradeDTO(student.getName(), gradeList);
    }

    public StudentGradeDTO(String studentName, List<Float> gradeList){
        this.studentName = studentName;
        grade1 = gradeList.get(0);
        grade2 = gradeList.get(1);
        grade3 = gradeList.get(2);
        grade4 = gradeList.get(3);
        grade5 = gradeList.get(4);
        grade6 = gradeList.get(5);
        grade7 = gradeList.get(6);
        grade8 = gradeList.get(7);
        grade9 = gradeList.get(8);
        grade10 = gradeList.get(9);
    }

    public Float getGrade1() { return grade1; }

    public Float getGrade2() { return grade2; }

    public Float getGrade3() { return grade3; }

    public Float getGrade4() { return grade4; }

    public Float getGrade5() { return grade5; }

    public Float getGrade6() { return grade6; }

    public Float getGrade7() { return grade7; }

    public Float getGrade8() { return grade8; }

    public Float getGrade9() { return grade9; }

    public Float getGrade10() { return grade10; }

    public String getStudentName(){
        return studentName;
    }
}
