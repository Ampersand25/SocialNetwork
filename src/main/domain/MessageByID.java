package main.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MessageByID implements Entity<Integer> {
    private int ID = -1;
    private final int from;
    private final List<Integer> to;
    private final String message;
    private final LocalDateTime dateTime;
    private final Integer reply;

    public MessageByID(int ID, int from, List<Integer> to, String message, LocalDateTime dateTime, Integer reply) {
        this.ID = ID;
        this.from = from;
        this.to = to;
        this.message = message;
        this.dateTime = dateTime;
        this.reply = reply;
    }

    public MessageByID(int from, List<Integer> to, String message, LocalDateTime dateTime, Integer reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.dateTime = dateTime;
        this.reply = reply;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    public int getFrom() {
        return from;
    }

    public List<Integer> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getReply() {
        return reply;
    }
}
