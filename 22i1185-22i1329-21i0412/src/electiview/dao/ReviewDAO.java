package electiview.dao;

import electiview.database.DatabaseManager;
import electiview.models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private Connection connection;
    
    public ReviewDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    public boolean createReview(Review review) {
        String sql = "INSERT INTO reviews (user_id, course_id, rating, comments, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getCourseId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComments());
            pstmt.setString(5, review.getStatus());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Review> getReviewsByCourse(int courseId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE course_id = ? AND status != 'REJECTED' AND report_count < 5 ORDER BY created_at DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    public List<Review> getReviewsByCourseAndType(int courseId, String userType) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.* FROM reviews r JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.course_id = ? AND r.status != 'REJECTED' AND r.report_count < 5 AND u.user_type = ? " +
                     "ORDER BY r.created_at DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, userType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    public List<Review> getReviewsByUser(int userId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE user_id = ? ORDER BY created_at DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    public List<Review> getPendingReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE status = 'PENDING' ORDER BY created_at DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    public List<Review> getReportedReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE status = 'PENDING' OR status = 'REJECTED' ORDER BY report_count DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    public boolean updateReviewStatus(int reviewId, String status) {
        String sql = "UPDATE reviews SET status = ?, report_count = 0 WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reviewId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasUserReportedReview(int reviewId, int userId) {
        String sql = "SELECT 1 FROM review_reports WHERE review_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean reviewExists(int reviewId) {
        String sql = "SELECT 1 FROM reviews WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM reviews WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractReviewFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getAcademicReviewCount(int courseId) {
        String sql = "SELECT COUNT(*) FROM reviews r JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.course_id = ? AND u.user_type = 'STUDENT' AND r.status != 'REJECTED' AND r.report_count < 5";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getPostAcademicReviewCount(int courseId) {
        String sql = "SELECT COUNT(*) FROM reviews r JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.course_id = ? AND u.user_type = 'ALUMNI' AND r.status != 'REJECTED' AND r.report_count < 5";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double getAcademicAverageRating(int courseId) {
        String sql = "SELECT AVG(r.rating) FROM reviews r JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.course_id = ? AND u.user_type = 'STUDENT' AND r.status != 'REJECTED' AND r.report_count < 5";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : avg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public double getPostAcademicAverageRating(int courseId) {
        String sql = "SELECT AVG(r.rating) FROM reviews r JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.course_id = ? AND u.user_type = 'ALUMNI' AND r.status != 'REJECTED' AND r.report_count < 5";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : avg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public boolean reportReview(int reviewId, int userId, String reason) {
        String sql = "INSERT INTO review_reports (review_id, user_id, reason) VALUES (?, ?, ?)";
        String updateSql = "UPDATE reviews SET report_count = report_count + 1, status = 'PENDING' WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, reason);
            int insertResult = pstmt.executeUpdate();
            System.out.println("Report inserted for reviewId: " + reviewId + ", userId: " + userId + ", reason: " + reason + ", insertResult: " + insertResult);
            
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setInt(1, reviewId);
                int updateResult = updateStmt.executeUpdate();
                System.out.println("Report count updated and status set to PENDING, updateResult: " + updateResult);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error in reportReview - ReviewId: " + reviewId + ", UserId: " + userId + ", Reason: " + reason);
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setCourseId(rs.getInt("course_id"));
        review.setRating(rs.getInt("rating"));
        review.setComments(rs.getString("comments"));
        review.setStatus(rs.getString("status"));
        review.setCreatedAt(rs.getTimestamp("created_at"));
        review.setReportCount(rs.getInt("report_count"));
        return review;
    }
}
