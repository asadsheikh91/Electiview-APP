package electiview.patterns.strategy;

import electiview.models.Review;
import java.util.List;

public interface RatingStrategy {
    double calculateRating(List<Review> reviews);
}
