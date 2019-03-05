package validator;

import domain.Assignment;
import exceptions.ValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssignmentValidatorTest {
    private Validator<Assignment> validator = new AssignmentValidator();
    private Assignment validAssignment = new Assignment("1", "Desc", 10, 4);
    private Assignment invalidAssignment = new Assignment(null, "", null, null);

    @Test
    public void validate() {
        try{
            validator.validate(invalidAssignment);
            fail();
        }
        catch (ValidationException ex){
            assertTrue(!ex.getMessage().isEmpty());
        }
        validator.validate(validAssignment);
    }
}