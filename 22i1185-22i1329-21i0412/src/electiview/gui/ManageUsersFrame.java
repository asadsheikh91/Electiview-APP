package electiview.gui;

import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;
import electiview.gui.components.ColoredStatusRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageUsersFrame extends JFrame {
    private User currentUser;
    private ElectiviewFacade facade;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton viewButton;
    private JButton deactivateButton;
    private JButton activateButton;
    private JButton refreshButton;
    private JButton backButton;
    
    public ManageUsersFrame(User user) {
        this.currentUser = user;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
        loadUsers();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Manage Users");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel userLogoPanel = new JPanel(new BorderLayout());
        JLabel userLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            userLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            userLogo.setText("[Logo]");
        }
        userLogoPanel.add(userLogo, BorderLayout.EAST);
        JLabel titleLabel = new JLabel("Manage Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        userLogoPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(userLogoPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Roll Number", "Name", "Email", "User Type", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);
        userTable.getColumnModel().getColumn(5).setCellRenderer(new ColoredStatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> handleView());
        buttonPanel.add(viewButton);
        
        deactivateButton = new JButton("Deactivate User");
        deactivateButton.addActionListener(e -> handleDeactivate());
        buttonPanel.add(deactivateButton);
        
        activateButton = new JButton("Activate User");
        activateButton.addActionListener(e -> handleActivate());
        buttonPanel.add(activateButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadUsers());
        buttonPanel.add(refreshButton);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = facade.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getRollNumber(),
                user.getName(),
                user.getEmail(),
                user.getUserType(),
                user.isActive() ? "Active" : "Inactive"
            });
        }
    }
    
    private void handleView() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String rollNumber = (String) tableModel.getValueAt(selectedRow, 1);
        String name = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);
        String userType = (String) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        
        StringBuilder details = new StringBuilder();
        details.append("User ID: ").append(userId).append("\n");
        details.append("Roll Number: ").append(rollNumber).append("\n");
        details.append("Name: ").append(name).append("\n");
        details.append("Email: ").append(email != null ? email : "N/A").append("\n");
        details.append("User Type: ").append(userType).append("\n");
        details.append("Status: ").append(status).append("\n");
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "User Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleDeactivate() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String userType = (String) tableModel.getValueAt(selectedRow, 4);
        
        // Check if trying to deactivate own admin account
        if (userId == currentUser.getUserId() && "ADMIN".equals(userType)) {
            JOptionPane.showMessageDialog(this, "Admin cannot be deactivated", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to deactivate this user?", "Confirm Deactivate", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to deactivate user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleActivate() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        
        // Check if user is already active
        if ("Active".equals(status)) {
            JOptionPane.showMessageDialog(this, "This user is already active", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to activate this user?", "Confirm Activate", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.activateUser(userId)) {
                JOptionPane.showMessageDialog(this, "User activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to activate user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
