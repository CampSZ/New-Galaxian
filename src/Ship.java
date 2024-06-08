import java.awt.Graphics;
import java.util.ArrayList;

public class Ship extends GameObject {
    private int speed;
    private boolean movingLeft;
    private boolean movingRight;
    private long lastShotTime;
    private int life;
    private static final long SHOT_INTERVAL = 500;

    public Ship(int x, int y, int width, int height, int speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.movingLeft = false;
        this.movingRight = false;
        this.lastShotTime = 0;
        this.life = 100;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void update() {
        if (movingLeft) {
            x -= speed;
        }
        if (movingRight) {
            x += speed;
        }
    }
    public void receiveDamage(int damage) {
        this.life -= damage;
    }

    public int getLife() {
        return this.life;
    }

    public void render(Graphics g) {
        g.fillRect(x, y, width, height);
    }

    public void shoot(ArrayList<Projectile> projectiles) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOT_INTERVAL) {
            projectiles.add(new Projectile(x + width / 2 - 2, y - 10, 4, 10, 10, -1));  // Define a direção vertical como para cima (-1)
            lastShotTime = currentTime;

        }
    }
}

