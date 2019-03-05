package validator;

import exceptions.ValidationException;
import org.junit.Test;

import domain.Student;
import static org.junit.Assert.*;

public class StudentValidatorTest {
    private Validator<Student> validator = new StudentValidator();
    private Student invalidStudent = new Student(null, "", null, "", "");
    private Student validStudent = new Student("3", "ion", 223, "mail@mail.com", "prof");

    @Test
    public void validate() {
        try{
            validator.validate(invalidStudent);
            fail();
        }
        catch (ValidationException ex){
            assertTrue(!ex.getMessage().isEmpty());
        }
        validator.validate(validStudent);
    }
}