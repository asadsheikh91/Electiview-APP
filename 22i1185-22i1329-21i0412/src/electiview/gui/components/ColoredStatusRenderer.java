package electiview.gui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom table cell renderer for colored status display
 */
public class ColoredStatusRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null) {
            String status = value.toString();
            
            if ("ACTIVE".equals(status)) {
                component.setForeground(new Color(0, 150, 0));
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            } else if ("NOT ACTIVE".equals(status)) {
                component.setForeground(new Color(255, 0, 0));
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            } else if ("Active".equals(status)) {
                component.setForeground(new Color(0, 150, 0));
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            } else if ("Inactive".equals(status)) {
                component.setForeground(new Color(255, 0, 0));
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            } else if ("Yes".equals(status) && column > 4) { // Locked column
                component.setForeground(new Color(255, 0, 0));
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            } else if ("No".equals(status) && column > 4) { // Not locked
                component.setForeground(new Color(0, 150, 0));
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            }
        }
        
        return component;
    }
}
