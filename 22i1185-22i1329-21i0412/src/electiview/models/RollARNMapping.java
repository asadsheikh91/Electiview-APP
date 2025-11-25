package electiview.models;

public class RollARNMapping {
    private int mappingId;
    private String rollNumber;
    private String arnNumber;
    private boolean isUsed;
    private int attemptCount;
    private boolean isLocked;
    
    public RollARNMapping() {
    }
    
    public RollARNMapping(String rollNumber, String arnNumber) {
        this.rollNumber = rollNumber;
        this.arnNumber = arnNumber;
        this.isUsed = false;
        this.attemptCount = 0;
        this.isLocked = false;
    }
    
    public int getMappingId() {
        return mappingId;
    }
    
    public void setMappingId(int mappingId) {
        this.mappingId = mappingId;
    }
    
    public String getRollNumber() {
        return rollNumber;
    }
    
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    public String getArnNumber() {
        return arnNumber;
    }
    
    public void setArnNumber(String arnNumber) {
        this.arnNumber = arnNumber;
    }
    
    public boolean isUsed() {
        return isUsed;
    }
    
    public void setUsed(boolean used) {
        isUsed = used;
    }
    
    public int getAttemptCount() {
        return attemptCount;
    }
    
    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }
    
    public boolean isLocked() {
        return isLocked;
    }
    
    public void setLocked(boolean locked) {
        isLocked = locked;
    }
    
    public void incrementAttempt() {
        this.attemptCount++;
        if (this.attemptCount >= 5) {
            this.isLocked = true;
        }
    }
}
