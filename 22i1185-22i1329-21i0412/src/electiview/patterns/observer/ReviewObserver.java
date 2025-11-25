package electiview.patterns.observer;

import electiview.models.Review;

public interface ReviewObserver {
    void onReviewSubmitted(Review review);
    void onReviewReported(Review review);
    void onReviewApproved(Review review);
    void onReviewRejected(Review review);
}
