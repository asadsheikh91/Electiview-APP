package electiview.gui;

import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;
import electiview.gui.components.RoundedButton;
import electiview.gui.components.RoundedTextField;
import electiview.gui.components.RoundedPasswordField;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField rollNumberField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private ElectiviewFacade facade;
    
    public LoginFrame() {
        facade = ElectiviewFacade.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Logo Panel - Centered layout with image and title below
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setBackground(new Color(240, 245, 250));
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("[Logo]");
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(15));
        
        JLabel titleLabel = new JLabel("ELECTIVIEW");
        titleLabel.setFont(new Font("Geogrotesque", Font.BOLD, 38));
        titleLabel.setForeground(new Color(0, 100, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Elective Course Review System");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(100, 120, 140));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(subtitleLabel);
        logoPanel.add(Box.createVerticalStrut(15));
        
        mainPanel.add(logoPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 220, 240), 2, true));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel rollLabel = new JLabel("Roll Number:");
        rollLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(rollLabel, gbc);
        
        rollNumberField = new RoundedTextField(30, 15, new Color(200, 220, 240));
        rollNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        rollNumberField.setBackground(new Color(250, 252, 255));
        rollNumberField.setPreferredSize(new java.awt.Dimension(280, 40));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(rollNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formPanel.add(passLabel, gbc);
        
        passwordField = new RoundedPasswordField(30, 15, new Color(200, 220, 240));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(250, 252, 255));
        passwordField.setPreferredSize(new java.awt.Dimension(280, 40));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(passwordField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(240, 245, 250));
        
        loginButton = new RoundedButton("Login", 20);
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setBackground(new Color(0, 150, 136));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new java.awt.Dimension(120, 35));
        loginButton.addActionListener(e -> handleLogin());
        buttonPanel.add(loginButton);
        
        registerButton = new RoundedButton("Register", 20);
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setBackground(new Color(100, 181, 246));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new java.awt.Dimension(120, 35));
        registerButton.addActionListener(e -> handleRegister());
        buttonPanel.add(registerButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void handleLogin() {
        String rollNumber = rollNumberField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (rollNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both roll number and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = facade.loginUser(rollNumber, password);
        
        if (user != null) {
            this.dispose();
            if (user.getUserType().equals("ADMIN")) {
                new AdminDashboardFrame(user).setVisible(true);
            } else {
                new DashboardFrame(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid roll number or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRegister() {
        this.dispose();
        new RegisterFrame().setVisible(true);
    }
}
