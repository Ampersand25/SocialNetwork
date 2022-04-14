package main.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendRequest implements Entity<Integer> {
    private Integer id = -1;
    private final User from;
    private final User to;
    private final LocalDate date;
    private String status;

    public FriendRequest(Integer id, User from, User to, LocalDate date, String status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.status = status;
    }

    public FriendRequest(User from, User to, LocalDate date, String status) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.status = status;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
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
        return "FriendRequest{" +
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
        return Objects.hash(getFrom().hashCode(), getTo().hashCode());
    }

    @Override
    public Integer getID() {
        return id;
    }
}
