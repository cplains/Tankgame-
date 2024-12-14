import java.awt.*; // For Graphics, Rectangle, etc.
import java.io.IOException; // For IOException handling
import java.util.ArrayList; // For list of bullets
import java.util.Iterator; // For iterating over bullets

public class Tank {
    private TankHull hull;
    private Torrent turret;
    private ArrayList<Bullet> bullets;
    private ParticleSystem particleSystem; // Add ParticleSystem

    public Tank(String hullImagePath, String turretImagePath, int startX, int startY, int width, int height) throws IOException {
        hull = new TankHull(hullImagePath, startX, startY, width, height);
        turret = new Torrent(turretImagePath, width, height);
        bullets = new ArrayList<>();
        particleSystem = new ParticleSystem(); // Initialize ParticleSystem
    }

    public void draw(Graphics g) {
        hull.draw(g);
        turret.draw(g, hull.getX() + hull.getWidth() / 2, hull.getY() + hull.getHeight() / 2);

        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        // Draw particles
        particleSystem.draw(g);
    }

    public void update(int screenWidth, int screenHeight, Gamemap gameMap) {
        hull.update();

        // Ensure the tank does not go outside the screen bounds
        if (hull.getX() < 0 || hull.getY() < 0 ||
            hull.getX() + hull.getWidth() > screenWidth || hull.getY() + hull.getHeight() > screenHeight ||
            gameMap.checkCollision(new Rectangle(hull.getX(), hull.getY(), hull.getWidth(), hull.getHeight()))) {
            hull.stop(); // Stop the tank if it collides with a wall or goes out of bounds
        }

        // Update bullets
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();

            // Remove bullets that are off-screen or collide with obstacles
            if (bullet.isOffScreen(screenWidth, screenHeight) || gameMap.checkCollision(bullet.getBounds())) {
                iterator.remove();
            }
        }

        // Update particles
        particleSystem.update();
    }

    public void fire(String bulletImagePath) {
        int bulletX = hull.getX() + hull.getWidth() / 2;
        int bulletY = hull.getY() + hull.getHeight() / 2;
        double angle = Math.toRadians(turret.angle);

        bullets.add(new Bullet(bulletX, bulletY, angle, bulletImagePath));
        System.out.println("Fired bullet from (" + bulletX + ", " + bulletY + ") at angle: " + turret.angle);

        hull.fireBullet(); // Play firing sound

        // Emit particles at the firing point
        particleSystem.emit(bulletX, bulletY);
    }

    public void move(double dx, double dy) {
        hull.move(dx, dy);
    }

    public void stop() {
        hull.stop();
    }

    public void aimAt(Point mousePosition) {
        turret.aimAt(mousePosition, hull.getX() + hull.getWidth() / 2, hull.getY() + hull.getHeight() / 2);
    }

    public TankHull getHull() {
        return hull;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets; // Expose the bullets list for collision checks
    }

    // Returns the hitbox of the tank
    public Rectangle getBounds() {
        return hull.getBounds();
    }
}
