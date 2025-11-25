package electiview;

import electiview.database.DatabaseManager;
import electiview.gui.LoginFrame;
import electiview.patterns.facade.ElectiviewFacade;
import electiview.patterns.observer.AdminNotificationObserver;
import electiview.patterns.observer.ReviewNotifier;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.getInstance();
        
        ReviewNotifier notifier = ReviewNotifier.getInstance();
        notifier.addObserver(new AdminNotificationObserver());
        
        initializeSampleData();
        
        SwingUtilities.invokeLater(() -> {
            try {
                // UNCOMMENT the line below to enable FlatLaf modern dark theme
                // FlatDarkLaf.setup();
                
                // OR use system look and feel (default)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
    
    private static void initializeSampleData() {
        ElectiviewFacade facade = ElectiviewFacade.getInstance();
        
        try {
            facade.addRollARNMapping("22i-1185", "ARN001185");
            facade.addRollARNMapping("22i-1329", "ARN001329");
            facade.addRollARNMapping("21i-0412", "ARN000412");
            facade.addRollARNMapping("admin", "ARN000000");
            
            facade.registerUser("admin", "ARN000000", "admin123", "Admin User", "admin@electiview.com", "ADMIN");
            
            facade.addCourse("CS401", "Software Design and Analysis", 3, "UML modeling, design patterns, software architecture");
            facade.addCourse("CS402", "Machine Learning", 3, "Neural networks, supervised learning, deep learning");
            facade.addCourse("CS403", "Cloud Computing", 3, "AWS, Azure, distributed systems");
            facade.addCourse("CS404", "Mobile App Development", 3, "Android, iOS, React Native");
            facade.addCourse("CS405", "Cybersecurity", 3, "Network security, cryptography, ethical hacking");
            
            System.out.println("Sample data initialized successfully");
        } catch (Exception e) {
            System.out.println("Sample data may already exist - continuing...");
        }
    }
}
