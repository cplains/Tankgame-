import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Gamemap {
    private ArrayList<Rectangle> obstacles;
    private int mapWidth;
    private int mapHeight;
    private int cellSize;
    private Random random;

    public Gamemap(int mapWidth, int mapHeight, int cellSize) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.cellSize = cellSize;
        this.obstacles = new ArrayList<>();
        this.random = new Random();

        generateRandomMap();
    }

    private void generateRandomMap() {
        // Clear any existing obstacles
        obstacles.clear();

        // Create a grid-like structure and randomly add walls
        for (int y = 0; y < mapHeight; y += cellSize) {
            for (int x = 0; x < mapWidth; x += cellSize) {
                // Randomly decide to add a vertical or horizontal wall
                if (random.nextBoolean()) {
                    // Add a horizontal wall
                    if (random.nextBoolean()) {
                        obstacles.add(new Rectangle(x, y, cellSize, cellSize / 5));
                    }
                    // Add a vertical wall
                    else {
                        obstacles.add(new Rectangle(x, y, cellSize / 5, cellSize));
                    }
                }
            }
        }

        // Create a border around the map
        createBorders();
    }

    private void createBorders() {
        // Top border
        obstacles.add(new Rectangle(0, 0, mapWidth, cellSize / 4));
        // Bottom border
        obstacles.add(new Rectangle(0, mapHeight - cellSize / 4, mapWidth, cellSize / 4));
        // Left border
        obstacles.add(new Rectangle(0, 0, cellSize / 4, mapHeight));
        // Right border
        obstacles.add(new Rectangle(mapWidth - cellSize / 4, 0, cellSize / 4, mapHeight));
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK); // Wall color
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }
    }

    public boolean checkCollision(Rectangle entity) {
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(entity)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Rectangle> getObstacles() {
        return obstacles;
    }
}
