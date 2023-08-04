package snakeGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class ImageLoader {
    // loads images with awareness of IOException
    public static Image loadImage(String imagePath) throws IOException {
        File file = new File(imagePath);
        return ImageIO.read(file);
    }
}
class Board extends JPanel {
    // biggest class that actually holds the gameplay in
    private static final int GRID_SPACING = 30;
    private static final int PANEL_WIDTH = 17 * GRID_SPACING;
    private static final int PANEL_HEIGHT = 15 * GRID_SPACING;
    private final Image snakeHead;
    private final Image snakeBody;
    private final Image food;
    private java.util.List<Point> snakePositions;
    private Point foodPosition;
    private final Timer timer;
    // Flags to represent the current direction
    private boolean movingLeft = false;
    private boolean movingRight = true;
    private boolean movingUp = false;
    private boolean movingDown = false;
    // Rotation angle for the snakeHead
    private double rotationAngle = 0; // Default: No rotation (facing right)
    private boolean canTurn = true; //ensures user doesn't turn back on themselves if pressing keys very quickly
    private int dx = GRID_SPACING; // how much to move right by
    private int dy = 0; // how much to move down by
    private int points = 0;

    public Board(Image snakeHead, Image snakeBody, Image food, int delay) {
        this.snakeHead = snakeHead;
        this.snakeBody = snakeBody;
        this.food = food;
        this.snakePositions = setSnakePositions();
        this.foodPosition = new Point(13*GRID_SPACING, 7*GRID_SPACING);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        // Create a timer with a specified delay (milliseconds). Adjust the delay to control the speed of movement
        timer = new Timer(delay, e -> { //lambda function
            // Update image positions based on the movement direction and speed
            moveSnake();
            // Trigger repainting to show the updated positions
            repaint();
        });
        // Start the timer
        timer.start();

        // Attach the KeyAdapter to this ImagePanel
        addKeyListener(new ArrowKeyAdapter());
        setFocusable(true); // Ensure the panel receives keyboard focus
    }

    public java.util.List<Point> setSnakePositions()
            // sets starting snake position of 3 points (1 will be the head)
    {
        java.util.List<Point> initialSnakePositions = new ArrayList<>();
        initialSnakePositions.add(new Point(3*GRID_SPACING, 7*GRID_SPACING));
        initialSnakePositions.add(new Point(2*GRID_SPACING, 7*GRID_SPACING));
        initialSnakePositions.add(new Point(GRID_SPACING, 7*GRID_SPACING));
        return initialSnakePositions;
    }

    public void moveSnake() {
        // method deals with how the snake moves from frame to frame. This includes its body following itself,
        // eating the food and checking that the snake has not hit itself or a wall
        
        Point Tail = snakePositions.get(snakePositions.size() - 1); //needed in case eatFood and then need to extend snake
        
        // Update all other elements to hold the data of the preceding element
        // This is the desired 'Snake' behaviour
        for (int i = snakePositions.size() - 1; i >= 1; i--) {
            Point currentPoint = snakePositions.get(i);
            Point nextPoint = snakePositions.get(i - 1);
            currentPoint.setLocation(nextPoint);
        }
        
        // Move the head by dx and dy
        Point Head = snakePositions.get(0);
        Head.setLocation(Head.x + dx, Head.y + dy);

        if (Head.equals(foodPosition)) {
            eatFood(Tail);
        }
        if(Utils.isPointOutsidePanel(Head, this) || Utils.isPointInsideListExceptFirst(Head, snakePositions)){
            timer.stop();
            showGameOverScreen();
        }
    }

    public void eatFood(Point Tail){
        // adds an element to the snake if food eaten and causes a new food item to be generated
        snakePositions.add(new Point(Tail.x, Tail.y));
        newFood(snakePositions);
        points += 1;
    }

