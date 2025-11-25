package electiview.gui;

import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;
import electiview.gui.components.RoundedButton;
import electiview.gui.components.RoundedTextField;
import electiview.gui.components.RoundedPasswordField;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField rollNumberField;
    private JTextField arnField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> userTypeCombo;
    private JButton registerButton;
    private JButton backButton;
    private ElectiviewFacade facade;
    
    public RegisterFrame() {
        facade = ElectiviewFacade.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Register New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(new Color(0, 100, 180));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        int row = 0;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel rollLabel = new JLabel("Roll Number:");
        rollLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(rollLabel, gbc);
        rollNumberField = new RoundedTextField(25, 12, new Color(200, 220, 240));
        rollNumberField.setFont(new Font("Arial", Font.PLAIN, 12));
        rollNumberField.setBackground(new Color(250, 252, 255));
        rollNumberField.setPreferredSize(new java.awt.Dimension(240, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(rollNumberField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel arnLabel = new JLabel("ARN Number:");
        arnLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(arnLabel, gbc);
        arnField = new RoundedTextField(25, 12, new Color(200, 220, 240));
        arnField.setFont(new Font("Arial", Font.PLAIN, 12));
        arnField.setBackground(new Color(250, 252, 255));
        arnField.setPreferredSize(new java.awt.Dimension(240, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(arnField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);
        nameField = new RoundedTextField(25, 12, new Color(200, 220, 240));
        nameField.setFont(new Font("Arial", Font.PLAIN, 12));
        nameField.setBackground(new Color(250, 252, 255));
        nameField.setPreferredSize(new java.awt.Dimension(240, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(nameField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLabel, gbc);
        emailField = new RoundedTextField(25, 12, new Color(200, 220, 240));
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        emailField.setBackground(new Color(250, 252, 255));
        emailField.setPreferredSize(new java.awt.Dimension(240, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(emailField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passLabel, gbc);
        passwordField = new RoundedPasswordField(25, 12, new Color(200, 220, 240));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBackground(new Color(250, 252, 255));
        passwordField.setPreferredSize(new java.awt.Dimension(240, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(passwordField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(confirmLabel, gbc);
        confirmPasswordField = new RoundedPasswordField(25, 12, new Color(200, 220, 240));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        confirmPasswordField.setBackground(new Color(250, 252, 255));
        confirmPasswordField.setPreferredSize(new java.awt.Dimension(240, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        formPanel.add(confirmPasswordField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel typeLabel = new JLabel("User Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        formPanel.add(typeLabel, gbc);
        userTypeCombo = new JComboBox<>(new String[]{"STUDENT", "ALUMNI"});
        userTypeCombo.setFont(new Font("Arial", Font.PLAIN, 11));
        userTypeCombo.setBackground(new Color(250, 252, 255));
        gbc.gridx = 1;
        formPanel.add(userTypeCombo, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(240, 245, 250));
        
        registerButton = new RoundedButton("Register", 20);
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new java.awt.Dimension(130, 35));
        registerButton.addActionListener(e -> handleRegister());
        buttonPanel.add(registerButton);
        
        backButton = new RoundedButton("Back to Login", 20);
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new java.awt.Dimension(130, 35));
        backButton.addActionListener(e -> handleBack());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handleRegister() {
        String rollNumber = rollNumberField.getText().trim();
        String arn = arnField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();
        
        if (rollNumber.isEmpty() || arn.isEmpty() || name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if user is already registered
        if (facade.isUserAlreadyRegistered(rollNumber)) {
            JOptionPane.showMessageDialog(this, "User already registered", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = facade.registerUser(rollNumber, arn, password, name, email, userType);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            handleBack();
        } else {
            // Check if roll number exists in mapping
            if (facade.getRollARNMapping(rollNumber) == null) {
                JOptionPane.showMessageDialog(this, 
                    "Registration failed for Roll Number: " + rollNumber + "\nRoll number does not exist!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Check if the roll number is locked
            else if (facade.isRollNumberLocked(rollNumber)) {
                JOptionPane.showMessageDialog(this, 
                    "Registration failed for Roll Number: " + rollNumber + "\nRoll number is locked due to too many failed attempts.\nPlease contact administrator.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Display remaining attempts for this roll number
                int remainingAttempts = facade.getRemainingRegistrationAttempts(rollNumber);
                String message;
                if (remainingAttempts > 0) {
                    message = "Registration failed for Roll Number: " + rollNumber + "\nInvalid ARN number.\nRemaining attempts: " + remainingAttempts;
                } else {
                    message = "Registration failed for Roll Number: " + rollNumber + "\nNo attempts left. Please contact administrator.";
                }
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleBack() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}
