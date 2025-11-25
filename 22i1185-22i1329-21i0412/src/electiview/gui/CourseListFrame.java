package electiview.gui;

import electiview.models.Course;
import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class CourseListFrame extends JFrame {
    private User currentUser;
    private ElectiviewFacade facade;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailsButton;
    private JButton compareButton;
    private JButton backButton;
    
    public CourseListFrame(User user) {
        this.currentUser = user;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
        loadCourses();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Browse Courses");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("[Logo]");
        }
        topPanel.add(logoLabel, BorderLayout.EAST);
        JLabel titleLabel = new JLabel("Available Elective Courses", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"Course Code", "Course Title", "Credits"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(courseTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        viewDetailsButton = new JButton("View Details & Reviews");
        viewDetailsButton.addActionListener(e -> handleViewDetails());
        buttonPanel.add(viewDetailsButton);
        
        compareButton = new JButton("Compare Courses");
        compareButton.addActionListener(e -> handleCompare());
        buttonPanel.add(compareButton);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courses = facade.getAllCourses();
        for (Course course : courses) {
            tableModel.addRow(new Object[]{
                course.getCourseCode(),
                course.getCourseTitle(),
                course.getCredits()
            });
        }
    }
    
    private void handleViewDetails() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
        List<Course> courses = facade.getAllCourses();
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                new CourseDetailsFrame(currentUser, course).setVisible(true);
                break;
            }
        }
    }
    
    private void handleCompare() {
        List<Course> courses = facade.getAllCourses();
        if (courses.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 courses to compare", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JCheckBox[] checkboxes = new JCheckBox[courses.size()];
        
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            checkboxes[i] = new JCheckBox(course.getCourseCode() + " - " + course.getCourseTitle());
            panel.add(checkboxes[i]);
        }
        
        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(panel), 
            "Select Courses to Compare (max 3)", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            java.util.ArrayList<Course> selectedCourses = new java.util.ArrayList<>();
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    selectedCourses.add(courses.get(i));
                }
            }
            
            if (selectedCourses.size() < 2) {
                JOptionPane.showMessageDialog(this, "Please select at least 2 courses", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (selectedCourses.size() > 3) {
                JOptionPane.showMessageDialog(this, "Please select maximum 3 courses", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                showComparison(selectedCourses);
            }
        }
    }
    
    private void showComparison(java.util.ArrayList<Course> courses) {
        String[] columnNames = new String[courses.size() + 1];
        columnNames[0] = "Attribute";
        for (int i = 0; i < courses.size(); i++) {
            columnNames[i + 1] = courses.get(i).getCourseCode();
        }
        
        Object[][] data = new Object[10][courses.size() + 1];
        
        // Row 0: Course Title
        data[0][0] = "COURSE TITLE";
        for (int i = 0; i < courses.size(); i++) {
            data[0][i + 1] = courses.get(i).getCourseTitle();
        }
        
        // Row 1: Credits
        data[1][0] = "CREDITS";
        for (int i = 0; i < courses.size(); i++) {
            data[1][i + 1] = courses.get(i).getCredits();
        }
        
        // Row 2: Academic Avg Rating
        data[2][0] = "AVG RATING (Academic)";
        for (int i = 0; i < courses.size(); i++) {
            double academicAvg = facade.getAcademicAverageRating(courses.get(i).getCourseId());
            data[2][i + 1] = (academicAvg > 0) ? String.format("%.2f / 5", academicAvg) : "No reviews";
        }
        
        // Row 3: Academic Reviews
        data[3][0] = "REVIEWS (Academic)";
        for (int i = 0; i < courses.size(); i++) {
            data[3][i + 1] = facade.getAcademicReviewCount(courses.get(i).getCourseId());
        }
        
        // Row 4: Post-Academic Avg Rating
        data[4][0] = "AVG RATING (Post-Academic)";
        for (int i = 0; i < courses.size(); i++) {
            double postAcademicAvg = facade.getPostAcademicAverageRating(courses.get(i).getCourseId());
            data[4][i + 1] = (postAcademicAvg > 0) ? String.format("%.2f / 5", postAcademicAvg) : "No reviews";
        }
        
        // Row 5: Post-Academic Reviews
        data[5][0] = "REVIEWS (Post-Academic)";
        for (int i = 0; i < courses.size(); i++) {
            data[5][i + 1] = facade.getPostAcademicReviewCount(courses.get(i).getCourseId());
        }
        
        // Row 6: Total Avg Rating
        data[6][0] = "TOTAL AVG RATING";
        for (int i = 0; i < courses.size(); i++) {
            electiview.models.CourseStatistics stats = facade.getCourseStatistics(courses.get(i).getCourseId());
            data[6][i + 1] = (stats != null && stats.getReviewCount() > 0) ? 
                String.format("%.2f / 5", stats.getAverageRating()) : "No reviews";
        }
        
        // Row 7: Total Reviews
        data[7][0] = "TOTAL REVIEWS";
        for (int i = 0; i < courses.size(); i++) {
            electiview.models.CourseStatistics stats = facade.getCourseStatistics(courses.get(i).getCourseId());
            data[7][i + 1] = (stats != null) ? stats.getReviewCount() : 0;
        }
        
        // Row 8: Syllabus
        data[8][0] = "SYLLABUS";
        for (int i = 0; i < courses.size(); i++) {
            String syllabus = courses.get(i).getSyllabus() != null && !courses.get(i).getSyllabus().isEmpty() ? 
                (courses.get(i).getSyllabus().length() > 40 ? courses.get(i).getSyllabus().substring(0, 40) + "..." : courses.get(i).getSyllabus()) : 
                "N/A";
            data[8][i + 1] = syllabus;
        }
        
        // Row 9: Status Info
        data[9][0] = "INFO";
        for (int i = 0; i < courses.size(); i++) {
            data[9][i + 1] = courses.get(i).isActive() ? "Active" : "NOT ACTIVE";
        }
        
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable comparisonTable = new JTable(tableModel);
        comparisonTable.setRowHeight(40);
        comparisonTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        comparisonTable.getTableHeader().setBackground(new Color(0, 102, 102));
        comparisonTable.getTableHeader().setForeground(Color.WHITE);
        comparisonTable.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 12));
        comparisonTable.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));
        
        // Show grid with larger spacing for VNC visibility
        comparisonTable.setGridColor(new Color(0, 0, 0));
        comparisonTable.setShowGrid(true);
        comparisonTable.setShowHorizontalLines(true);
        comparisonTable.setShowVerticalLines(true);
        comparisonTable.setIntercellSpacing(new java.awt.Dimension(2, 2));
        comparisonTable.setColumnSelectionAllowed(false);
        comparisonTable.setRowSelectionAllowed(false);
        
        // Create cell renderer with visible borders and background colors
        javax.swing.table.DefaultTableCellRenderer dataRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setOpaque(true);
                
                // Alternating background colors for columns
                if (column % 2 == 0) {
                    setBackground(new Color(240, 240, 240));  // Light gray
                } else {
                    setBackground(new Color(255, 255, 255));  // White
                }
                
                // Add visible border
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                
                // First column (Attribute names) - BOLD and UPPERCASE
                if (column == 0) {
                    setFont(new Font("Times New Roman", Font.BOLD, 11));
                } else {
                    setFont(new Font("Times New Roman", Font.PLAIN, 11));
                }
                
                return c;
            }
        };
        
        // Apply renderer to all columns
        for (int i = 0; i < comparisonTable.getColumnCount(); i++) {
            // First column (Attribute) gets less space
            if (i == 0) {
                comparisonTable.getColumnModel().getColumn(i).setMinWidth(50);
                comparisonTable.getColumnModel().getColumn(i).setPreferredWidth(70);
            } else {
                // Course columns get more space
                comparisonTable.getColumnModel().getColumn(i).setMinWidth(130);
                comparisonTable.getColumnModel().getColumn(i).setPreferredWidth(160);
            }
            comparisonTable.getColumnModel().getColumn(i).setCellRenderer(dataRenderer);
        }
        
        // Header renderer with borders
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setOpaque(true);
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                setFont(new Font("Times New Roman", Font.BOLD, 12));
                return c;
            }
        };
        comparisonTable.getTableHeader().setDefaultRenderer(headerRenderer);
        
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(new JScrollPane(comparisonTable), BorderLayout.CENTER);
        
        JDialog dialog = new JDialog(this, "Course Comparison", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(tablePanel);
        dialog.setSize(900, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