    public void newFood(java.util.List<Point> snakePositions){
        // generates new food item in random location, checking this is not where the snake currently is
        Random random = new Random();
        Point randomPoint;

        do {
            int x = random.nextInt(PANEL_WIDTH / GRID_SPACING) * GRID_SPACING;
            int y = random.nextInt(PANEL_HEIGHT / GRID_SPACING) * GRID_SPACING;

            randomPoint = new Point(x, y);
        } while (snakePositions.contains(randomPoint));

        foodPosition = randomPoint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // deals with the painting of the board. Uses Painting class methods to draw a grid and scoreboard.
        // Also uses an affine rotation to orientate the snakeHead depending on which way it's going.
        super.paintComponent(g);

        for (int i = 1; i < snakePositions.size(); i++) { //start at 1 as the snake positions also has the head at index 0
            Point position = snakePositions.get(i);
            int x = position.x;
            int y = position.y;
            g.drawImage(snakeBody, x, y, GRID_SPACING, GRID_SPACING, this);
        }
        g.drawImage(food, foodPosition.x, foodPosition.y, GRID_SPACING, GRID_SPACING, this);

        Painting.drawGridBackground(g, this, GRID_SPACING);
        Painting.drawText(g, points, "Points");

        if (snakeHead != null){
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform rotationTransform = AffineTransform.getRotateInstance(rotationAngle,
                    snakePositions.get(0).x + (double) GRID_SPACING /2, snakePositions.get(0).y + (double) GRID_SPACING /2);
            g2d.setTransform(rotationTransform);
            // Draw the rotated image
            g2d.drawImage(snakeHead, snakePositions.get(0).x, snakePositions.get(0).y, GRID_SPACING, GRID_SPACING, this);
            // Dispose of the Graphics2D object to release resources
            g2d.dispose();
        }

        canTurn = true;
    }

    public void resetGame() {
        // Reset all the game elements and properties to their initial values
        movingLeft = false; movingRight = true;
        movingUp = false; movingDown = false;
        rotationAngle = 0;

        dx = GRID_SPACING; dy = 0;

        points = 0;
        timer.restart();

        snakePositions = setSnakePositions();
        foodPosition = new Point(13*GRID_SPACING, 7*GRID_SPACING);
    }

    private void showGameOverScreen() {
        // Shows GameOverScreen once the user has hit a wall or itself.
        // Screen gives options to retry or close the game.

        // Get the parent JFrame
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Create and add the GameOverPanel to the parent JFrame
        GameOverPanel gameOverPanel = new GameOverPanel(points);
        frame.getContentPane().remove(this);
        frame.getContentPane().add(gameOverPanel);
        frame.revalidate();
        frame.repaint();

        // Add action listeners to the buttons in GameOverPanel
        gameOverPanel.getRetryButton().addActionListener(e -> {
            // Retry button clicked, restart the game
            frame.getContentPane().remove(gameOverPanel);
            frame.getContentPane().add(Board.this);
            resetGame();
            requestFocusInWindow(); // Request focus back to Board otherwise KeyAdapter doesn't work
            frame.revalidate();
            frame.repaint();
        });

        gameOverPanel.getCloseButton().addActionListener(e -> {
            // Close button clicked, exit the program
            System.exit(0);
        });
    }

    // Custom KeyAdapter to handle arrow key events
    private class ArrowKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            // Determine the movement direction based on the arrow key pressed
            if (keyCode == KeyEvent.VK_LEFT && !movingRight && canTurn) {
                dx = -GRID_SPACING; dy = 0;
                rotationAngle = Math.PI; // Rotate 180 degrees (facing left)
                movingLeft = true;
                movingUp = false; movingDown = false;
            } else if (keyCode == KeyEvent.VK_RIGHT && !movingLeft && canTurn) {
                dx = GRID_SPACING; dy = 0;
                rotationAngle = 0; // No rotation (facing right)
                movingRight = true;
                movingUp = false; movingDown = false;
            } else if (keyCode == KeyEvent.VK_UP && !movingDown && canTurn) {
                dx = 0; dy = -GRID_SPACING;
                rotationAngle = -Math.PI / 2; // Rotate -90 degrees (facing up)
                movingLeft = false; movingRight = false;
                movingUp = true;
            } else if (keyCode == KeyEvent.VK_DOWN && !movingUp && canTurn) {
                dx = 0; dy = GRID_SPACING;
                rotationAngle = Math.PI / 2; // Rotate 90 degrees (facing down)
                movingLeft = false; movingRight = false;
                movingDown = true;
            }
            canTurn = false; // can't turn until repainted now key has been pressed
        }
    }
}
