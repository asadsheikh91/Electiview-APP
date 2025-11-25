package electiview.gui;

import electiview.models.RollARNMapping;
import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageRollARNFrame extends JFrame {
    private User currentUser;
    private ElectiviewFacade facade;
    private JTable mappingTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton unlockButton;
    private JButton refreshButton;
    private JButton backButton;
    
    public ManageRollARNFrame(User user) {
        this.currentUser = user;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
        loadMappings();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Manage Roll-ARN Mappings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Manage Roll-ARN Mappings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Roll Number", "ARN Number", "Used", "Attempts", "Locked"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mappingTable = new JTable(tableModel);
        mappingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mappingTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(mappingTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add Mapping");
        addButton.addActionListener(e -> handleAdd());
        buttonPanel.add(addButton);
        
        unlockButton = new JButton("Unlock Roll");
        unlockButton.addActionListener(e -> handleUnlock());
        buttonPanel.add(unlockButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadMappings());
        buttonPanel.add(refreshButton);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadMappings() {
        tableModel.setRowCount(0);
        List<RollARNMapping> mappings = facade.getAllRollARNMappings();
        
        for (RollARNMapping mapping : mappings) {
            tableModel.addRow(new Object[]{
                mapping.getMappingId(),
                mapping.getRollNumber(),
                mapping.getArnNumber(),
                mapping.isUsed() ? "Yes" : "No",
                mapping.getAttemptCount(),
                mapping.isLocked() ? "Yes" : "No"
            });
        }
    }
    
    private void handleAdd() {
        JTextField rollField = new JTextField();
        JTextField arnField = new JTextField();
        
        Object[] message = {
            "Roll Number:", rollField,
            "ARN Number:", arnField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Add New Roll-ARN Mapping", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String rollNumber = rollField.getText().trim();
            String arnNumber = arnField.getText().trim();
            
            if (rollNumber.isEmpty() || arnNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if roll number already exists
            if (facade.getRollARNMapping(rollNumber) != null) {
                JOptionPane.showMessageDialog(this, "Roll number-ARN pair already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (facade.addRollARNMapping(rollNumber, arnNumber)) {
                JOptionPane.showMessageDialog(this, "Mapping added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMappings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add mapping", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleUnlock() {
        int selectedRow = mappingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a roll number to unlock", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String rollNumber = (String) tableModel.getValueAt(selectedRow, 1);
        int option = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to unlock roll number: " + rollNumber + "?", 
            "Unlock Roll Number", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (facade.unlockRollNumber(rollNumber)) {
                JOptionPane.showMessageDialog(this, "Roll number unlocked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMappings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to unlock roll number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
