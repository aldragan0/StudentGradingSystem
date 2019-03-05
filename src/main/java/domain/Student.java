package domain;

import exceptions.ValidationException;

import java.util.Objects;

public class Student implements HasID<String>{
    private String id;
    private String name;
    private Integer group;
    private String email;
    private String teacher;

    public static Account accountFromStudent(Student student){
        return new Account(student.getID(), student.getID(), AccountType.STUDENT);
    }

    /**
     * assign a generated, unique id to a student
     * @param student student to who assign id to
     * @throws ValidationException if studentName is shorter than two characters
     */
    public static void assignId(Student student){
        String prefix;
        try {
            prefix = student.getName().substring(0, 2).toLowerCase();
        } catch (StringIndexOutOfBoundsException ex){
            throw new ValidationException("name must contain at least two letters");
        }
        String id = prefix + Index.getIndex();
        Index.nextIndex();
        student.setID(id);
    }

    public Student(String id){
        this.id = id;
    }

    public Student(String id, String name, Integer group, String email, String teacher) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.email = email;
        this.teacher = teacher;
    }

    public Student(String name, Integer group, String email, String teacher){
        this.id = null;
        this.name = name;
        this.group = group;
        this.email = email;
        this.teacher = teacher;
    }

    public Student(Student student){
        this.setID(student.getID());
        this.setName(student.getName());
        this.setGroup(student.getGroup());
        this.setEmail(student.getEmail());
        this.setTeacher(student.getTeacher());
    }
    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return id + " " + name + " "
                 + group + " " + email
                 + " " + teacher;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        if(other == null || other.getClass() != Student.class) return false;
        return this.getID().equals(((Student) other).getID());
    }
}
