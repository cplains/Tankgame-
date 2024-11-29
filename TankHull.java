import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TankHull {
    private int xPos;
    private int yPos;
    private double xVel = 0.0;
    private double yVel = 0.0;
    private double maxSpeed = 4.0;
    private double rotationAngle = 0; // Rotation angle in degrees
    private double scale = 0.1; // Scale factor (20% size)

    private int width;
    private int height;
    private BufferedImage hullImage;

    public TankHull(String imagePath, int startX, int startY, int width, int height) throws IOException {
        this.xPos = startX;
        this.yPos = startY;
        this.width = width;
        this.height = height;

        System.out.println("Attempting to load hull image from: " + imagePath);

        // Load the hull image with error handling
        try {
            hullImage = ImageIO.read(new File(imagePath));
            if (hullImage == null) {
                throw new IOException("Image file is not valid or supported: " + imagePath);
            }
            System.out.println("Hull image loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading hull image from: " + imagePath);
            e.printStackTrace();
            throw e;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = new AffineTransform();

        // Scale the image
        at.scale(scale, scale);

        // Translate to the scaled position of the tank hull
        at.translate((xPos / scale), (yPos / scale));

        // Rotate around the center of the hull
        at.translate(width / 2.0, height / 2.0); // Move to center
        at.rotate(Math.toRadians(rotationAngle));
        at.translate(-width / 2.0, -height / 2.0); // Move back

        // Draw the hull
        g2d.drawImage(hullImage, at, null);
    }

    public void move(double dx, double dy) {
        xVel = Math.max(-maxSpeed, Math.min(maxSpeed, dx));
        yVel = Math.max(-maxSpeed, Math.min(maxSpeed, dy));
    }

    public void update() {
        xPos += xVel;
        yPos += yVel;
    }

    public void stop() {
        xVel = 0;
        yVel = 0;
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
    }

    public void setScale(double scale) {
        this.scale = scale; // Adjust the scale factor dynamically
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getWidth() {
        return (int) (width * scale); // Return scaled width
    }

    public int getHeight() {
        return (int) (height * scale); // Return scaled height
    }
}
