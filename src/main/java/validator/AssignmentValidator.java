package validator;

import domain.Assignment;
import exceptions.ValidationException;

public class AssignmentValidator implements Validator<Assignment> {
    private String msg;

    /**
     * validates a given entity
     * @param entity entity to be validated
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public void validate(Assignment entity) throws ValidationException {
        msg = "";
        validateId(entity);
        validateDeadline(entity);
        validateDescription(entity);
        validateWeekReceived(entity);
        if(!msg.isEmpty())
            throw new ValidationException(msg);
    }
    private void validateId(Assignment entity){
        if(entity.getID() == null || entity.getID().equals(""))
            msg += "Id is empty\n";
    }

    private void validateDescription(Assignment entity){
        if(entity.getDescription() == null || entity.getDescription().equals(""))
            msg += "Description is empty\n";
    }

    private void validateDeadline(Assignment entity){
        if(entity.getDeadline() == null
                || (entity.getDeadline() < 1 || entity.getDeadline() > 14))
            msg += "Deadline isn't between 1 and 14\n";
    }

    private void validateWeekReceived(Assignment entity){
        if(entity.getWeekReceived() == null
                || (entity.getWeekReceived() < 1 || entity.getWeekReceived() > 14)
        )
            msg += "Week received isn't between 1 and 14\n";
    }
}
