package validator;

import domain.Account;
import exceptions.ValidationException;

public class AccountValidator implements Validator<Account> {
    /**
     * validates a given entity
     * @param entity entity to be validated
     * @throws ValidationException if the entity is invalid
     */
    @Override
    public void validate(Account entity) throws ValidationException {
        String message = "";
        if(entity.getAccountName().equals("")) message += "account name is empty\n";
        if(entity.getPassword().equals("")) message += "account password is empty\n";
        if(!message.isEmpty()) throw new ValidationException(message);
    }
}
