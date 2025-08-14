package Model;

import java.sql.Timestamp;

public class Notification {
    public int id;
    public int userId;
    public String fromUser;
    public String message;
    public Timestamp createdOn;
    public boolean seen;

    public Notification(int id, int userId, String fromUser, String message, Timestamp createdOn, boolean seen) {
        this.id = id;
        this.userId = userId;
        this.fromUser = fromUser;
        this.message = message;
        this.createdOn = createdOn;
        this.seen = seen;
    }

    public Notification(int userId, String fromUser, String message) {
        this.userId = userId;
        this.fromUser = fromUser;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[" + createdOn + "] From: " + fromUser + " - " + message + (seen ? " ✅ Seen" : " 🆕");
    }
}
