package validator;

import domain.Student;
import exceptions.ValidationException;

public class StudentValidator implements Validator<Student> {
    private String msg;

    /**
     * validates a given entity
     * @param entity entity to be validated
     * @throws ValidationException if the entity is invalid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        msg = "";
        validateId(entity);
        validateName(entity);
        validateGroup(entity);
        validateEmail(entity);
        validateTeacher(entity);
        if(!msg.isEmpty())
            throw new ValidationException(msg);
    }

    private void validateId(Student entity){
        if(entity.getID() == null || entity.getID().equals(""))
            msg += "Id is invalid\n";
    }
    private void validateName(Student entity){
        if(entity.getName() == null ||  entity.getName().equals(""))
            msg += "Student name is empty\n";
    }
    private void validateGroup(Student entity){
        if(entity.getGroup() == null
                || (entity.getGroup() < 221
                || entity.getGroup() > 227))
            msg += "Group is not between 221 and 227\n";
    }
    private void validateEmail(Student entity){
        if(entity.getEmail() == null
                || entity.getEmail().equals("")
                || !entity.getEmail().matches(".+@.{3,}\\.[a-z]{2,3}"))
            msg += "Invalid email\n";
    }
    private void validateTeacher(Student entity){
        if(entity.getTeacher() == null || entity.getTeacher().equals(""))
            msg += "Teacher name is empty\n";
    }
}
