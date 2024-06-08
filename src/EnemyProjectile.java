import java.awt.*;

public class EnemyProjectile extends Projectile{

    private int speed;

    private int directionY;

    public EnemyProjectile(int x, int y, int width, int height, int speed, int directionY) {
        super(x,y,width,height,speed,directionY);
        this.speed = speed;
        this.directionY = directionY;


    }

    public void update() {
        y += directionY * speed;
    }

    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }
}