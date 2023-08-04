package snakeGame;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;
import java.util.List;

public class Utils {

    // Method to check if a Point is outside a JPanel with given dimensions
    public static boolean isPointOutsidePanel(Point point, JPanel panel) {
        // Get the size of the JPanel
        Dimension panelSize = panel.getSize();

        // Check if the x coordinate of the Point is outside the JPanel
        if (point.x < 0 || point.x >= panelSize.width) {
            return true;
        }

        // Check if the y coordinate of the Point is outside the JPanel
        if (point.y < 0 || point.y >= panelSize.height) {
            return true;
        }

        // If both x and y coordinates are within the JPanel, the Point is not outside
        return false;
    }

    // Method to check if a Point is inside a list of points except for the first element
    public static boolean isPointInsideListExceptFirst(Point point, List<Point> points) {
        for (int i = 1; i < points.size(); i++) {
            Point currentPoint = points.get(i);
            if (point.equals(currentPoint)) {
                return true;
            }
        }
        return false;
    }
}
