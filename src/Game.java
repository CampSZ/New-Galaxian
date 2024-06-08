import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Game extends JPanel implements ActionListener {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Galaxian");
        Game game = new Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static final long serialVersionUID = 1L;
    private ArrayList<EnemyShip> enemies;
    private ArrayList<EnemyShipWithShooting> shipWithShootings;
    private Ship player;
    private ArrayList<Ship> ship;
    static ArrayList<Projectile> projectiles;
    static ArrayList<EnemyProjectile> Enemyprojectiles;
    private Timer timer;
    private int score;
    private boolean allEnemiesPositioned;

    public Game() {
        player = new Ship(375, 500, 30, 30, 5);
        ship = new ArrayList<>();
        enemies = new ArrayList<>();
        shipWithShootings = new ArrayList<>();
        projectiles = new ArrayList<>();
        Enemyprojectiles = new ArrayList<>();
        timer = new Timer(16, this);
        timer.start();
        initializeEnemies();

        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        setBackground(Color.BLACK);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    player.setMovingLeft(true);
                } else if (key == KeyEvent.VK_RIGHT) {
                    player.setMovingRight(true);
                } else if (key == KeyEvent.VK_SPACE) {
                    player.shoot(projectiles);
                    Sound.playSound("Shoot.wav");
                }
            }

            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    player.setMovingLeft(false);
                } else if (key == KeyEvent.VK_RIGHT) {
                    player.setMovingRight(false);
                }
            }
        });
    }

    private void initializeShootingEnemies() {
        shipWithShootings.clear();
        int enemyShootingWidth = 20;
        int enemyShootingHeight = 20;
        int EninitialX = 413;
        int EninitialY = -50;
        int EtargetYStart = 50;
        int Egap = 30;
        int EoscillationDistance = 100;
        int EnumRows = 3;

        Random random = new Random();
        for (EnemyShipWithShooting shenemy : shipWithShootings) {
            // Define um tempo aleatório para o próximo tiro entre 100 e 300 milissegundos
            shenemy.setShootTimer(1);
            System.out.println("a");
        }

        for (int row = 0; row < EnumRows; row++) {
            int enemiesInRow = EnumRows - row;
            for (int col = 0; col < enemiesInRow; col++) {
                int x = EninitialX - (enemiesInRow * (enemyShootingHeight + Egap) / 2) + col * (enemyShootingWidth + Egap);
                int y = EninitialY;
                int targetY = EtargetYStart + row * (enemyShootingHeight + Egap);
                shipWithShootings.add(new EnemyShipWithShooting(x, y, enemyShootingWidth, enemyShootingHeight, 2, targetY, EoscillationDistance));
            }
        }

        allEnemiesPositioned = false;
    }

    private void initializeEnemies() {
        enemies.clear();
        int enemyWidth = 20;
        int enemyHeight = 20;
        int initialX = 400;
        int initialY = -50;
        int targetYStart = 50;
        int gap = 5;
        int oscillationDistance = 100;
        int numRows = 5;

        for (int row = 0; row < numRows; row++) {
            int enemiesInRow = numRows - row;
            for (int col = 0; col < enemiesInRow; col++) {
                int x = initialX - (enemiesInRow * (enemyWidth + gap) / 2) + col * (enemyWidth + gap);
                int y = initialY;
                int targetY = targetYStart + row * (enemyHeight + gap);
                enemies.add(new EnemyShip(x, y, enemyWidth, enemyHeight, 2, targetY, oscillationDistance));
            }
        }

        allEnemiesPositioned = false;
    }


    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    public void update() {
        player.update();

        boolean allPositioned = true;
        for (EnemyShip enemy : enemies) {
            enemy.update(allEnemiesPositioned);
            if (!enemy.isPositioned()) {
                allPositioned = false;
            }
        }

        if (allPositioned) {
            allEnemiesPositioned = true;
        }

        for (Projectile projectile : projectiles) {
            projectile.update();
        }


        if (enemies.isEmpty()) {
            initializeEnemies();
        }

        boolean allEPositioned = true;
        for (EnemyShipWithShooting enemy : shipWithShootings) {
            enemy.update(allEnemiesPositioned);
            if (!enemy.isPositioned()) {
                allEPositioned = false;

            }
        }

        if (allEPositioned) {
            allEnemiesPositioned = true;

        }

        for (EnemyProjectile Enemyprojectiles : Enemyprojectiles) {
            Enemyprojectiles.update();

        }

        if (shipWithShootings.isEmpty()) {
            initializeShootingEnemies();

        }

        for (EnemyShipWithShooting shenemy : shipWithShootings) {
            //Define um tempo aleatório para o próximo tiro entre 100 e 300 milissegundos
            shenemy.setShootTimer(1);

        }
        checkCollisions();
    }






    public void checkCollisions() {
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        ArrayList<EnemyShip> enemiesToRemove = new ArrayList<>();
        ArrayList<EnemyShipWithShooting> shenemiesToRemove = new ArrayList<>();
        ArrayList<Ship> shipsToRemove = new ArrayList<>();
        ArrayList<EnemyProjectile> EnemyprojectilesToRemove = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            for (EnemyShip enemy : enemies) {
                if (projectile.x < enemy.x + enemy.width && projectile.x + projectile.width > enemy.x &&
                        projectile.y < enemy.y + enemy.height && projectile.y + projectile.height > enemy.y) {
                    projectilesToRemove.add(projectile);
                    enemiesToRemove.add(enemy);
                    score += 10;
                    Sound.playSound("Exploson.wav");
                }
            }
            for (EnemyShipWithShooting shenemy : shipWithShootings) {
                if (projectile.x < shenemy.x + shenemy.width && projectile.x + projectile.width > shenemy.x &&
                        projectile.y < shenemy.y + shenemy.height && projectile.y + projectile.height > shenemy.y) {
                    projectilesToRemove.add(projectile);
                    shenemiesToRemove.add(shenemy);
                    score += 10;
                    Sound.playSound("Exploson.wav");
                }
            }
        }
        for (EnemyProjectile Enemyprojectile : Enemyprojectiles) {
            System.out.println("teste1");
            for (Ship ship : ship) {
                System.out.println("teste2");
                if (Enemyprojectile.x < ship.x + ship.width && Enemyprojectile.x + Enemyprojectile.width > ship.x &&
                        Enemyprojectile.y < ship.y + ship.height && Enemyprojectile.y + Enemyprojectile.height > ship.y){
                    EnemyprojectilesToRemove.add(Enemyprojectile);
                    shipsToRemove.add(ship);
                    System.out.println("teste3");
                }

            }

        }

        projectiles.removeAll(projectilesToRemove);
        enemies.removeAll(enemiesToRemove);
        shipWithShootings.removeAll(shenemiesToRemove);
        ship.removeAll(shipsToRemove);
        Enemyprojectiles.removeAll(EnemyprojectilesToRemove);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.render(g);
        for (EnemyShip enemy : enemies) {
            enemy.render(g);
        }
        for (EnemyShipWithShooting shenemy : shipWithShootings) {
            shenemy.render(g);
        }
        for (Projectile projectile : projectiles) {
            projectile.render(g);
        }
        for(EnemyProjectile Enemyprojectile:Enemyprojectiles){
            Enemyprojectile.render(g);
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);
    }


    class Sound {
        public static void playSound(String soundFile) {
            try {
                String soundPath = "C:\\Users\\rober\\Dropbox\\PC\\Desktop\\New Galaxian\\Sounds\\" + soundFile;
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundPath));
                Clip clip = AudioSystem.getClip();

                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}