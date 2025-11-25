package electiview.patterns.observer;

import electiview.models.Review;
import java.util.ArrayList;
import java.util.List;

public class ReviewNotifier {
    private static ReviewNotifier instance;
    private List<ReviewObserver> observers;
    
    private ReviewNotifier() {
        observers = new ArrayList<>();
    }
    
    public static synchronized ReviewNotifier getInstance() {
        if (instance == null) {
            instance = new ReviewNotifier();
        }
        return instance;
    }
    
    public void addObserver(ReviewObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ReviewObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyReviewSubmitted(Review review) {
        for (ReviewObserver observer : observers) {
            observer.onReviewSubmitted(review);
        }
    }
    
    public void notifyReviewReported(Review review) {
        for (ReviewObserver observer : observers) {
            observer.onReviewReported(review);
        }
    }
    
    public void notifyReviewApproved(Review review) {
        for (ReviewObserver observer : observers) {
            observer.onReviewApproved(review);
        }
    }
    
    public void notifyReviewRejected(Review review) {
        for (ReviewObserver observer : observers) {
            observer.onReviewRejected(review);
        }
    }
}
