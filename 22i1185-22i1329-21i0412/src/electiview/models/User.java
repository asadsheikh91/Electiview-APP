package electiview.models;

public class User {
    private int userId;
    private String rollNumber;
    private String password;
    private String name;
    private String email;
    private String userType;
    private boolean isActive;
    
    public User() {
    }
    
    public User(String rollNumber, String password, String name, String email, String userType) {
        this.rollNumber = rollNumber;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.isActive = true;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getRollNumber() {
        return rollNumber;
    }
    
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
}
