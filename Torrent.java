import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Torrent {
    double angle = 0.0; // Fixed angle in degrees (e.g., 0 for upward)
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
    
        // Do NOT apply rotation (the turret will stay stationary)
        // Draw the turret
        g2d.drawImage(turretImage, at, null);
    }
    
    

    // Method to shoot bullets in the turret's fixed direction
    public double getAngle() {
        return angle; // Return the fixed angle of the turret
    }
    

    public void setScale(double scale) {
        this.scale = scale; // Adjust the scale factor dynamically
    }

    public double getScale() {
        return scale;
    }

    public void aimAt(Point mousePosition, int i, int j) {
        
        throw new UnsupportedOperationException("Unimplemented method 'aimAt'");
    }
}
