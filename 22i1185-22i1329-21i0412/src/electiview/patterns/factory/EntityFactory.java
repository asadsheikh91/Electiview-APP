package electiview.patterns.factory;

import electiview.models.*;

public class EntityFactory {
    
    public static User createUser(String rollNumber, String password, String name, String email, String userType) {
        return new User(rollNumber, password, name, email, userType);
    }
    
    public static Course createCourse(String courseCode, String courseTitle, int credits, String syllabus) {
        return new Course(courseCode, courseTitle, credits, syllabus);
    }
    
    public static Review createReview(int userId, int courseId, int rating, String comments) {
        return new Review(userId, courseId, rating, comments);
    }
    
    public static RollARNMapping createRollARNMapping(String rollNumber, String arnNumber) {
        return new RollARNMapping(rollNumber, arnNumber);
    }
    
    public static ReviewReport createReviewReport(int reviewId, int userId, String reason) {
        return new ReviewReport(reviewId, userId, reason);
    }
}
