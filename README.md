import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TankGame extends JFrame {
    private GamePanel gamePanel;

    public TankGame() {
        setTitle("Tank Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);

        // Timer for game loop
        Timer timer = new Timer(20, e -> {
            gamePanel.update();
            gamePanel.repaint();
        });
        timer.start();
    }

    class GamePanel extends JPanel {
        private Tank tank;
        private List<Rectangle> walls;
        private int cols, rows, cellSize = 40;
        private boolean[][] visited;

        public GamePanel() {
            tank = new Tank(25, 25); // Start tank at a position
            walls = new ArrayList<>();
            cols = 800 / cellSize;
            rows = 600 / cellSize;
            visited = new boolean[cols][rows];

            generateMaze(0, 0);

            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    System.out.println("Key Pressed: " + KeyEvent.getKeyText(e.getKeyCode())); // Debugging line
                    tank.move(e.getKeyCode(), walls);
                    repaint(); // Repaint after movement
                }
            });

            requestFocusInWindow(); // Request focus for key events
        }

        // Maze generation using recursive backtracking
        private void generateMaze(int x, int y) {
            if (!isInBounds(x, y) || visited[x][y]) {
                return; // Stop if out of bounds or already visited
            }

            visited[x][y] = true;

            // Directions: Right, Down, Left, Up
            int[] dx = {1, 0, -1, 0};
            int[] dy = {0, 1, 0, -1};

            List<Integer> directions = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                directions.add(i);
            }
            Collections.shuffle(directions); // Randomize directions

            for (int direction : directions) {
                int newX = x + dx[direction];
                int newY = y + dy[direction];

                if (isInBounds(newX, newY) && !visited[newX][newY]) {
                    // Create a wall between the cells
                    if (direction == 0) { // Right
                        walls.add(new Rectangle((x + 1) * cellSize - 5, y * cellSize, 5, cellSize));
                    } else if (direction == 1) { // Down
                        walls.add(new Rectangle(x * cellSize, (y + 1) * cellSize - 5, cellSize, 5));
                    } else if (direction == 2) { // Left
                        walls.add(new Rectangle(x * cellSize, y * cellSize, 5, cellSize));
                    } else if (direction == 3) { // Up
                        walls.add(new Rectangle(x * cellSize, y * cellSize, cellSize, 5));
                    }

                    generateMaze(newX, newY);
                }
            }
        }

        private boolean isInBounds(int x, int y) {
            return x >= 0 && x < cols && y >= 0 && y < rows;
        }

        public void update() {
            // Update game state (if needed)
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            tank.draw(g);
            g.setColor(Color.GRAY);
            for (Rectangle wall : walls) {
                g.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        }
    }

    class Tank {
        private int x, y;
        private static final int SIZE = 25; // Tank size
        private static final int MOVE_SPEED = 5; // Movement speed

        public Tank(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(int keyCode, List<Rectangle> walls) {
            int newX = x;
            int newY = y;

            switch (keyCode) {
                case KeyEvent.VK_UP:
                    newY -= MOVE_SPEED;
                    break;
                case KeyEvent.VK_DOWN:
                    newY += MOVE_SPEED;
                    break;
                case KeyEvent.VK_LEFT:
                    newX -= MOVE_SPEED;
                    break;
                case KeyEvent.VK_RIGHT:
                    newX += MOVE_SPEED;
                    break;
            }

            // Prevent the tank from moving out of bounds
            newX = Math.max(0, Math.min(newX, 800 - SIZE));
            newY = Math.max(0, Math.min(newY, 600 - SIZE));

            // Check for collision with walls
            Rectangle tankRect = new Rectangle(newX, newY, SIZE, SIZE);
            for (Rectangle wall : walls) {
                if (tankRect.intersects(wall)) {
                    return; // Collision detected, don't move
                }
            }

            // If no collision, update position
            x = newX;
            y = newY;
        }

        public void draw(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, SIZE, SIZE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TankGame::new); // Ensure GUI updates are on the Event Dispatch Thread
    }
}
