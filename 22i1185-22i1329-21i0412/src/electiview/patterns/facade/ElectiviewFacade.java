package electiview.patterns.facade;

import electiview.dao.*;
import electiview.models.*;
import electiview.patterns.factory.EntityFactory;
import electiview.patterns.observer.ReviewNotifier;
import electiview.patterns.strategy.AverageRatingStrategy;
import electiview.patterns.strategy.RatingStrategy;

import java.util.List;

public class ElectiviewFacade {
    private static ElectiviewFacade instance;
    private UserDAO userDAO;
    private CourseDAO courseDAO;
    private ReviewDAO reviewDAO;
    private ReviewReportDAO reviewReportDAO;
    private RollARNDAO rollARNDAO;
    private RatingStrategy ratingStrategy;
    private ReviewNotifier reviewNotifier;
    
    private ElectiviewFacade() {
        userDAO = new UserDAO();
        courseDAO = new CourseDAO();
        reviewDAO = new ReviewDAO();
        reviewReportDAO = new ReviewReportDAO();
        rollARNDAO = new RollARNDAO();
        ratingStrategy = new AverageRatingStrategy();
        reviewNotifier = ReviewNotifier.getInstance();
    }
    
    public static synchronized ElectiviewFacade getInstance() {
        if (instance == null) {
            instance = new ElectiviewFacade();
        }
        return instance;
    }
    
    public void setRatingStrategy(RatingStrategy strategy) {
        this.ratingStrategy = strategy;
    }
    
    public RatingStrategy getRatingStrategy() {
        return this.ratingStrategy;
    }
    
    public User registerUser(String rollNumber, String arnNumber, String password, String name, String email, String userType) {
        RollARNMapping mapping = rollARNDAO.getMappingByRollNumber(rollNumber);
        
        if (mapping == null) {
            return null;
        }
        
        if (mapping.isLocked()) {
            return null;
        }
        
        if (mapping.isUsed()) {
            return null;
        }
        
        if (!rollARNDAO.validateRollARN(rollNumber, arnNumber)) {
            rollARNDAO.incrementAttempt(rollNumber);
            return null;
        }
        
        User existingUser = userDAO.getUserByRollNumber(rollNumber);
        if (existingUser != null) {
            return existingUser;
        }
        
        User user = EntityFactory.createUser(rollNumber, password, name, email, userType);
        if (userDAO.createUser(user)) {
            rollARNDAO.markAsUsed(rollNumber);
            return userDAO.getUserByRollNumber(rollNumber);
        }
        
        return null;
    }
    
