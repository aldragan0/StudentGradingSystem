package repo;

import domain.Student;
import exceptions.ValidationException;
import validator.StudentValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentRepoFileTest {
    private CrudRepository<String, Student> repo;

    @Before
    public void setUp() {
        repo = new StudentXMLFileRepo(new StudentValidator(),
                                          "src/test/resources/studentsTest.xml");
    }

    @Test
    public void save() {
        try{
            repo.save(null);
            fail();
        }
        catch (IllegalArgumentException ex){
            assertTrue(true);
        }
        Student invalidStudent = new Student("0","",223, "", "");
        try{
            repo.save(invalidStudent);
            fail();
        }
        catch (ValidationException ex){
            assertTrue(true);
        }
        Student s = new Student("10", "Petru", 227,
                "nume@nume.com", "Moise");
        Student returnedStudent = repo.save(s);
        assertNull(returnedStudent);
        returnedStudent = repo.save(s);
        assertEquals(s, returnedStudent);
    }

    @Test
    public void delete() {
        Student s = new Student("10", "Petru", 227,
                "nume@nume.com", "Moise");
        try{
            repo.delete(null);
            fail();
        }
        catch (IllegalArgumentException ex){
            assertTrue(true);
        }
        Student returnedStudent = repo.delete(s.getID());
        assertEquals(returnedStudent, s);
        returnedStudent = repo.delete(s.getID());
        assertNull(returnedStudent);
    }

    @Test
    public void update() {
        Student s = new Student("3", "Avram", 226,
                "iancu@cluj.com", "Mihai_Viteazu");

        try{
            repo.update(null);
            fail();
        }
        catch (IllegalArgumentException ex){
            assertTrue(true);
        }

        Student invalid = new Student("100", "", 240,
                "", "");
        try{
            repo.update(invalid);
            fail();
        }
        catch(ValidationException ex){
            assertTrue(true);
        }

        Student returnedStudent = repo.update(s);
        assertNull(returnedStudent);
        Student s1 = new Student("80", "alin", 224,
                "alin@alin.com", "andrei");
        returnedStudent = repo.update(s1);
        assertEquals(returnedStudent, s1);
    }
}