package electiview.gui;

import electiview.models.Course;
import electiview.models.Review;
import electiview.models.User;
import electiview.models.CourseStatistics;
import electiview.patterns.facade.ElectiviewFacade;
import electiview.patterns.adapter.ReviewDisplayAdapter;
import electiview.patterns.adapter.ReviewDisplayAdapterImpl;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseDetailsFrame extends JFrame {
    private User currentUser;
    private Course course;
    private ElectiviewFacade facade;
    private ReviewDisplayAdapter reviewDisplayAdapter;
    private JEditorPane detailsArea;
    private JEditorPane ratingStatsArea;
    private JEditorPane academicReviewsArea;
    private JEditorPane postAcademicReviewsArea;
    private JButton submitReviewButton;
    private JButton reportReviewButton;
    private JButton backButton;
    private JList<String> reviewList;
    private HashMap<Integer, String> reviewUserEmails;
    
    public CourseDetailsFrame(User user, Course course) {
        this.currentUser = user;
        this.course = course;
        this.facade = ElectiviewFacade.getInstance();
        this.reviewDisplayAdapter = new ReviewDisplayAdapterImpl();
        initComponents();
        loadCourseDetails();
    }
    
    private void initComponents() {
        setTitle("Course Details - " + course.getCourseCode());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topLogoPanel = new JPanel(new BorderLayout());
        JLabel topLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            topLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            topLogo.setText("[Logo]");
        }
        topLogoPanel.add(topLogo, BorderLayout.EAST);
        JLabel titleLabel = new JLabel(course.getCourseTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        topLogoPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topLogoPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JPanel topSection = new JPanel(new GridLayout(1, 2, 15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                g2d.setColor(Color.GRAY);
                int centerX = width / 2;
                for (int y = 10; y < height - 10; y += 5) {
                    g2d.drawLine(centerX, y, centerX, y + 2);
                }
            }
        };
        topSection.setLayout(new GridLayout(1, 2, 15, 10));
        
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Course Information"));
        detailsArea = new JEditorPane();
        detailsArea.setContentType("text/html");
        detailsArea.setEditable(false);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);
        topSection.add(detailsPanel);
        
        JPanel ratingStatsPanel = new JPanel(new BorderLayout());
        ratingStatsPanel.setBorder(BorderFactory.createTitledBorder("Review Ratings"));
        ratingStatsArea = new JEditorPane();
        ratingStatsArea.setContentType("text/html");
        ratingStatsArea.setEditable(false);
        JScrollPane ratingStatsScroll = new JScrollPane(ratingStatsArea);
        ratingStatsPanel.add(ratingStatsScroll, BorderLayout.CENTER);
        topSection.add(ratingStatsPanel);
        
        contentPanel.add(topSection);
        
        JPanel reviewsPanel = new JPanel(new BorderLayout());
        reviewsPanel.setBorder(BorderFactory.createTitledBorder("Reviews"));
        
        JPanel reviewColumnsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JPanel academicPanel = new JPanel(new BorderLayout());
        academicPanel.setBorder(BorderFactory.createTitledBorder("ACADEMIC (Students)"));
        academicReviewsArea = new JEditorPane();
        academicReviewsArea.setContentType("text/html");
        academicReviewsArea.setEditable(false);
        academicReviewsArea.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                String email = reviewUserEmails.get(Integer.parseInt(e.getDescription()));
                if (email != null) {
                    JOptionPane.showMessageDialog(this, "Email: " + email, "User Email", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JScrollPane academicScroll = new JScrollPane(academicReviewsArea);
        academicPanel.add(academicScroll, BorderLayout.CENTER);
        reviewColumnsPanel.add(academicPanel);
        
        JPanel postAcademicPanel = new JPanel(new BorderLayout());
        postAcademicPanel.setBorder(BorderFactory.createTitledBorder("POST-ACADEMIC (Alumni)"));
        postAcademicReviewsArea = new JEditorPane();
        postAcademicReviewsArea.setContentType("text/html");
        postAcademicReviewsArea.setEditable(false);
        postAcademicReviewsArea.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                String email = reviewUserEmails.get(Integer.parseInt(e.getDescription()));
                if (email != null) {
                    JOptionPane.showMessageDialog(this, "Email: " + email, "User Email", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JScrollPane postAcademicScroll = new JScrollPane(postAcademicReviewsArea);
        postAcademicPanel.add(postAcademicScroll, BorderLayout.CENTER);
        reviewColumnsPanel.add(postAcademicPanel);
        
        reviewsPanel.add(reviewColumnsPanel, BorderLayout.CENTER);
        
        JPanel reviewButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        reportReviewButton = new JButton("Report Selected Review");
        reportReviewButton.addActionListener(e -> handleReportReview());
        reviewButtonPanel.add(reportReviewButton);
        reviewsPanel.add(reviewButtonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(reviewsPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        submitReviewButton = new JButton("Submit Review");
        submitReviewButton.addActionListener(e -> handleSubmitReview());
        buttonPanel.add(submitReviewButton);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCourseDetails() {
        reviewUserEmails = new HashMap<>();
        
        StringBuilder details = new StringBuilder();
        details.append("<html><body style='font-family: Arial; font-size: 11px;'>");
        details.append("Course Code: ").append(course.getCourseCode()).append("<br>");
        details.append("Course Title: ").append(course.getCourseTitle()).append("<br>");
        details.append("Credits: ").append(course.getCredits()).append("<br>");
        details.append("Syllabus: ").append(course.getSyllabus() != null ? course.getSyllabus() : "N/A").append("<br>");
        
        String statusText = course.isActive() ? "ACTIVE" : "NOT ACTIVE";
        String statusColor = course.isActive() ? "#008000" : "#FF0000";
        details.append("Status: <span style='color: ").append(statusColor).append("; font-weight: bold;'>").append(statusText).append("</span><br>");
        
        details.append("</body></html>");
        detailsArea.setText(details.toString());
        
        int academicReviews = facade.getAcademicReviewCount(course.getCourseId());
        int postAcademicReviews = facade.getPostAcademicReviewCount(course.getCourseId());
        int totalReviews = academicReviews + postAcademicReviews;
        
        double academicAvgRating = facade.getAcademicAverageRating(course.getCourseId());
        double postAcademicAvgRating = facade.getPostAcademicAverageRating(course.getCourseId());
        CourseStatistics stats = facade.getCourseStatistics(course.getCourseId());
        double totalAvgRating = (stats != null) ? stats.getAverageRating() : 0.0;
        
        StringBuilder ratingStats = new StringBuilder();
        ratingStats.append("<html><body style='font-family: Arial; font-size: 11px;'>");
        ratingStats.append("<b>ACADEMIC (Students)</b><br>");
        if (academicReviews > 0) {
            ratingStats.append("Avg Rating: ").append(String.format("%.2f", academicAvgRating)).append(" / 5<br>");
            ratingStats.append("Reviews: ").append(academicReviews).append("<br>");
        } else {
            ratingStats.append("Reviews: 0<br>");
        }
        
        ratingStats.append("<br><b>POST-ACADEMIC (Alumni)</b><br>");
        if (postAcademicReviews > 0) {
            ratingStats.append("Avg Rating: ").append(String.format("%.2f", postAcademicAvgRating)).append(" / 5<br>");
            ratingStats.append("Reviews: ").append(postAcademicReviews).append("<br>");
        } else {
            ratingStats.append("Reviews: 0<br>");
        }
        
        ratingStats.append("<br><b>TOTAL</b><br>");
        if (totalReviews > 0) {
            ratingStats.append("Total Avg Rating: ").append(String.format("%.2f", totalAvgRating)).append(" / 5<br>");
            ratingStats.append("Total Reviews: ").append(totalReviews).append("<br>");
        } else {
            ratingStats.append("Total Reviews: 0<br>");
        }
        
        ratingStats.append("</body></html>");
        ratingStatsArea.setText(ratingStats.toString());
        
        List<Review> reviews = facade.getCourseReviews(course.getCourseId());
        List<Review> studentReviews = new ArrayList<>();
        List<Review> alumniReviews = new ArrayList<>();
        
        for (Review review : reviews) {
            if (review.getReportCount() >= 5) {
                continue;
            }
            User reviewer = facade.getUserById(review.getUserId());
            if (reviewer != null && "ALUMNI".equals(reviewer.getUserType())) {
                alumniReviews.add(review);
            } else {
                studentReviews.add(review);
            }
        }
        
        StringBuilder academicText = new StringBuilder();
        academicText.append("<html><body style='font-family: Arial; font-size: 11px;'>");
        if (studentReviews.isEmpty()) {
            academicText.append("No student reviews available.");
        } else {
            for (Review review : studentReviews) {
                User reviewer = facade.getUserById(review.getUserId());
                if (reviewer != null) {
                    reviewUserEmails.put(review.getReviewId(), reviewer.getEmail());
                    academicText.append(reviewDisplayAdapter.adaptForHtmlDisplay(review, reviewer));
                }
            }
        }
        academicText.append("</body></html>");
        academicReviewsArea.setText(academicText.toString());
        
        StringBuilder postAcademicText = new StringBuilder();
        postAcademicText.append("<html><body style='font-family: Arial; font-size: 11px;'>");
        if (alumniReviews.isEmpty()) {
            postAcademicText.append("No alumni reviews available.");
        } else {
            for (Review review : alumniReviews) {
                User reviewer = facade.getUserById(review.getUserId());
                if (reviewer != null) {
                    reviewUserEmails.put(review.getReviewId(), reviewer.getEmail());
                    postAcademicText.append(reviewDisplayAdapter.adaptForHtmlDisplay(review, reviewer));
                }
            }
        }
        postAcademicText.append("</body></html>");
        postAcademicReviewsArea.setText(postAcademicText.toString());
    }
    
    private void handleReportReview() {
        String input = JOptionPane.showInputDialog(this, "Enter Review ID to report:", "");
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        
        try {
            int reviewId = Integer.parseInt(input.trim());
            
            // Check if review exists in database
            if (!facade.reviewExists(reviewId)) {
                JOptionPane.showMessageDialog(this, "Review ID not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if user has already reported this review
            if (facade.hasUserReportedReview(reviewId, currentUser.getUserId())) {
                JOptionPane.showMessageDialog(this, "You have already reported this review", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Show reason selection dialog
            String[] reasons = {"Spam", "Offensive", "Irrelevant", "Inappropriate Language", "Misinformation", "Other"};
            String selectedReason = (String) JOptionPane.showInputDialog(this, 
                "Select reason for reporting:", 
                "Report Review Reason", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                reasons, 
                reasons[0]);
            
            if (selectedReason == null) {
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Report this review as " + selectedReason.toLowerCase() + "?", "Confirm Report", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (facade.reportReview(reviewId, currentUser.getUserId(), selectedReason)) {
                    JOptionPane.showMessageDialog(this, "Review reported successfully! Admin will review it.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshReviews();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to report review", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Review ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleSubmitReview() {
        new SubmitReviewFrame(currentUser, course, this).setVisible(true);
    }
    
    public void refreshReviews() {
        loadCourseDetails();
    }
}
