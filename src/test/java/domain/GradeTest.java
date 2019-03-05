package domain;

import org.junit.Test;
import service.GradeService;

import static org.junit.Assert.*;

public class GradeTest {
    private Student s = new Student("10", "Ion", 223, "mail_stud", "prof");
    private Assignment t = new Assignment("10", "desc_assignment", 14, 10);
    private Grade reg = new Grade(s, t, 10, 10f);

    @Test
    public void getID() {
        assertEquals(reg.getID(), "1010");
    }

    @Test
    public void setID() {
        reg.setID("100");
        assertEquals(reg.getID(), "100");
    }

    @Test
    public void getStudent() {
        assertEquals(reg.getStudent(), s);
    }

    @Test
    public void getAssignment() {
        assertEquals(reg.getAssignment(), t);
    }

    @Test
    public void getGrade() {
        assertEquals(reg.getGrade(), 10f, 0.01f);
    }

    @Test
    public void setGrade() {
        reg.setGrade(7.5f);
        assertEquals(reg.getGrade(), 7.5f, 0.01f);
    }

    @Test
    public void getWeekGraded(){
        assertEquals((int)reg.getWeekGraded(), 10);
    }

    @Test
    public void setWeekGraded(){
        reg.setWeekGraded(100);
        assertEquals((int)reg.getWeekGraded(), 100);
        assertEquals(GradeService.calcGrade(reg),1.0f, 0.01f);
        reg.setWeekGraded(16);
        assertEquals(GradeService.calcGrade(reg), 5f, 0.01f);
    }

    @Test
    public void equals(){
        Grade other = new Grade(new Student("1", "Ana", 223, "email", "prof"),
                new Assignment("10", "desc", 200, 140),
                10,
                10f);
        Grade other2 = new Grade(new Student("10", "Ana", 223, "email", "prof"),
                new Assignment("10", "desc", 200, 140),
                10,
                10f);
        assertNotEquals(reg, other);
        assertEquals(reg, other2);
    }
}