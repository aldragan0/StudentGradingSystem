package service;

import domain.Assignment;
import exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import repo.AssignmentXMLFileRepo;
import validator.AssignmentValidator;

import static org.junit.Assert.*;

public class AssignmentServiceTest {
    private AssignmentService service;

    @Before
    public void setUp() {
        String assignmentFilePath = "src/test/resources/assignmentTest.xml";
        service = new AssignmentService(
                        new AssignmentXMLFileRepo(new AssignmentValidator(), assignmentFilePath));
    }

    @Test
    public void getSize(){
        assertEquals(service.getSize(), 3);
    }

    @Test
    public void getAssignment() {
        assertEquals(service.getAssignment(new Assignment("1")).toString(),
                "1 Assignment1 2 1");
        assertNull(service.getAssignment(new Assignment("100")));
    }

    @Test
    public void addAssignment() {
        Assignment assignment = new Assignment("5", "descriere", 14, 11);
        Assignment response = service.addAssignment(assignment);
        assertNull(response);
        assignment = new Assignment("1", "desc", 5, 4);
        response = service.addAssignment(assignment);
        assertNotNull(response);
        try{
            assignment = new Assignment(null, "descriere", 0, null);
            service.addAssignment(assignment);
            fail();
        } catch (ValidationException ex){
            assertTrue(true);
        }
    }

    @Test
    public void updateAssignment() {
        Assignment assignment = new Assignment("3", "update_descriere",
                6, 1);
        Assignment response = service.updateAssignment(assignment);
        assertNull(response);
        assignment = new Assignment("100", "descriere", 10, 9);
        response = service.updateAssignment(assignment);
        assertNotNull(response);
        try{
            assignment = new Assignment(null, "", 3, 4);
            service.updateAssignment(assignment);
        } catch (ValidationException ex){
            assertTrue(true);
        }
    }

    @Test
    public void deleteAssignment(){
        Assignment assignment = new Assignment("5");
        Assignment response = service.deleteAssignment(assignment);
        assertNotNull(response);
        response = service.deleteAssignment(assignment);
        assertNull(response);
    }

    @Test
    public void updateDeadlineAssignment() {
        Assignment assignment = new Assignment("30", 1);
        Assignment response = service.updateDeadlineAssignment(assignment);
        assertNotNull(response);
        assignment = new Assignment("3", -1);
        response = service.updateDeadlineAssignment(assignment);
        assertNotNull(response);
        assignment = new Assignment("3", 1);
        response = service.updateDeadlineAssignment(assignment);
        assertNotNull(response);
        assignment = new Assignment("5", 14);
        response = service.updateDeadlineAssignment(assignment);
        assertNull(response);
    }
}