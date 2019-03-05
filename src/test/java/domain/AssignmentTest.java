package domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class AssignmentTest {
    private Assignment assignment = new Assignment("1", "assignment", 10, 11);
    private Assignment assignment1 = new Assignment("2", 10);

    @Test
    public void getID() {
        assertEquals(assignment.getID(), "1");
        assertEquals(assignment1.getID(), "2");
    }

    @Test
    public void setID() {
        assignment.setID("2");
        assertEquals(assignment.getID(), "2");
        assignment1.setID("1");
        assertEquals(assignment1.getID(), "1");
    }

    @Test
    public void getDescription() {
        assertEquals(assignment.getDescription(), "assignment");
        assertNull(assignment1.getDescription());
    }

    @Test
    public void setDescription() {
        assignment.setDescription("test");
        assertEquals(assignment.getDescription(), "test");
        assignment1.setDescription("test1");
        assertEquals(assignment1.getDescription(), "test1");
    }

    @Test
    public void getDeadline() {
        assertEquals((int) assignment.getDeadline(), 10);
        assertEquals((int) assignment1.getDeadline(), 10);
    }

    @Test
    public void setDeadline() {
        assignment.setDeadline(8);
        assertEquals((int) assignment.getDeadline(), 8);
        assignment1.setDeadline(8);
        assertEquals((int) assignment.getDeadline(), 8);
    }

    @Test
    public void getWeekReceived() {
        assertEquals((int) assignment.getWeekReceived(), 11);
        assertNull(assignment1.getWeekReceived());
    }

    @Test
    public void setWeekReceived() {
        assignment.setWeekReceived(7);
        assertEquals((int) assignment.getWeekReceived(), 7);
        assignment.setWeekReceived(7);
        assertEquals((int) assignment.getWeekReceived(), 7);
    }

    @Test
    public void equals(){
        Assignment other = new Assignment("100", "test1", 10, 7);
        Assignment other2 = new Assignment("1", "assignment", 9, 1);
        assertNotEquals(other, assignment);
        assertEquals(other2, assignment);
    }
}