package electiview.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JButton with rounded corners
 */
public class RoundedButton extends JButton {
    private int arcRadius;

    public RoundedButton(String text, int arcRadius) {
        super(text);
        this.arcRadius = arcRadius;
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(new Color(Math.max(getBackground().getRed() - 30, 0),
                    Math.max(getBackground().getGreen() - 30, 0),
                    Math.max(getBackground().getBlue() - 30, 0)));
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(Math.min(getBackground().getRed() + 30, 255),
                    Math.min(getBackground().getGreen() + 30, 255),
                    Math.min(getBackground().getBlue() + 30, 255)));
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcRadius, arcRadius);

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border needed for rounded button
    }
}
