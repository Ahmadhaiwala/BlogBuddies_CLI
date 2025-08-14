package Model;

import java.sql.Date;


public class Blog {
    public int blogId;
    public int userId;
    public String username; // ✔️ Added
    public String title;
    public String content;
    public Date createdOn;

    public Blog(int blogId, int userId, String username, String title, String content, Date createdOn) {
        this.blogId = blogId;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
    }
}


