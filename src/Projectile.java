import java.awt.*;

public class Projectile extends GameObject {
    private int speed;
    private int directionY;

    public Projectile(int x, int y, int width, int height, int speed, int directionY) {
        super(x, y, width, height);
        this.speed = speed;
        this.directionY = directionY;
    }

    public void update() {
        y += directionY * speed;
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }
}