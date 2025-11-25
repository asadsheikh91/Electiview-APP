package electiview.gui;

import electiview.models.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JButton viewCoursesButton;
    private JButton myReviewsButton;
    private JButton logoutButton;
    
    public DashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add logo in top-right corner
        JPanel cornerLogoPanel = new JPanel(new BorderLayout());
        JLabel cornerLogoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            cornerLogoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            cornerLogoLabel.setText("[Logo]");
        }
        cornerLogoPanel.add(cornerLogoLabel, BorderLayout.EAST);
        mainPanel.add(cornerLogoPanel, BorderLayout.NORTH);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("ELECTIVIEW - Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getUserType() + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        viewCoursesButton = new JButton("Browse & Review Courses");
        viewCoursesButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        viewCoursesButton.addActionListener(e -> handleViewCourses());
        buttonPanel.add(viewCoursesButton);
        
        myReviewsButton = new JButton("My Reviews");
        myReviewsButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        myReviewsButton.addActionListener(e -> handleMyReviews());
        buttonPanel.add(myReviewsButton);
        
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        logoutButton.addActionListener(e -> handleLogout());
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void handleViewCourses() {
        new CourseListFrame(currentUser).setVisible(true);
    }
    
    private void handleMyReviews() {
        new MyReviewsFrame(currentUser).setVisible(true);
    }
    
    private void handleLogout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}
