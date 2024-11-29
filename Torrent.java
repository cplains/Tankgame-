import javax.imageio.ImageIO; // For image handling
import java.awt.*; // For Graphics, Point, etc.
import java.awt.geom.AffineTransform; // For rotation and transformations
import java.awt.image.BufferedImage; // For BufferedImage
import java.io.File; // For file handling
import java.io.IOException; // For IOException handling

public class Torrent {
    double angle = 9.0; // Angle in degrees
    private double scale = 0.2; // Match the tank hull's scale
    private BufferedImage turretImage;
    private int width;
    private int height;

    public Torrent(String imagePath, int width, int height) throws IOException {
        this.width = width;
        this.height = height;

        // Load the turret image
        turretImage = ImageIO.read(new File(imagePath));
        if (turretImage == null) {
            throw new IOException("Failed to load turret image: " + imagePath);
        }
    }

    public void draw(Graphics g, int hullCenterX, int hullCenterY) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = new AffineTransform();

        // Scale the turret
        at.scale(scale, scale);

        // Translate the turret's position to match the hull's center
        double scaledWidth = width * scale;
        double scaledHeight = height * scale;

        at.translate((hullCenterX - scaledWidth / 2) / scale, (hullCenterY - scaledHeight / 2) / scale);

        // Rotate around the turret's center
        at.rotate(Math.toRadians(angle), scaledWidth / 2, scaledHeight / 2);

        // Draw the turret
        g2d.drawImage(turretImage, at, null);
    }

    public void aimAt(Point mousePosition, int hullCenterX, int hullCenterY) {
        double deltaX = mousePosition.getX() - hullCenterX;
        double deltaY = mousePosition.getY() - hullCenterY;

        angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public void setScale(double scale) {
        this.scale = scale; // Adjust the scale factor dynamically
    }

    public double getScale() {
        return scale;
    }
}
