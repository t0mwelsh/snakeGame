package snakeGame;

import java.awt.*;
import java.awt.Dimension;
import javax.swing.JPanel;

public class Painting {
    // couple of generic methods for drawing grids or putting text on a JPanel
    public static void drawGridBackground(Graphics g, JPanel panel, int gridSpacing) {
        // draws a square grid of grey lines or argument defined spacing

        // Get the size of the panel
        Dimension panelSize = panel.getSize();

        // Set the color for the grid lines
        g.setColor(Color.LIGHT_GRAY);

        // Draw horizontal grid lines
        for (int y = 0; y < panelSize.height; y += gridSpacing) {
            g.drawLine(0, y, panelSize.width, y);
        }

        // Draw vertical grid lines
        for (int x = 0; x < panelSize.width; x += gridSpacing) {
            g.drawLine(x, 0, x, panelSize.height);
        }
    }

    public static void drawText(Graphics g, int number, String variable) {
        // Writes a variable name with an associated value on a JPanel

        // Set the color for the points text
        g.setColor(Color.BLACK);

        // Set the font for the points text
        Font font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);

        // Draw the points text in the top-left corner (adjust position as needed)
        int textX = 10;
        int textY = 30;
        String pointsText = variable + ": " + number;
        g.drawString(pointsText, textX, textY);
    }
}
