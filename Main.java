package snakeGame;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            try {
                // Load the PNG images from files
                Image snakeHead = ImageLoader.loadImage("snakeHead.png");
                Image snakeBody = ImageLoader.loadImage("snakeBody.png");
                Image food = ImageLoader.loadImage("mouse.png");

                // Create the custom JPanel with custom images (snakeHead should be orientated right)
                // and custom speed.
                Board Board = new Board(snakeHead, snakeBody, food, 200);

                // Add the panel to the frame
                frame.add(Board);

                frame.pack();
                frame.setLocationRelativeTo(null); // Center the frame on the screen
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
