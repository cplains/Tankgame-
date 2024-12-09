import javax.imageio.ImageIO;
import javax.sound.sampled.*;
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
    private double scale = 0.2; // Scale factor (20% size)

    private int width;
    private int height;
    private BufferedImage hullImage;

    private Clip moveSoundClip; // Clip for tank movement sound
    private boolean isMoving = false; // Track if the tank is moving

    private String firingSoundPath = "bullet_firing.wav"; // Replace with your actual sound file path

    public TankHull(String imagePath, int startX, int startY, int width, int height) {
        this.xPos = startX;
        this.yPos = startY;
        this.width = width;
        this.height = height;

        System.out.println("Attempting to load hull image from: " + imagePath);

        try {
            hullImage = ImageIO.read(new File(imagePath));
            if (hullImage == null) {
                throw new IOException("Image is null or unsupported.");
            }
            System.out.println("Hull image loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading hull image from: " + imagePath + ". Using default image.");
            hullImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // Default placeholder
            Graphics2D g2d = hullImage.createGraphics();
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, width, height); // Simple red rectangle as a placeholder
            g2d.dispose();
        }

        // Load movement sound
        try {
            File soundFile = new File("C:\\Users\\vbhak\\Downloads\\converted_audio.wav"); // Replace with your actual sound file path
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            moveSoundClip = AudioSystem.getClip();
            moveSoundClip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading movement sound.");
            e.printStackTrace();
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

        // Start movement sound if not already playing
        if (!isMoving) {
            startMovementSound();
        }
    }

    public void fireBullet() {
        // Play firing sound
        playFiringSound();
    }

    public void update() {
        xPos += xVel;
        yPos += yVel;

        // Stop movement sound if tank is stationary
        if (xVel == 0 && yVel == 0 && isMoving) {
            stopMovementSound();
        }
    }

    public void stop() {
        xVel = 0;
        yVel = 0;
        stopMovementSound();
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

    public Rectangle getBounds() {
        return new Rectangle(xPos, yPos, getWidth(), getHeight());
    }

    private void startMovementSound() {
        if (moveSoundClip != null && !moveSoundClip.isRunning()) {
            moveSoundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the sound continuously
            isMoving = true;
        }
    }

    private void stopMovementSound() {
        if (moveSoundClip != null && moveSoundClip.isRunning()) {
            moveSoundClip.stop();
            isMoving = false;
        }
    }

    private void playFiringSound() {
        try {
            File soundFile = new File(firingSoundPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip firingClip = AudioSystem.getClip();
            firingClip.open(audioStream);
            firingClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing firing sound.");
            e.printStackTrace();
        }
    }
}
