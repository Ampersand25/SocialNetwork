package main.validator;

import main.domain.FriendshipByID;
import main.exceptions.ValidationException;

public class FriendshipByIDValidator implements Validator<FriendshipByID> {

    /**
     * @param friendshipByID the friendship that needs to be validated
     * @throws ValidationException if the friendship is not valid (negative IDs)
     */
    @Override
    public void validate(FriendshipByID friendshipByID) throws ValidationException {
        String errors = "";
        if (friendshipByID.getID() < 0)
            errors += "ID of friendship is not valid.\n";
        if (friendshipByID.getFirstUserID() < 0)
            errors += "ID of first user is not valid.\n";
        if (friendshipByID.getSecondUserID() < 0)
            errors += "ID of second user is not valid.\n";
        if (friendshipByID.getDate() == null)
            errors += "Date is not valid.\n";
        if (!errors.equals("")) {
            StringBuilder sb = new StringBuilder(errors);
            sb.deleteCharAt(errors.length() - 1); // deleting last '\n'
            throw new ValidationException(sb.toString());
        }
    }
}
