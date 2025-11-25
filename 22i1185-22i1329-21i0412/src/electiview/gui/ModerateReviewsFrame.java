package electiview.gui;

import electiview.models.Review;
import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ModerateReviewsFrame extends JFrame {
    private User currentUser;
    private ElectiviewFacade facade;
    private JTable reviewTable;
    private DefaultTableModel tableModel;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backButton;
    
    public ModerateReviewsFrame(User user) {
        this.currentUser = user;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
        loadReviews();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Moderate Reviews");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel modLogoPanel = new JPanel(new BorderLayout());
        JLabel modLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            modLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            modLogo.setText("[Logo]");
        }
        modLogoPanel.add(modLogo, BorderLayout.EAST);
        JLabel titleLabel = new JLabel("Moderate Reviews (Reported)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        modLogoPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(modLogoPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "User ID", "Course ID", "Rating", "Reports", "Status", "Comments"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reviewTable = new JTable(tableModel);
        reviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reviewTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(reviewTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        approveButton = new JButton("Approve");
        approveButton.addActionListener(e -> handleApprove());
        buttonPanel.add(approveButton);
        
        rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e -> handleReject());
        buttonPanel.add(rejectButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDelete());
        buttonPanel.add(deleteButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReviews());
        buttonPanel.add(refreshButton);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadReviews() {
        tableModel.setRowCount(0);
        List<Review> reviews = facade.getReportedReviews();
        for (Review review : reviews) {
            String comments = review.getComments();
            if (comments != null && comments.length() > 50) {
                comments = comments.substring(0, 47) + "...";
            }
            tableModel.addRow(new Object[]{
                review.getReviewId(),
                review.getUserId(),
                review.getCourseId(),
                review.getRating(),
                review.getReportCount(),
                review.getStatus(),
                comments != null ? comments : "No comments"
            });
        }
    }
    
    private void handleApprove() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a review", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int reviewId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Approve this review?", "Confirm Approve", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.approveReview(reviewId)) {
                JOptionPane.showMessageDialog(this, "Review approved! It will be removed from moderation panel.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadReviews();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to approve review", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleReject() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a review", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int reviewId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Reject this review?", "Confirm Reject", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.rejectReview(reviewId)) {
                JOptionPane.showMessageDialog(this, "Review rejected! Status changed to REJECTED and will remain in moderation panel.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadReviews();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reject review", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleDelete() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a review", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int reviewId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this review?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.deleteReview(reviewId)) {
                JOptionPane.showMessageDialog(this, "Review deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadReviews();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete review", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
