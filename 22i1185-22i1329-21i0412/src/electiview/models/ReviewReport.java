package electiview.models;

import java.sql.Timestamp;

public class ReviewReport {
    private int reportId;
    private int reviewId;
    private int userId;
    private String reason;
    private Timestamp reportedAt;
    
    public ReviewReport() {
    }
    
    public ReviewReport(int reviewId, int userId, String reason) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reason = reason;
    }
    
    public int getReportId() {
        return reportId;
    }
    
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
    
    public int getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Timestamp getReportedAt() {
        return reportedAt;
    }
    
    public void setReportedAt(Timestamp reportedAt) {
        this.reportedAt = reportedAt;
    }
}
