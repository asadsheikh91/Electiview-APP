package electiview.database;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:electiview.db";
    
    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    private void initializeDatabase() {
        try {
            Statement stmt = connection.createStatement();
            
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roll_number TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "email TEXT," +
                    "user_type TEXT NOT NULL," +
                    "is_active INTEGER DEFAULT 1)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "course_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "course_code TEXT UNIQUE NOT NULL," +
                    "course_title TEXT NOT NULL," +
                    "instructor TEXT," +
                    "credits INTEGER," +
                    "syllabus TEXT," +
                    "is_active INTEGER DEFAULT 1)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS reviews (" +
                    "review_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "course_id INTEGER NOT NULL," +
                    "rating INTEGER NOT NULL CHECK(rating >= 1 AND rating <= 5)," +
                    "comments TEXT," +
                    "status TEXT DEFAULT 'APPROVED' CHECK(status IN ('PENDING', 'APPROVED', 'REJECTED'))," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "report_count INTEGER DEFAULT 0," +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                    "FOREIGN KEY(course_id) REFERENCES courses(course_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS roll_arn_mapping (" +
                    "mapping_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roll_number TEXT UNIQUE NOT NULL," +
                    "arn_number TEXT NOT NULL," +
                    "is_used INTEGER DEFAULT 0," +
                    "attempt_count INTEGER DEFAULT 0," +
                    "is_locked INTEGER DEFAULT 0)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS review_reports (" +
                    "report_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "review_id INTEGER NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "reason TEXT NOT NULL," +
                    "reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "UNIQUE(review_id, user_id)," +
                    "FOREIGN KEY(review_id) REFERENCES reviews(review_id) ON DELETE CASCADE," +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
