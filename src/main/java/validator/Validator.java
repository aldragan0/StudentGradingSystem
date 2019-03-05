package validator;

import exceptions.ValidationException;

public interface Validator<E> {
    /**
     * validates an entity
     * @param entity entity to be validated
     * @throws ValidationException if the entity is not valid
     */
    void validate(E entity) throws ValidationException;
}


