package validator;

import domain.Assignment;
import domain.Grade;
import domain.Student;
import exceptions.ValidationException;

public class GradeValidator implements Validator<Grade> {
    private String msg;
    private Validator<Student> sValidator;
    private Validator<Assignment> tValidator;

    public GradeValidator(){
        sValidator = new StudentValidator();
        tValidator = new AssignmentValidator();
    }

    /**
     * validates a given entity
     * @param entity entity to be validated
     * @throws ValidationException if the entity is invalid
     */
    @Override
    public void validate(Grade entity) throws ValidationException {
        msg = "";
        validateStudent(entity);
        validateAssignment(entity);
        validateGrade(entity);
        validateWeekGraded(entity);
        if(!msg.isEmpty()){
            throw new ValidationException(msg);
        }
    }

    private void validateStudent(Grade entity){
        if(entity.getStudent() == null) msg += "Invalid Student\n";
        else{
            try {
                sValidator.validate(entity.getStudent());
            }
            catch (ValidationException ex){
                msg += ex.getMessage();
            }
        }
    }
    private void validateAssignment(Grade entity){
        if(entity.getAssignment() == null) msg += "Invalid Assignment\n";
        else{
            try {
                tValidator.validate(entity.getAssignment());
            }
            catch (ValidationException ex){
                msg += ex.getMessage();
            }
        }
    }
    private void validateGrade(Grade entity){
        if(entity.getGrade() == null
                || ((entity.getGrade() < 0 || entity.getGrade() > 10)))
            msg += "Invalid Grade\n";
    }
    private void validateWeekGraded(Grade entity){
        if(entity.getWeekGraded() == null
                || (entity.getWeekGraded() < 1 || entity.getWeekGraded() > 14))
            msg += "Invalid WeekGraded\n";
    }
}
