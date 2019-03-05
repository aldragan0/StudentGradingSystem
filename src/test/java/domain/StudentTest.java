package domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {
    private Student s = new Student("10", "Ion", 223, "i@ion.com", "Maria");

    @Test
    public void getID() {
        assertEquals(s.getID(), "10");
    }

    @Test
    public void setID() {
        s.setID("100");
        assertEquals(s.getID(), "100");
    }

    @Test
    public void getName() {
        assertEquals(s.getName(), "Ion");
    }

    @Test
    public void setName() {
        s.setName("Ana");
        assertEquals(s.getName(), "Ana");
    }

    @Test
    public void getGroup() {
        assertEquals((int)s.getGroup(), 223);
    }

    @Test
    public void setGroup() {
        s.setGroup(224);
        assertEquals((int)s.getGroup(), 224);
    }

    @Test
    public void getEmail() {
        assertEquals(s.getEmail(), "i@ion.com");
    }

    @Test
    public void setEmail() {
        s.setEmail("ion@ion.com");
        assertEquals(s.getEmail(), "ion@ion.com");
    }

    @Test
    public void getTeacher() {
        assertEquals(s.getTeacher(), "Maria");
    }

    @Test
    public void setTeacher() {
        s.setTeacher("Ion");
        assertEquals(s.getTeacher(), "Ion");
    }

    @Test
    public void equals() {
        Student other = new Student("100", "Maria", 3330, "mair@ma.com", "Prof");
        Student other2 = new Student("10", "Ion", 300, "ion_ion@.com", "Prof2");
        assertNotEquals(other, s);
        assertEquals(other2, s);
    }

    @Test
    public void assignId() {
        Student student = new Student("Ion", 223, "ion@ion.com", "Petru");
        Student.assignId(student);
        Student student1 = new Student("Ion", 223, "ion@ion.com", "Petru");
        Student.assignId(student1);
        assertNotEquals(student, student1);

        Index.resetIndex();
        Student.assignId(student1);
        assertEquals(student.getID(), student1.getID());
        Index.nextIndex();

        assertEquals(Index.getIndexId("io1000"), 1000L);
    }
}