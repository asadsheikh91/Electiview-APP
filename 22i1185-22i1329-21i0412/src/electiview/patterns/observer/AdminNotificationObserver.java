package electiview.patterns.observer;

import electiview.models.Review;

public class AdminNotificationObserver implements ReviewObserver {
    
    @Override
    public void onReviewSubmitted(Review review) {
        System.out.println("[ADMIN NOTIFICATION] New review submitted - Review ID: " + review.getReviewId());
    }
    
    @Override
    public void onReviewReported(Review review) {
        System.out.println("[ADMIN ALERT] Review reported - Review ID: " + review.getReviewId() + 
                          ", Report Count: " + review.getReportCount());
    }
    
    @Override
    public void onReviewApproved(Review review) {
        System.out.println("[ADMIN NOTIFICATION] Review approved - Review ID: " + review.getReviewId());
    }
    
    @Override
    public void onReviewRejected(Review review) {
        System.out.println("[ADMIN NOTIFICATION] Review rejected - Review ID: " + review.getReviewId());
    }
}
