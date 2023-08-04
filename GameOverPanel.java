package snakeGame;

import javax.swing.*;

public class GameOverPanel extends JPanel {
    // JPanel that shows if the game is over.
    private final JButton retryButton;
    private final JButton closeButton;

    public GameOverPanel(int points) {
        // Create and set up the "Game Over" screen
        retryButton = new JButton("Retry");
        closeButton = new JButton("Close");

        // Add buttons to the panel
        add(new JLabel("Game Over"));
        add(retryButton);
        add(closeButton);

        JLabel pointsLabel = new JLabel("Points: " + points);
        add(pointsLabel);
    }

    public JButton getRetryButton() {
        return retryButton;
    }

    public JButton getCloseButton() {
        return closeButton;
    }
}
