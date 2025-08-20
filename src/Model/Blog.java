package Model;

import java.sql.Date;


public class Blog {
    public int blogId;
    public int userId;
    public String username;
    public String title;
    public String content;
    public Date createdOn;
    public String visibility;

    public Blog(int blogId, int userId, String username, String title, String content, Date createdOn) {
        this.blogId = blogId;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
    }
    public Blog(int blogId, int userId, String username, String title, String content, Date createdOn,String visibility) {
        this.blogId = blogId;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
        this.visibility=visibility;
    }
}


