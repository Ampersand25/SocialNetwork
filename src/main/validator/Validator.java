package main.validator;

import main.exceptions.ValidationException;

/**
 * @param <T> type of entity that must be validated
 */
public interface Validator<T> {
    /**
     * @param entity entity that must be validated
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}
