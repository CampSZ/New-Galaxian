import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EnemyShipWithShooting extends GameObject{
    private long lastShotTime;
    private static final long SHOT_INTERVAL = 2000;
    private int speed;
    private int targetY;
    private int initialX;
    private int oscillationDistance;
    private boolean movingRight;
    private boolean positioned;
    private long lastOscillationTime;
    private static final long PAUSE_DURATION = 500;
    private boolean pausing;
    private int shootTimer;
    private Random random;


    public EnemyShipWithShooting(int x, int y, int width, int height, int speed, int targetY, int oscillationDistance) {
        super(x, y, width, height);
        this.speed = speed;
        this.targetY = targetY;
        this.initialX = x;
        this.oscillationDistance = oscillationDistance;
        this.movingRight = true;
        this.positioned = false;
        this.lastOscillationTime = 0;
        this.pausing = false;
        this.lastShotTime = 0;
        shootTimer = 0;
        random = new Random();
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
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }
    public boolean isPositioned() {
        return positioned;
    }

    public void Enemyshoot(ArrayList<EnemyProjectile> Enemyprojectiles) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= SHOT_INTERVAL) {
            Enemyprojectiles.add(new EnemyProjectile(x + width / 2 - 2, y - 10, 4, 10, 5, 1));
            lastShotTime = currentTime;

        }
    }
    public void setShootTimer(int time) {
        shootTimer = time;
        if (shootTimer > 0) {
            setShootTimer(shootTimer-1); // Reduz o temporizador a cada atualização
        }
        if (shootTimer <= 0) {
            Enemyshoot(Game.Enemyprojectiles);



            // Define um novo tempo de tiro aleatório entre 100 e 300 milissegundos
            shootTimer = random.nextInt(201) + 100;
        }
    }

}

