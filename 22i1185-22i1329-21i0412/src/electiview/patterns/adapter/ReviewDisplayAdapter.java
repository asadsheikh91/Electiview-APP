package electiview.patterns.adapter;

import electiview.models.Review;
import electiview.models.User;

/**
 * Adapter Interface for converting Review objects to display-friendly format.
 * Adapts the internal Review domain model to UI presentation requirements.
 */
public interface ReviewDisplayAdapter {
    /**
     * Adapts a Review object to a formatted display string
     */
    String adaptForDisplay(Review review, User reviewer);
    
    /**
     * Adapts a Review object to HTML format for rendering
     */
    String adaptForHtmlDisplay(Review review, User reviewer);
    
    /**
     * Adapts a Review object to a summary format
     */
    String adaptForSummary(Review review, User reviewer);
}
