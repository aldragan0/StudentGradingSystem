package repo;

import domain.Student;
import exceptions.ValidationException;
import validator.StudentValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class InMemoryRepoTest {
    private CrudRepository<String, Student> repo;

    @Before
    public void setUp(){
        repo = new InMemoryRepo<>(new StudentValidator());
        repo.save(new Student("1", "Ion", 223, "mail@mail.com", "prof"));
        repo.save(new Student("2", "Maria", 223, "mail_maria@mail.com", "prof1"));
        repo.save(new Student("3", "Ana", 225, "mail_petru@mail.com", "prof2"));
        assertEquals((repo.findAll()).size(), 3);
    }

    @Test
    public void findOne() {
        try{
            repo.findOne(null);
            fail();
        }
        catch (IllegalArgumentException ex){
            assertTrue(true);
        }
        Student s = repo.findOne("2");
        assertEquals(s.getID(), "2");
    }

    @Test
    public void findAll() {
        Collection<Student> items = repo.findAll();
        assertEquals(items.size(), 3);
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
        Student s = new Student("1", "nume", 227,

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
        Student s = new Student("3", "Avram", 221,
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

        Student return_student = repo.update(s);
        assertNull(return_student);
        Student s1 = new Student("80", "alin", 223,
                "alin@alin.com", "andrei");
        return_student = repo.update(s1);
        assertEquals(return_student, s1);
    }
}