import java.awt.*;
import java.util.Random;

public class Particle {
    private double x, y;
    private double xVel, yVel;
    private double lifespan; // Lifespan in milliseconds
    private long creationTime;

    public Particle(double x, double y) {
        this.x = x;
        this.y = y;

        Random random = new Random();
        xVel = (random.nextDouble() - 0.5) * 4; // Random velocity
        yVel = (random.nextDouble() - 0.5) * 4;
        lifespan = 500; // Particles last 500ms
        creationTime = System.currentTimeMillis();
    }

    public void update() {
        x += xVel;
        y += yVel;
    }

    public boolean isDead() {
        return System.currentTimeMillis() - creationTime > lifespan;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int) x, (int) y, 4, 4); // Small circular particles
    }
}
