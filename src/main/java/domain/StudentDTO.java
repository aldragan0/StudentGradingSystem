package domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import java.util.stream.Collectors;

public class StudentDTO extends Student{
    BooleanProperty selected;

    /**
     * converts an object to a specified type
     * @param student object to convert
     * @return converted object
     */
    public static StudentDTO toStudentDTO(Student student){
        return new StudentDTO(student.getID(), student.getName(),
                student.getGroup(), student.getEmail(),
                student.getTeacher(), false);
    }

    /**
     * converts each object in a list to a specified type
     * @param studentList list of objects to convert
     * @return list of converted objects
     */
    public static List<StudentDTO> toStudentDTOList(List<Student> studentList){
        if(studentList == null) return null;
        return studentList.stream()
                .map(StudentDTO::toStudentDTO)
                .collect(Collectors.toList());
    }

    public BooleanProperty getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public StudentDTO(String id, String name, Integer group, String email,
                      String teacher, boolean selected) {
        super(id, name, group, email, teacher);
        this.selected = new SimpleBooleanProperty(selected);
    }
}
