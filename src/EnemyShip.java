import java.awt.*;

public class EnemyShip extends GameObject {
    private int speed;
    private int targetY;
    private int initialX;
    private int oscillationDistance;
    private boolean movingRight;
    private boolean positioned;
    private long lastOscillationTime;
    private static final long PAUSE_DURATION = 500;
    private boolean pausing;

    public EnemyShip(int x, int y, int width, int height, int speed, int targetY, int oscillationDistance) {
        super(x, y, width, height);
        this.speed = speed;
        this.targetY = targetY;
        this.initialX = x;
        this.oscillationDistance = oscillationDistance;
        this.movingRight = true;
        this.positioned = false;
        this.lastOscillationTime = 0;
        this.pausing = false;
    }

    public void update(boolean startOscillation) {
        if (!positioned && y < targetY) {
            y += speed;
            if (y >= targetY) {
                y = targetY;
                positioned = true;
            }
        }

        if (positioned && startOscillation) {
            long currentTime = System.currentTimeMillis();

            if (pausing) {
                if (currentTime - lastOscillationTime >= PAUSE_DURATION) {
                    pausing = false;
                    lastOscillationTime = currentTime;
                }
            } else {
                if (movingRight) {
                    x += speed / 2;
                    if (x > initialX + oscillationDistance) {
                        movingRight = false;
                        pausing = true;
                        lastOscillationTime = currentTime;
                    }
                } else {
                    x -= speed / 2;
                    if (x < initialX - oscillationDistance) {
                        movingRight = true;
                        pausing = true;
                        lastOscillationTime = currentTime;
                    }
                }
            }
        }
    }


    @Override
    public void update() {

    }

    public void render(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(x, y, width, height);
    }
    public boolean isPositioned() {
        return positioned;
    }
}