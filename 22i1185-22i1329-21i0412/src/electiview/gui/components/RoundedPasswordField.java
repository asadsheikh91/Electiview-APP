package electiview.gui.components;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Custom JPasswordField with rounded corners
 */
public class RoundedPasswordField extends JPasswordField {
    private int arcRadius;
    private Color borderColor;

    public RoundedPasswordField(int columns, int arcRadius, Color borderColor) {
        super(columns);
        this.arcRadius = arcRadius;
        this.borderColor = borderColor;
        setOpaque(false);
        setBorder(new RoundedBorder(borderColor, arcRadius));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcRadius, arcRadius);

        super.paintComponent(g);
    }

    /**
     * Custom border with rounded corners
     */
    private static class RoundedBorder extends AbstractBorder {
        private Color color;
        private int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 10, 5, 10);
        }
    }
}
