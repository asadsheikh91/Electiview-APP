package electiview.patterns.strategy;

import electiview.models.Review;
import java.util.List;

public class WeightedRatingStrategy implements RatingStrategy {
    
    @Override
    public double calculateRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        
        double weightedSum = 0.0;
        double totalWeight = 0.0;
        
        for (int i = 0; i < reviews.size(); i++) {
            double weight = 1.0 / (i + 1);
            weightedSum += reviews.get(i).getRating() * weight;
            totalWeight += weight;
        }
        
        return weightedSum / totalWeight;
    }
}
