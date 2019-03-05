package service;

import domain.Student;
import exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import repo.StudentXMLFileRepo;
import validator.StudentValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class StudentServiceTest {
    private StudentService service;

    @Before
    public void setUp() {
        String studFilePath = "src/test/resources/studentsTest.xml";
        service = new StudentService(
                        new StudentXMLFileRepo(new StudentValidator(), studFilePath));
    }

    @Test
    public void getStudent() {
        Student student = service.getAll().iterator().next();
        Student existentStudent = new Student(student.getID());
        assertNotNull(service.getStudent(existentStudent));
        assertNull(service.getStudent(new Student("100")));
        assertEquals(service.getSize(), 5);
    }

    @Test
    public void addStudent() {
        Student stud = new Student("19","nume_test", 224,
                "mail_test@mail.com","prof_lab_test");
        Student response = service.addStudent(stud);
        assertNull(response);

        try{
            service.addStudent(new Student(
                    null, "", 223, "", ""));
            fail();
        } catch (ValidationException ex){
            assertTrue(true);
        }
    }

    //TODO: make test work
    @Test
    public void removeStudent() {
        Optional<Student> toDelete = service.getAll().stream()
                .filter(student -> student.getID().contains("nu")).findFirst();
        Student response;
        if(toDelete.isPresent()) {
            response = service.removeStudent(toDelete.get());
            assertNotNull(response);
            response = service.removeStudent(new Student("nu1025"));
            assertNull(response);
        }
        response = service.removeStudent(new Student("0"));
        assertNull(response);
    }

    @Test
    public void updateStudent() {
        Student stud = new Student("10", "updatedStudent", 225,
                "emailUpdated@mail_updated.com", "updatedProfLab");
        Student response = service.updateStudent(stud);
        assertNull(response);
        stud = new Student("12938192", "unusedName", 222,
                "email@mailnou.com", "prof");
        response = service.updateStudent(stud);
        assertNotNull(response);

        stud = new Student("", "", 36345, "email_invalid", "Prof");
        try{
            service.updateStudent(stud);
            fail();
        } catch (ValidationException ex){
            assertTrue(true);
        }
    }

    @Test
    public void getNextRepoPage() {
        service.setCurrentPageNumber(0);
        List<Student> pageItems;
        for(int i = 0; i < 2; ++i) {
            pageItems = service.getNextRepoPage();
            assertNotNull(pageItems);
        }
        for(int i = 0; i < 2; ++i) {
            pageItems = service.getNextRepoPage();
            assertNull(pageItems);
        }
        service.setCurrentPageNumber(0);
    }

    @Test
    public void getPrevRepoPage() {
        service.setCurrentPageNumber(2);
        List<Student> pageItems;
        for(int i = 0; i < 2; ++i) {
            pageItems = service.getPrevRepoPage();
            assertNotNull(pageItems);
        }
        for(int i = 0; i < 2; ++i) {
            pageItems = service.getPrevRepoPage();
            assertNull(pageItems);
        }
        service.setCurrentPageNumber(0);
    }

    @Test
    public void getSetCurrentPageNumber() {
        assertTrue(service.getCurrentPageNumber() >= 0);
        int pageNumber = service.getCurrentPageNumber();
        service.setCurrentPageNumber(10);
        assertEquals(service.getCurrentPageNumber(), 10);
        service.setCurrentPageNumber(pageNumber);
    }

    @Test
    public void getAll() {
        Collection<Student> students = service.getAll();
        assertNotNull(students);
    }

    @Test
    public void getSize() {
        int numStudents = service.getSize();
        assertTrue(numStudents >= 4);
    }

    @Test
    public void setPageSize() {
        service.setCurrentPageNumber(0);
        service.setPageSize(2);
        List<Student> pageItems = service.getNextRepoPage();
        assertEquals(pageItems.size(), 2);
        service.setPageSize(3);
    }
}