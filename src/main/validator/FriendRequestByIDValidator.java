package main.validator;

import main.domain.FriendRequestByID;
import main.exceptions.ValidationException;

import java.util.Arrays;
import java.util.List;

public class FriendRequestByIDValidator implements Validator<FriendRequestByID> {
    @Override
    public void validate(FriendRequestByID friendRequestByID) throws ValidationException {
        String errors = "";
        if (friendRequestByID.getFrom() <= 0)
            errors += "Sender is not valid.\n";
        if (friendRequestByID.getTo() <= 0)
            errors += "Receiver is not valid.\n";
        if (friendRequestByID.getFrom().equals(friendRequestByID.getTo()))
            errors += "User can't send friend request to himself.\n";
        List<String> statusList = Arrays.asList("pending", "approved", "rejected");
        if (friendRequestByID.getStatus().isEmpty() || !statusList.contains(friendRequestByID.getStatus().toLowerCase()))
            errors += "Status is not valid.\n";
        if (friendRequestByID.getDate() == null)
            errors += "Date is not valid.\n";
        if (!errors.equals("")) {
            StringBuilder sb = new StringBuilder(errors);
            sb.deleteCharAt(errors.length() - 1); // deleting last '\n'
            throw new ValidationException(sb.toString());
        }
    }
}
