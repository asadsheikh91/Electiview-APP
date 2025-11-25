package electiview.gui;

import electiview.models.Course;
import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;

import javax.swing.*;
import java.awt.*;

public class SubmitReviewFrame extends JFrame {
    private User currentUser;
    private Course course;
    private CourseDetailsFrame parentFrame;
    private ElectiviewFacade facade;
    private JSlider ratingSlider;
    private JTextArea commentsArea;
    private JButton submitButton;
    private JButton cancelButton;
    
    public SubmitReviewFrame(User user, Course course, CourseDetailsFrame parent) {
        this.currentUser = user;
        this.course = course;
        this.parentFrame = parent;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Submit Review - " + course.getCourseCode());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Submit Review for " + course.getCourseTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel ratingPanel = new JPanel(new BorderLayout(10, 10));
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Rating"));
        JLabel ratingLabel = new JLabel("Rate from 1 (Poor) to 5 (Excellent)", SwingConstants.CENTER);
        ratingPanel.add(ratingLabel, BorderLayout.NORTH);
        
        ratingSlider = new JSlider(1, 5, 3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingPanel.add(ratingSlider, BorderLayout.CENTER);
        
        formPanel.add(ratingPanel, BorderLayout.NORTH);
        
        JPanel commentsPanel = new JPanel(new BorderLayout());
        commentsPanel.setBorder(BorderFactory.createTitledBorder("Comments (Optional)"));
        commentsArea = new JTextArea(8, 40);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        commentsPanel.add(scrollPane, BorderLayout.CENTER);
        
        formPanel.add(commentsPanel, BorderLayout.CENTER);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        submitButton = new JButton("Submit Review");
        submitButton.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handleSubmit() {
        int rating = ratingSlider.getValue();
        String comments = commentsArea.getText().trim();
        
        if (facade.submitReview(currentUser.getUserId(), course.getCourseId(), rating, comments)) {
            JOptionPane.showMessageDialog(this, "Review submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.refreshReviews();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit review. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
