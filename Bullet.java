import java.awt.*;

public class Bullet {
    private int x, y;
    private int width = 16; // Bullet width
    private int height = 16; // Bullet height
    private double angle;
    private int speed = 10;

    public Bullet(int x, int y, double angle, String imagePath) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void update() {
        // Update bullet position based on angle and speed
        x += (int) (speed * Math.cos(angle));
        y += (int) (speed * Math.sin(angle));
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);
    }

    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
