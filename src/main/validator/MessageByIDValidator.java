package main.validator;

import main.domain.MessageByID;
import main.exceptions.ValidationException;

public class MessageByIDValidator implements Validator<MessageByID> {

    /**
     * @param message the message that needs to be validated
     * @throws ValidationException if the user is not valid
     */
    @Override
    public void validate(MessageByID message) throws ValidationException {
        String errors = "";
        if (message.getTo().contains(message.getFrom()))
            errors += "Sender is the same as receiver.\n";
        if (message.getFrom() < 0)
            errors += "Sender is not valid.\n";
        if (message.getTo().isEmpty())
            errors += "List of receivers is empty.\n";
        if (message.getMessage().equals(""))
            errors += "Message is not valid.\n";
        if (message.getDateTime() == null)
            errors += "Date is not valid.\n";
        if (!errors.equals("")) {
            StringBuilder sb = new StringBuilder(errors);
            sb.deleteCharAt(errors.length() - 1); // deleting last '\n'
            throw new ValidationException(sb.toString());
        }
    }
}
