package electiview.dao;

import electiview.database.DatabaseManager;
import electiview.models.RollARNMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RollARNDAO {
    private Connection connection;
    
    public RollARNDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    public boolean createMapping(RollARNMapping mapping) {
        String sql = "INSERT INTO roll_arn_mapping (roll_number, arn_number) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mapping.getRollNumber());
            pstmt.setString(2, mapping.getArnNumber());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public RollARNMapping getMappingByRollNumber(String rollNumber) {
        String sql = "SELECT * FROM roll_arn_mapping WHERE roll_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractMappingFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean validateRollARN(String rollNumber, String arnNumber) {
        String sql = "SELECT * FROM roll_arn_mapping WHERE roll_number = ? AND arn_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.setString(2, arnNumber);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean incrementAttempt(String rollNumber) {
        String sql = "UPDATE roll_arn_mapping SET attempt_count = attempt_count + 1, " +
                     "is_locked = CASE WHEN attempt_count >= 4 THEN 1 ELSE 0 END " +
                     "WHERE roll_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markAsUsed(String rollNumber) {
        String sql = "UPDATE roll_arn_mapping SET is_used = 1 WHERE roll_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean unlockRoll(String rollNumber) {
        String sql = "UPDATE roll_arn_mapping SET is_locked = 0, attempt_count = 0 WHERE roll_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<RollARNMapping> getAllMappings() {
        List<RollARNMapping> mappings = new ArrayList<>();
        String sql = "SELECT * FROM roll_arn_mapping";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mappings.add(extractMappingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mappings;
    }
    
    private RollARNMapping extractMappingFromResultSet(ResultSet rs) throws SQLException {
        RollARNMapping mapping = new RollARNMapping();
        mapping.setMappingId(rs.getInt("mapping_id"));
        mapping.setRollNumber(rs.getString("roll_number"));
        mapping.setArnNumber(rs.getString("arn_number"));
        mapping.setUsed(rs.getInt("is_used") == 1);
        mapping.setAttemptCount(rs.getInt("attempt_count"));
        mapping.setLocked(rs.getInt("is_locked") == 1);
        return mapping;
    }
}
