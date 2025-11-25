package electiview.models;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int userId;
    private int courseId;
    private int rating;
    private String comments;
    private String status;
    private Timestamp createdAt;
    private int reportCount;
    
    public Review() {
    }
    
    public Review(int userId, int courseId, int rating, String comments) {
        this.userId = userId;
        this.courseId = courseId;
        this.rating = rating;
        this.comments = comments;
        this.status = "APPROVED";
        this.reportCount = 0;
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
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public int getReportCount() {
        return reportCount;
    }
    
    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
}
