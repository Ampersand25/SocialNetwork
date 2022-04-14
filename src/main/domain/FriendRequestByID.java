package main.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendRequestByID implements Entity<Integer> {
    private Integer id = -1;
    private final Integer from;
    private final Integer to;
    private final LocalDate date;
    private String status;

    public FriendRequestByID(Integer id, Integer from, Integer to, LocalDate date, String status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.status = status;
    }

    public FriendRequestByID(Integer from, Integer to, LocalDate date, String status) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.status = status;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendRequestByID{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequestByID)) return false;
        FriendRequestByID that = (FriendRequestByID) o;
        return (Objects.equals(getFrom(), that.getFrom()) && Objects.equals(getTo(), that.getTo())) ||
                (Objects.equals(getFrom(), that.getTo()) && Objects.equals(getTo(), that.getFrom()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo());
    }

    @Override
    public Integer getID() {
        return id;
    }
}
