package electiview.dao;

import electiview.database.DatabaseManager;
import electiview.models.ReviewReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewReportDAO {
    private Connection connection;
    
    public ReviewReportDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    public boolean createReport(ReviewReport report) {
        String sql = "INSERT INTO review_reports (review_id, user_id, reason) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, report.getReviewId());
            pstmt.setInt(2, report.getUserId());
            pstmt.setString(3, report.getReason());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ReviewReport getReportById(int reportId) {
        String sql = "SELECT * FROM review_reports WHERE report_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractReportFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ReviewReport> getReportsByReviewId(int reviewId) {
        List<ReviewReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM review_reports WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    
    public List<ReviewReport> getAllReports() {
        List<ReviewReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM review_reports ORDER BY reported_at DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    
    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM review_reports WHERE report_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteReportsByReviewId(int reviewId) {
        String sql = "DELETE FROM review_reports WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getReportCountByReviewId(int reviewId) {
        String sql = "SELECT COUNT(*) as count FROM review_reports WHERE review_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
    
    private ReviewReport extractReportFromResultSet(ResultSet rs) throws SQLException {
        ReviewReport report = new ReviewReport();
        report.setReportId(rs.getInt("report_id"));
        report.setReviewId(rs.getInt("review_id"));
        report.setUserId(rs.getInt("user_id"));
        report.setReason(rs.getString("reason"));
        report.setReportedAt(rs.getTimestamp("reported_at"));
        return report;
    }
}
