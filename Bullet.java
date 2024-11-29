import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bullet {
    private int x, y;
    private int speed = 10; // Speed of the bullet
    private double angle; // Direction in radians
    private BufferedImage bulletImage;

    public Bullet(int x, int y, double angle, String imagePath) throws IOException {
        this.x = x;
        this.y = y;
        this.angle = angle;

        System.out.println("Bullet initialized at (" + x + ", " + y + ") with angle: " + Math.toDegrees(angle));

        try {
            bulletImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading bullet image: " + imagePath);
            e.printStackTrace();
            throw e;
        }
    }

    public void update() {
        // Move the bullet in the direction of the angle
        x += (int) (speed * Math.cos(angle));
        y += (int) (speed * Math.sin(angle));
        System.out.println("Bullet updated to (" + x + ", " + y + ")");
    }

    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, 16, 16, null);
        } else {
            // Draw a fallback circle if the image is missing
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, 16, 16);
        }
        System.out.println("Drawing bullet at (" + x + ", " + y + ")");
    }
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
        

    public boolean isOffScreen(int screenWidth, int screenHeight) {
        // Check if the bullet is outside the screen boundaries
        return x < 0 || x > screenWidth || y < 0 || y > screenHeight;
    }
}
