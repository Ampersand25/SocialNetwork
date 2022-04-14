package main.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message implements Entity<Integer> {
    private final int ID;
    private final User from;
    private final List<User> to;
    private final String message;
    private final LocalDateTime dateTime;
    private final Message reply;

    public Message(int ID, User from, List<User> to, String message, LocalDateTime dateTime, Message reply) {
        this.ID = ID;
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

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Message getReply() {
        return reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return ID == message1.ID && from.equals(message1.from) && to.equals(message1.to) && message.equals(message1.message) && Objects.equals(reply, message1.reply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, from, to, message, reply);
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID=" + ID +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                ", reply=" + reply +
                '}';
    }
}
