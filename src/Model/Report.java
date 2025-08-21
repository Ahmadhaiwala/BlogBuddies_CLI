package Model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Report {
    public int reportId;
    public int reporterId;
    public Integer blogId;
    public Integer commentId;
    public Integer reportedUserId;
    public String reason;
    public String status;
    public LocalDate createdOn;

    public Report(int reportId, int reporterId, int blogId, int commentId, int reportedUserId,
                  String reason, String status, LocalDate createdOn) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.blogId = blogId;
        this.commentId = commentId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.status = status;
        this.createdOn = createdOn;
    }

    public Report(int reporterId, Integer blogId, Integer commentId, Integer reportedUserId, String reason) {
        this.reporterId = reporterId;
        this.blogId = blogId;
        this.commentId = commentId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.status = "pending";
        this.createdOn = LocalDate.now();
    }

    public Report(int reportId, int reporterId, int reportedUserId, int blogId, int commentId, String reason, Date createdOn) {
        this.reportId=reportId;
        this.reporterId=reporterId;
        this.reportedUserId=reportedUserId;
        this.blogId=blogId;
        this.commentId=commentId;
        this.reason=reason;
        this.createdOn=createdOn.toLocalDate();
    }
}
