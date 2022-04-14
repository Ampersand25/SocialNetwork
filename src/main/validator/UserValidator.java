package main.validator;

import main.exceptions.ValidationException;
import main.domain.User;

public class UserValidator implements Validator<User> {

    /**
     * @param user the user that needs to be validated
     * @throws ValidationException if the user is not valid
     */
    @Override
    public void validate(User user) throws ValidationException {
        String errors = "";
        if (user.getID() < 0)
            errors += "ID is not valid.\n";
        if (user.getFirstName().equals(""))
            errors += "First name is not valid.\n";
        if (user.getLastName().equals(""))
            errors += "Last name is not valid.\n";
        if (!errors.equals("")) {
            StringBuilder sb = new StringBuilder(errors);
            sb.deleteCharAt(errors.length() - 1); // deleting last '\n'
            throw new ValidationException(sb.toString());
        }
    }
}
