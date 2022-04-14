package main.service;

import main.domain.MessageByID;
import main.exceptions.ValidationException;
import main.repository.database.MessageDbRepository;
import main.utils.ComparatorMessageByDate;
import main.validator.MessageByIDValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageService {
    private final MessageDbRepository messageRepository;
    private final MessageByIDValidator messageValidator;

    public MessageService(MessageDbRepository messageRepository, MessageByIDValidator messageValidator) {
        this.messageRepository = messageRepository;
        this.messageValidator = messageValidator;
    }

    public void add(Integer from, List<Integer> to, String message) throws ValidationException {
        MessageByID messageByID = new MessageByID(from, to, message, LocalDateTime.now(), null);
        messageValidator.validate(messageByID);
        messageRepository.save(messageByID);
    }

    public List<MessageByID> getConversation(Integer firstUserID, Integer secondUserID) {
        List<MessageByID> messageByIDList = (ArrayList<MessageByID>)messageRepository.getAll();
        List<MessageByID> messageByIDListFiltered = new ArrayList<>();
        for (MessageByID messageByID : messageByIDList) {
            if (messageByID.getFrom() == firstUserID || messageByID.getFrom() == secondUserID) {
                if (messageByID.getTo().contains(firstUserID) || messageByID.getTo().contains(secondUserID)) {
                    messageByIDListFiltered.add(messageByID);
                }
            }
        }
        Collections.sort(messageByIDListFiltered, new ComparatorMessageByDate());
        return messageByIDListFiltered;
    }

    public MessageByID getOne(Integer messageID) {
        return messageRepository.getOne(messageID);
    }
}
