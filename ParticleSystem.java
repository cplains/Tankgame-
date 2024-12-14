import java.awt.*;
import java.util.ArrayList;

public class ParticleSystem {
    private ArrayList<Particle> particles;

    public ParticleSystem() {
        particles = new ArrayList<>();
    }

    public void emit(double x, double y) {
        for (int i = 0; i < 10; i++) { // Emit 10 particles
            particles.add(new Particle(x, y));
        }
    }

    public void update() {
        particles.removeIf(Particle::isDead);
        for (Particle particle : particles) {
            particle.update();
        }
    }

    public void draw(Graphics g) {
        for (Particle particle : particles) {
            particle.draw(g);
        }
    }
}
