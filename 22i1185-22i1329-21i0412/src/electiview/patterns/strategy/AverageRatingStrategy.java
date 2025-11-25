package electiview.patterns.strategy;

import electiview.models.Review;
import java.util.List;

public class AverageRatingStrategy implements RatingStrategy {
    
    @Override
    public double calculateRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        
        return sum / reviews.size();
    }
}
