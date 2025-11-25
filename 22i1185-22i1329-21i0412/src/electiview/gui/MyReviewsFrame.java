package electiview.gui;

import electiview.models.Course;
import electiview.models.Review;
import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyReviewsFrame extends JFrame {
    private User currentUser;
    private ElectiviewFacade facade;
    private JTable reviewTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton backButton;
    
    public MyReviewsFrame(User user) {
        this.currentUser = user;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
        loadReviews();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - My Reviews");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Reviews", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Review ID", "Course Code", "Course Title", "Rating", "Comments", "Status", "Date"};
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
        List<Review> reviews = facade.getUserReviews(currentUser.getUserId());
        
        if (reviews.isEmpty()) {
            tableModel.addRow(new Object[]{"", "", "No reviews yet", "", "", "", ""});
        } else {
            for (Review review : reviews) {
                Course course = facade.getCourseDetails(review.getCourseId());
                String courseCode = course != null ? course.getCourseCode() : "N/A";
                String courseTitle = course != null ? course.getCourseTitle() : "N/A";
                
                tableModel.addRow(new Object[]{
                    review.getReviewId(),
                    courseCode,
                    courseTitle,
                    review.getRating() + " / 5",
                    review.getComments() != null && !review.getComments().isEmpty() ? 
                        (review.getComments().length() > 50 ? review.getComments().substring(0, 50) + "..." : review.getComments()) : 
                        "No comments",
                    review.getStatus(),
                    review.getCreatedAt()
                });
            }
        }
    }
}
