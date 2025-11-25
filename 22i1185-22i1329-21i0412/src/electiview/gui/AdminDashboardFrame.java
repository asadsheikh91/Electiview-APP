package electiview.gui;

import electiview.models.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {
    private User currentUser;
    private JButton manageCoursesButton;
    private JButton manageUsersButton;
    private JButton moderateReviewsButton;
    private JButton manageRollARNButton;
    private JButton logoutButton;
    
    public AdminDashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Admin Dashboard");
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
        JLabel titleLabel = new JLabel("ELECTIVIEW - Admin Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        manageCoursesButton = new JButton("Manage Courses");
        manageCoursesButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        manageCoursesButton.addActionListener(e -> handleManageCourses());
        buttonPanel.add(manageCoursesButton);
        
        manageUsersButton = new JButton("Manage Users");
        manageUsersButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        manageUsersButton.addActionListener(e -> handleManageUsers());
        buttonPanel.add(manageUsersButton);
        
        moderateReviewsButton = new JButton("Moderate Reviews");
        moderateReviewsButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        moderateReviewsButton.addActionListener(e -> handleModerateReviews());
        buttonPanel.add(moderateReviewsButton);
        
        manageRollARNButton = new JButton("Manage Roll-ARN Mappings");
        manageRollARNButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        manageRollARNButton.addActionListener(e -> handleManageRollARN());
        buttonPanel.add(manageRollARNButton);
        
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        logoutButton.addActionListener(e -> handleLogout());
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void handleManageCourses() {
        new ManageCoursesFrame(currentUser).setVisible(true);
    }
    
    private void handleManageUsers() {
        new ManageUsersFrame(currentUser).setVisible(true);
    }
    
    private void handleModerateReviews() {
        new ModerateReviewsFrame(currentUser).setVisible(true);
    }
    
    private void handleManageRollARN() {
        new ManageRollARNFrame(currentUser).setVisible(true);
    }
    
    private void handleLogout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}
