import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

public class GameCanvas extends JPanel implements KeyListener, MouseMotionListener {
    private Tank playerTank;
    private Gamemap gameMap;
    private static final int MOVE_SPEED = 2;
    private static final String BULLET_IMAGE_PATH = "C:/Users/vbhak/OneDrive/Pictures/Tank_round.png";

    public GameCanvas() throws IOException {
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        gameMap = new Gamemap(screenWidth, screenHeight, 100); // Initialize the randomized map

        // Find a valid spawn position for the tank
        Point spawnPoint = findValidSpawn(screenWidth, screenHeight);
        playerTank = new Tank(
            "C:/Users/vbhak/Downloads/TankHull-removebg-preview.png",
            "C:/Users/vbhak/Downloads/torrent-removebg-preview.png",
            spawnPoint.x, spawnPoint.y, 64, 64
        );

        playerTank.getHull().setScale(0.5); // Adjust scale
    }

    private Point findValidSpawn(int screenWidth, int screenHeight) {
        Random random = new Random();
        Rectangle tankBounds = new Rectangle(0, 0, 64, 64); // Initial placeholder for tank size
        int maxAttempts = 100;

        for (int i = 0; i < maxAttempts; i++) {
            // Generate random position
            int x = random.nextInt(screenWidth - tankBounds.width);
            int y = random.nextInt(screenHeight - tankBounds.height);
            tankBounds.setLocation(x, y);

            // Check if the position is collision-free
            if (!gameMap.checkCollision(tankBounds)) {
                return new Point(x, y); // Return valid position
            }
        }

        // Default spawn point if no valid position is found
        System.err.println("Failed to find a valid spawn position. Using default.");
        return new Point(screenWidth / 2, screenHeight / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.LIGHT_GRAY);

        gameMap.draw(g); // Draw the map
        playerTank.draw(g); // Draw the player's tank
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Rectangle nextPosition = new Rectangle(playerTank.getHull().getX(), playerTank.getHull().getY(), playerTank.getHull().getWidth(), playerTank.getHull().getHeight());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                nextPosition.y -= MOVE_SPEED;
                if (!gameMap.checkCollision(nextPosition)) {
                    playerTank.move(0, -MOVE_SPEED);
                    playerTank.getHull().setRotationAngle(0);
                }
                break;
            case KeyEvent.VK_S:
                nextPosition.y += MOVE_SPEED;
                if (!gameMap.checkCollision(nextPosition)) {
                    playerTank.move(0, MOVE_SPEED);
                    playerTank.getHull().setRotationAngle(180);
                }
                break;
            case KeyEvent.VK_A:
                nextPosition.x -= MOVE_SPEED;
                if (!gameMap.checkCollision(nextPosition)) {
                    playerTank.move(-MOVE_SPEED, 0);
                    playerTank.getHull().setRotationAngle(270);
                }
                break;
            case KeyEvent.VK_D:
                nextPosition.x += MOVE_SPEED;
                if (!gameMap.checkCollision(nextPosition)) {
                    playerTank.move(MOVE_SPEED, 0);
                    playerTank.getHull().setRotationAngle(90);
                }
                break;
            case KeyEvent.VK_SPACE:
                playerTank.fire(BULLET_IMAGE_PATH);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        playerTank.stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        playerTank.aimAt(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("Tank Game");
            GameCanvas canvas = new GameCanvas();

            // Full-screen setup
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.add(canvas);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            new Timer(16, e -> {
                canvas.playerTank.update((int) screenSize.getWidth(), (int) screenSize.getHeight(), canvas.gameMap);
                canvas.repaint();
            }).start();
            
        } catch (IOException e) {
            System.err.println("Error initializing game.");
            e.printStackTrace();
        }
    }
}
