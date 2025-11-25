package electiview.dao;

import electiview.database.DatabaseManager;
import electiview.models.Course;
import electiview.models.CourseStatistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private Connection connection;
    
    public CourseDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    public boolean createCourse(Course course) {
        if (courseCodeExists(course.getCourseCode())) {
            return false;
        }
        String sql = "INSERT INTO courses (course_code, course_title, credits, syllabus) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseTitle());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getSyllabus());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean courseCodeExists(String courseCode) {
        String sql = "SELECT 1 FROM courses WHERE course_code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    public List<Course> getAllActiveCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE is_active = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_code = ?, course_title = ?, credits = ?, syllabus = ? WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseTitle());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getSyllabus());
            pstmt.setInt(5, course.getCourseId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deactivateCourse(int courseId) {
        String sql = "UPDATE courses SET is_active = 0 WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean activateCourse(int courseId) {
        String sql = "UPDATE courses SET is_active = 1 WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public CourseStatistics getCourseStatistics(int courseId) {
        String sql = "SELECT c.course_id, c.course_code, c.course_title, " +
                     "AVG(r.rating) as avg_rating, COUNT(r.review_id) as review_count " +
                     "FROM courses c LEFT JOIN reviews r ON c.course_id = r.course_id " +
                     "WHERE c.course_id = ? AND r.status = 'APPROVED' " +
                     "GROUP BY c.course_id, c.course_code, c.course_title";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CourseStatistics(
                    rs.getInt("course_id"),
                    rs.getString("course_code"),
                    rs.getString("course_title"),
                    rs.getDouble("avg_rating"),
                    rs.getInt("review_count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseTitle(rs.getString("course_title"));
        course.setCredits(rs.getInt("credits"));
        course.setSyllabus(rs.getString("syllabus"));
        course.setActive(rs.getInt("is_active") == 1);
        return course;
    }
}