    public User loginUser(String rollNumber, String password) {
        return userDAO.authenticate(rollNumber, password);
    }
    
    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }
    
    public List<Course> getAllCoursesForAdmin() {
        return courseDAO.getAllCourses();
    }
    
    public Course getCourseDetails(int courseId) {
        return courseDAO.getCourseById(courseId);
    }
    
    public List<Review> getCourseReviews(int courseId) {
        return reviewDAO.getReviewsByCourse(courseId);
    }
    
    public List<Review> getUserReviews(int userId) {
        return reviewDAO.getReviewsByUser(userId);
    }
    
    public boolean submitReview(int userId, int courseId, int rating, String comments) {
        Review review = EntityFactory.createReview(userId, courseId, rating, comments);
        if (reviewDAO.createReview(review)) {
            reviewNotifier.notifyReviewSubmitted(review);
            return true;
        }
        return false;
    }
    
    public boolean reportReview(int reviewId, int userId, String reason) {
        if (reviewDAO.reportReview(reviewId, userId, reason)) {
            Review review = new Review();
            review.setReviewId(reviewId);
            reviewNotifier.notifyReviewReported(review);
            return true;
        }
        return false;
    }
    
    public boolean hasUserReportedReview(int reviewId, int userId) {
        return reviewDAO.hasUserReportedReview(reviewId, userId);
    }
    
    public List<Review> getPendingReviews() {
        return reviewDAO.getPendingReviews();
    }
    
    public List<Review> getReportedReviews() {
        return reviewDAO.getReportedReviews();
    }
    
    public boolean approveReview(int reviewId) {
        if (reviewDAO.updateReviewStatus(reviewId, "APPROVED")) {
            Review review = reviewDAO.getReviewById(reviewId);
            if (review != null) {
                reviewNotifier.notifyReviewApproved(review);
            }
            return true;
        }
        return false;
    }
    
    public boolean rejectReview(int reviewId) {
        if (reviewDAO.updateReviewStatus(reviewId, "REJECTED")) {
            Review review = reviewDAO.getReviewById(reviewId);
            if (review != null) {
                reviewNotifier.notifyReviewRejected(review);
            }
            return true;
        }
        return false;
    }
    
    public boolean deleteReview(int reviewId) {
        reviewReportDAO.deleteReportsByReviewId(reviewId);
        return reviewDAO.deleteReview(reviewId);
    }
    
    public int getReportCountForReview(int reviewId) {
        return reviewReportDAO.getReportCountByReviewId(reviewId);
    }
    
    public List<ReviewReport> getReportsForReview(int reviewId) {
        return reviewReportDAO.getReportsByReviewId(reviewId);
    }
    
    public List<ReviewReport> getAllReports() {
        return reviewReportDAO.getAllReports();
    }
    
    public int getAcademicReviewCount(int courseId) {
        return reviewDAO.getAcademicReviewCount(courseId);
    }
    
    public int getPostAcademicReviewCount(int courseId) {
        return reviewDAO.getPostAcademicReviewCount(courseId);
    }
    
    public double getAcademicAverageRating(int courseId) {
        return reviewDAO.getAcademicAverageRating(courseId);
    }
    
    public double getPostAcademicAverageRating(int courseId) {
        return reviewDAO.getPostAcademicAverageRating(courseId);
    }
    
    public CourseStatistics getCourseStatistics(int courseId) {
        return courseDAO.getCourseStatistics(courseId);
    }
    
    public boolean addCourse(String courseCode, String courseTitle, int credits, String syllabus) {
        Course course = EntityFactory.createCourse(courseCode, courseTitle, credits, syllabus);
        return courseDAO.createCourse(course);
    }
    
    public boolean updateCourse(Course course) {
        return courseDAO.updateCourse(course);
    }
    
    public boolean deactivateCourse(int courseId) {
        return courseDAO.deactivateCourse(courseId);
    }
    
    public boolean activateCourse(int courseId) {
        return courseDAO.activateCourse(courseId);
    }
    
    public boolean deleteCourse(int courseId) {
        return courseDAO.deleteCourse(courseId);
    }
    
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public boolean deactivateUser(int userId) {
        return userDAO.deleteUser(userId);
    }
    
    public List<RollARNMapping> getAllRollARNMappings() {
        return rollARNDAO.getAllMappings();
    }
    
    public void addSampleRollARNData() {
        // Add sample roll-ARN mappings for testing
        for (int i = 1; i <= 10; i++) {
            RollARNMapping mapping = EntityFactory.createRollARNMapping("ROLL" + i, "ARN" + i);
            rollARNDAO.createMapping(mapping);
        }
    }
    
    public boolean addRollARNMapping(String rollNumber, String arnNumber) {
        RollARNMapping mapping = EntityFactory.createRollARNMapping(rollNumber, arnNumber);
        return rollARNDAO.createMapping(mapping);
    }
    
    public RollARNMapping getRollARNMapping(String rollNumber) {
        return rollARNDAO.getMappingByRollNumber(rollNumber);
    }
    
    public boolean courseCodeExists(String courseCode) {
        return courseDAO.courseCodeExists(courseCode);
    }
    
    public Review getReviewById(int reviewId) {
        return reviewDAO.getReviewById(reviewId);
    }
    
    public boolean isUserAlreadyRegistered(String rollNumber) {
        return userDAO.getUserByRollNumber(rollNumber) != null;
    }
    
    public boolean isRollNumberLocked(String rollNumber) {
        RollARNMapping mapping = rollARNDAO.getMappingByRollNumber(rollNumber);
        return mapping != null && mapping.isLocked();
    }
    
    public int getRemainingRegistrationAttempts(String rollNumber) {
        RollARNMapping mapping = rollARNDAO.getMappingByRollNumber(rollNumber);
        if (mapping != null) {
            return Math.max(0, 5 - mapping.getAttemptCount());
        }
        return 0;
    }
    
    public boolean unlockRollNumber(String rollNumber) {
        return rollARNDAO.unlockRoll(rollNumber);
    }
    
    public boolean reviewExists(int reviewId) {
        return reviewDAO.getReviewById(reviewId) != null;
    }
    
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
    
    public boolean activateUser(int userId) {
        return userDAO.activateUser(userId);
    }
}
