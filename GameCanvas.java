import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

public class GameCanvas extends JPanel implements KeyListener, MouseMotionListener {
    private Tank playerOneTank;
    private Tank playerTwoTank;
    private Gamemap gameMap;
    private static final int MOVE_SPEED = 2;
    private static final String BULLET_IMAGE_PATH = "C:/Users/vbhak/OneDrive/Pictures/Tank_round.png";
    private boolean gameOver = false;

    public GameCanvas() throws IOException {
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        gameMap = new Gamemap(screenWidth, screenHeight, 100); // Initialize the randomized map

        // Player 1 Tank
        Point spawnPoint1 = findValidSpawn(screenWidth, screenHeight);
        playerOneTank = new Tank(
            "C:/Users/vbhak/Downloads/TankHull-removebg-preview.png",
            "C:/Users/vbhak/Downloads/torrent-removebg-preview.png",
            spawnPoint1.x, spawnPoint1.y, 64, 64
        );
        playerOneTank.getHull().setScale(0.5); // Adjust scale

        // Player 2 Tank
        Point spawnPoint2 = findValidSpawn(screenWidth, screenHeight);
        playerTwoTank = new Tank(
            "C:/Users/vbhak/Downloads/TankHull-removebg-preview.png",
            "C:/Users/vbhak/Downloads/torrent-removebg-preview.png",
            spawnPoint2.x, spawnPoint2.y, 64, 64
        );
        playerTwoTank.getHull().setScale(0.5); // Adjust scale
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

    private void checkCollisions() {
        // Check bullets from Player 1 hitting Player 2
        for (Bullet bullet : playerOneTank.getBullets()) {
            if (bullet.getBounds().intersects(playerTwoTank.getBounds())) {
                System.out.println("Player 2 hit!");
                gameOver = true;
                break;
            }
        }
    
        // Check bullets from Player 2 hitting Player 1
        for (Bullet bullet : playerTwoTank.getBullets()) {
            if (bullet.getBounds().intersects(playerOneTank.getBounds())) {
                System.out.println("Player 1 hit!");
                gameOver = true;
                break;
            }
        }
    
        if (gameOver) {
            playerOneTank.getBullets().clear();
            playerTwoTank.getBullets().clear();
            System.out.println("Game Over!");
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.LIGHT_GRAY);

        if (!gameOver) {
            gameMap.draw(g); // Draw the map
            playerOneTank.draw(g); // Draw Player 1's tank
            playerTwoTank.draw(g); // Draw Player 2's tank
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over!", getWidth() / 2 - 150, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return; // Disable input after game over

        // Player 1 Controls (WASD + Space)
        Rectangle nextPositionP1 = new Rectangle(playerOneTank.getHull().getX(), playerOneTank.getHull().getY(), playerOneTank.getHull().getWidth(), playerOneTank.getHull().getHeight());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                nextPositionP1.y -= MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP1)) {
                    playerOneTank.move(0, -MOVE_SPEED);
                    playerOneTank.getHull().setRotationAngle(0);
                }
                break;
            case KeyEvent.VK_S:
                nextPositionP1.y += MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP1)) {
                    playerOneTank.move(0, MOVE_SPEED);
                    playerOneTank.getHull().setRotationAngle(180);
                }
                break;
            case KeyEvent.VK_A:
                nextPositionP1.x -= MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP1)) {
                    playerOneTank.move(-MOVE_SPEED, 0);
                    playerOneTank.getHull().setRotationAngle(270);
                }
                break;
            case KeyEvent.VK_D:
                nextPositionP1.x += MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP1)) {
                    playerOneTank.move(MOVE_SPEED, 0);
                    playerOneTank.getHull().setRotationAngle(90);
                }
                break;
            case KeyEvent.VK_SPACE:
                playerOneTank.fire(BULLET_IMAGE_PATH);
                break;

            // Player 2 Controls (Arrow Keys + Enter)
            case KeyEvent.VK_UP:
                Rectangle nextPositionP2 = new Rectangle(playerTwoTank.getHull().getX(), playerTwoTank.getHull().getY(), playerTwoTank.getHull().getWidth(), playerTwoTank.getHull().getHeight());
                nextPositionP2.y -= MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP2)) {
                    playerTwoTank.move(0, -MOVE_SPEED);
                    playerTwoTank.getHull().setRotationAngle(0);
                }
                break;
            case KeyEvent.VK_DOWN:
                nextPositionP2 = new Rectangle(playerTwoTank.getHull().getX(), playerTwoTank.getHull().getY(), playerTwoTank.getHull().getWidth(), playerTwoTank.getHull().getHeight());
                nextPositionP2.y += MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP2)) {
                    playerTwoTank.move(0, MOVE_SPEED);
                    playerTwoTank.getHull().setRotationAngle(180);
                }
                break;
            case KeyEvent.VK_LEFT:
                nextPositionP2 = new Rectangle(playerTwoTank.getHull().getX(), playerTwoTank.getHull().getY(), playerTwoTank.getHull().getWidth(), playerTwoTank.getHull().getHeight());
                nextPositionP2.x -= MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP2)) {
                    playerTwoTank.move(-MOVE_SPEED, 0);
                    playerTwoTank.getHull().setRotationAngle(270);
                }
                break;
            case KeyEvent.VK_RIGHT:
                nextPositionP2 = new Rectangle(playerTwoTank.getHull().getX(), playerTwoTank.getHull().getY(), playerTwoTank.getHull().getWidth(), playerTwoTank.getHull().getHeight());
                nextPositionP2.x += MOVE_SPEED;
                if (!gameMap.checkCollision(nextPositionP2)) {
                    playerTwoTank.move(MOVE_SPEED, 0);
                    playerTwoTank.getHull().setRotationAngle(90);
                }
                break;
            case KeyEvent.VK_ENTER:
                playerTwoTank.fire(BULLET_IMAGE_PATH);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
            playerOneTank.stop();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerTwoTank.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        playerOneTank.aimAt(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("Two-Player Tank Game");
            GameCanvas canvas = new GameCanvas();

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.add(canvas);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            new Timer(16, e -> {
                canvas.playerOneTank.update((int) screenSize.getWidth(), (int) screenSize.getHeight(), canvas.gameMap);
                canvas.playerTwoTank.update((int) screenSize.getWidth(), (int) screenSize.getHeight(), canvas.gameMap);
                canvas.checkCollisions();
                canvas.repaint();
            }).start();

        } catch (IOException e) {
            System.err.println("Error initializing game.");
            e.printStackTrace();
        }
    }
}
