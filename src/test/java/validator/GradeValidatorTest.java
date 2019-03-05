package validator;

import domain.Assignment;
import domain.Grade;
import domain.Student;
import exceptions.ValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GradeValidatorTest {
    private Validator<Grade> validator = new GradeValidator();
    private Grade invalidGrade1 = new Grade(new Student("1", null, null,
            null, null),
            new Assignment("10", null,
                    5, null),
            10,
            10f);

    private Grade invalidGrade2 = new Grade(new Student("1", "Vasile", 223,
            null, null),
            new Assignment("10", "tema",
                    5, 3),
            4,
            10f);
    private Grade invalidGrade3 = new Grade(new Student("1", "Vasile", 223,
           "vasile@vasile.com", "Mihai"),
            new Assignment("10", "tema",
                    5, 3),
            4,
            11f);
    private Grade validGrade = new Grade(new Student("1", "Vasile", 223,
            "mail@mail.com", "prof"),
            new Assignment("10", "tema",
                    5, 3),
            4,
            10f);
    @Test
    public void validate() {
        try{
            validator.validate(invalidGrade1);
            fail();
        }
        catch (ValidationException ex){
            assertTrue(!ex.getMessage().isEmpty());
        }
        try{
            validator.validate(invalidGrade2);
            fail();
        }
        catch (ValidationException ex){
            assertTrue(!ex.getMessage().isEmpty());
        }
        try{
            validator.validate(invalidGrade3);
            fail();
        }
        catch (ValidationException ex){
            assertTrue(!ex.getMessage().isEmpty());
        }
        validator.validate(validGrade);
    }
}