package electiview.patterns.adapter;

import electiview.models.Review;
import electiview.models.User;
import java.text.SimpleDateFormat;

/**
 * Concrete Adapter Implementation for converting Review objects to display formats.
 * This adapter transforms internal Review domain model into presentation-ready formats.
 * Follows Adapter Pattern - converts incompatible interfaces to make classes work together.
 */
public class ReviewDisplayAdapterImpl implements ReviewDisplayAdapter {
    
    private static final String RATING_STAR = "★";
    private static final String EMPTY_STAR = "☆";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm");
    
    /**
     * Adapts Review to plain text display format
     * Used for console and simple text displays
     */
    @Override
    public String adaptForDisplay(Review review, User reviewer) {
        if (review == null || reviewer == null) {
            return "[Invalid Review]";
        }
        
        return String.format(
            "Review by %s (%s)\n" +
            "Rating: %s\n" +
            "Status: %s\n" +
            "Date: %s\n" +
            "Comments: %s",
            reviewer.getName(),
            reviewer.getUserType(),
            buildRatingDisplay(review.getRating()),
            review.getStatus(),
            DATE_FORMAT.format(review.getCreatedAt()),
            review.getComments() != null ? review.getComments() : "No comments"
        );
    }
    
    /**
     * Adapts Review to HTML format for GUI rendering
     * Used for enhanced visual presentation with HTML formatting
     * Note: Does NOT include <html> wrapper - caller handles that
     */
    @Override
    public String adaptForHtmlDisplay(Review review, User reviewer) {
        if (review == null || reviewer == null) {
            return "[Invalid Review]";
        }
        
        String statusColor = getStatusColor(review.getStatus());
        String reportBadge = buildReportBadge(review.getReportCount());
        
        return String.format(
            "<div style='border: 1px solid #ccc; padding: 10px; margin: 8px 0; border-radius: 5px; background-color: #f9f9f9;'>" +
            "<b>Review ID: %d</b> | <b>By: %s</b> <span style='color: gray; font-size: 0.85em;'>(%s)</span>" +
            "%s" +
            "<br><span style='color: #FFD700;'>%s</span> " +
            "<span style='color: %s; font-size: 0.85em; font-weight: bold;'>[%s]</span>" +
            "<br><span style='color: gray; font-size: 0.8em;'>%s</span>" +
            "<br><span style='color: #0066CC; cursor: pointer;'><u><a href='%d'>Get Email</a></u></span>" +
            "<br><p style='margin: 5px 0;'><b>Comments:</b> %s</p>" +
            "</div>",
            review.getReviewId(),
            reviewer.getName(),
            reviewer.getUserType(),
            reportBadge,
            buildRatingDisplay(review.getRating()),
            statusColor,
            review.getStatus(),
            DATE_FORMAT.format(review.getCreatedAt()),
            review.getReviewId(),
            review.getComments() != null ? escapeHtml(review.getComments()) : "<em>No comments</em>"
        );
    }
    
    /**
     * Adapts Review to summary format for list displays
     * Used in course listing and quick preview displays
     */
    @Override
    public String adaptForSummary(Review review, User reviewer) {
        if (review == null || reviewer == null) {
            return "[Invalid Review]";
        }
        
        String commentsPreview = review.getComments() != null && !review.getComments().isEmpty()
            ? review.getComments().substring(0, Math.min(50, review.getComments().length())) + "..."
            : "No comments";
        
        return String.format(
            "%s (%s) - %s - %s",
            reviewer.getName(),
            reviewer.getUserType(),
            buildRatingDisplay(review.getRating()),
            commentsPreview
        );
    }
    
    /**
     * Helper method: Build visual rating display with stars
     */
    private String buildRatingDisplay(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stars.append(i < rating ? RATING_STAR : EMPTY_STAR);
        }
        return stars.append(" (").append(rating).append("/5)").toString();
    }
    
    /**
     * Helper method: Determine status color for HTML display
     */
    private String getStatusColor(String status) {
        if ("APPROVED".equals(status)) {
            return "#008000"; // Green
        } else if ("REJECTED".equals(status)) {
            return "#FF0000"; // Red
        } else {
            return "#FFA500"; // Orange (PENDING)
        }
    }
    
    /**
     * Helper method: Build report count badge for display
     */
    private String buildReportBadge(int reportCount) {
        if (reportCount > 0) {
            String badgeColor = reportCount >= 5 ? "#FF0000" : "#FFA500";
            return String.format(" <span style='background-color: %s; color: white; padding: 2px 5px; border-radius: 3px; font-size: 0.75em;'>%d reports</span>", 
                badgeColor, reportCount);
        }
        return "";
    }
    
    /**
     * Helper method: Escape HTML special characters
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}
