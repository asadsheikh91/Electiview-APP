package electiview.gui;

import electiview.models.Course;
import electiview.models.User;
import electiview.patterns.facade.ElectiviewFacade;
import electiview.gui.components.ColoredStatusRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageCoursesFrame extends JFrame {
    private User currentUser;
    private ElectiviewFacade facade;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deactivateButton;
    private JButton activateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backButton;
    
    public ManageCoursesFrame(User user) {
        this.currentUser = user;
        this.facade = ElectiviewFacade.getInstance();
        initComponents();
        loadCourses();
    }
    
    private void initComponents() {
        setTitle("ELECTIVIEW - Manage Courses");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel logoTopPanel = new JPanel(new BorderLayout());
        JLabel manageLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("resources/electiview_logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            manageLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            manageLogo.setText("[Logo]");
        }
        logoTopPanel.add(manageLogo, BorderLayout.EAST);
        JLabel titleLabel = new JLabel("Manage Courses", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        logoTopPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(logoTopPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Course Code", "Course Title", "Credits", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.setRowHeight(25);
        courseTable.getColumnModel().getColumn(4).setCellRenderer(new ColoredStatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(courseTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add Course");
        addButton.addActionListener(e -> handleAdd());
        buttonPanel.add(addButton);
        
        editButton = new JButton("Edit Course");
        editButton.addActionListener(e -> handleEdit());
        buttonPanel.add(editButton);
        
        deactivateButton = new JButton("Deactivate Course");
        deactivateButton.addActionListener(e -> handleDeactivate());
        buttonPanel.add(deactivateButton);
        
        activateButton = new JButton("Activate Course");
        activateButton.addActionListener(e -> handleActivate());
        buttonPanel.add(activateButton);
        
        deleteButton = new JButton("Delete Course");
        deleteButton.addActionListener(e -> handleDelete());
        buttonPanel.add(deleteButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadCourses());
        buttonPanel.add(refreshButton);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courses = facade.getAllCoursesForAdmin();
        for (Course course : courses) {
            String status = course.isActive() ? "ACTIVE" : "NOT ACTIVE";
            tableModel.addRow(new Object[]{
                course.getCourseId(),
                course.getCourseCode(),
                course.getCourseTitle(),
                course.getCredits(),
                status
            });
        }
    }
    
    private void handleAdd() {
        JTextField codeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField creditsField = new JTextField();
        JTextArea syllabusArea = new JTextArea(5, 20);
        
        Object[] message = {
            "Course Code:", codeField,
            "Course Title:", titleField,
            "Credits:", creditsField,
            "Syllabus:", new JScrollPane(syllabusArea)
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Add New Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String code = codeField.getText().trim();
                String title = titleField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                String syllabus = syllabusArea.getText().trim();
                
                if (facade.courseCodeExists(code)) {
                    JOptionPane.showMessageDialog(this, "Course Code Already Exists", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (facade.addCourse(code, title, credits, syllabus)) {
                    JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add course", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid credits value", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleEdit() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        List<Course> courses = facade.getAllCourses();
        Course selectedCourse = null;
        for (Course course : courses) {
            if (course.getCourseId() == courseId) {
                selectedCourse = course;
                break;
            }
        }
        
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Course not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JTextField codeField = new JTextField(selectedCourse.getCourseCode());
        JTextField titleField = new JTextField(selectedCourse.getCourseTitle());
        JTextField creditsField = new JTextField(String.valueOf(selectedCourse.getCredits()));
        JTextArea syllabusArea = new JTextArea(selectedCourse.getSyllabus() != null ? selectedCourse.getSyllabus() : "", 5, 20);
        
        Object[] message = {
            "Course Code:", codeField,
            "Course Title:", titleField,
            "Credits:", creditsField,
            "Syllabus:", new JScrollPane(syllabusArea)
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                selectedCourse.setCourseCode(codeField.getText().trim());
                selectedCourse.setCourseTitle(titleField.getText().trim());
                selectedCourse.setCredits(Integer.parseInt(creditsField.getText().trim()));
                selectedCourse.setSyllabus(syllabusArea.getText().trim());
                
                if (facade.updateCourse(selectedCourse)) {
                    JOptionPane.showMessageDialog(this, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update course", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid credits value", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleDeactivate() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to deactivate this course?\nIt will still be viewable but marked as NOT ACTIVE.", "Confirm Deactivate", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.deactivateCourse(courseId)) {
                JOptionPane.showMessageDialog(this, "Course deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to deactivate course", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleActivate() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to activate this course?", "Confirm Activate", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.activateCourse(courseId)) {
                JOptionPane.showMessageDialog(this, "Course activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to activate course", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleDelete() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to DELETE this course permanently?\nThis action cannot be undone.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (facade.deleteCourse(courseId)) {
                JOptionPane.showMessageDialog(this, "Course deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete course", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
