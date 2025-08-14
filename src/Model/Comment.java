package Model;

import java.sql.Date;

public class Comment {
    public int commentId;
    public int blogId;
    public int userId;
    public String username;
    public String content;
    public Date commentedOn;

    public Comment(int commentId, int blogId, int userId, String username, String content, Date commentedOn) {
        this.commentId = commentId;
        this.blogId = blogId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.commentedOn = commentedOn;
    }
}


