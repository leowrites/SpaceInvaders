import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main implements Runnable {

    /*the game loop
     * Handles the game status and updates
     * TODO: fix enemy bullet counting and explosion animation delay
     */

    public static void main(String[] args) throws IOException {

        int maximumAlienCapacity = 50;
        ArrayList<AlienSpaceCraft> alienSpaceCrafts = new ArrayList<>();
        HashMap<Integer,Integer> alienLocationMap = new HashMap<Integer,Integer>();
        int columnCount = 0;
        int rowCount = 0;
        for (int i = 0; i < maximumAlienCapacity; i++) {
            alienSpaceCrafts.add(new AlienSpaceCraft(new Point(20 + 60 * columnCount, 20 + 50 * rowCount),columnCount,rowCount));
            columnCount++;
            if (columnCount == 10) {
                columnCount = 0;
                rowCount++;
            }
            alienLocationMap.put(columnCount,rowCount);
        }
        GameWindow gameWindow = new GameWindow();
        int actualHeight = gameWindow.getContentPane().getHeight();
        int actualWidth = gameWindow.getContentPane().getWidth();
        PlayerSpaceCraft player1 = new PlayerSpaceCraft(new Point(400, 700));
        GameBoard gameBoard = new GameBoard(player1, new Dimension(actualWidth, actualHeight), alienSpaceCrafts);
        gameWindow.add(gameBoard);
        gameWindow.revalidate();
        Main game = new Main(player1, alienSpaceCrafts, gameBoard, alienLocationMap);
        game.start();

        //handles movement

        //on press
        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left");
        Action moveLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setLeft(true);
            }
        };
        gameBoard.getActionMap().put("left", moveLeft);
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right");
        Action moveRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setRight(true);
            }
        };
        gameBoard.getActionMap().put("right", moveRight);

        //on release
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "releaseLeft");
        Action releaseLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setLeft(false);
            }
        };
        gameBoard.getActionMap().put("releaseLeft", releaseLeft);
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "releaseRight");
        Action releaseRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setRight(false);
            }
        };
        gameBoard.getActionMap().put("releaseRight", releaseRight);

        //handles shooting
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"), "shoot");
        Action fire = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    player1.shoot();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        };
        gameBoard.getActionMap().put("shoot", fire);

    }


    private Thread gameLoop;
    private boolean running = false;
    private GameStatus gameStatus = GameStatus.start;
    private final PlayerSpaceCraft player1;
    private final ArrayList<AlienSpaceCraft> alienSpaceCrafts;
    private final GameBoard gameBoard;
    private HashMap<Integer,Integer> alienLocationMap = new HashMap<Integer,Integer>();

    Main(PlayerSpaceCraft player1, ArrayList<AlienSpaceCraft> alienSpaceCrafts, GameBoard gameBoard, HashMap<Integer,Integer> alienLocationMap) {
        this.player1 = player1;
        this.alienSpaceCrafts = alienSpaceCrafts;
        this.gameBoard = gameBoard;
        this.alienLocationMap = alienLocationMap;
    }

    private synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            gameLoop.join();    //joins all the threads and wait for them to die
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        System.exit(1);
    }

    public enum GameStatus {
        start, inGame, paused, gameOver
    }

    private long lastMoved;
    public void run() {
        //gets run once
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;  //Update 60 times per second, or 60FPS
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;   //calculates the time passed to catch itself up
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            //You want a delayed refresh rate so it's not refreshing a billion times per second
            //running at the frame rate
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                delta--;
                updates++;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + " Ticks, FPS " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();

    }

    int currentRow = 0;
    ArrayList<Bullet> enemyBullets = new ArrayList<>();
    int counter;
    void tick() {
        counter = 0;
        //everything that updates
        player1.move();
        playerBulletMoving();
        processAlienHit();
        alienMoveAndShoot();
        processPlayerHealth();
        alienBulletMovement();
    }

    public void processPlayerHealth(){
        player1.hitCheck(enemyBullets);
        if (player1.isHit()){
            //if player is hit
            player1.kill();
            //player 1 is killed
        }
    }

    public void playerBulletMoving(){
        if (player1.getBullets() != null) {
            for (int i = player1.getBullets().size() - 1; i > 0; i--) {
                Bullet thisBullet = player1.getBullets().get(i);
                if (thisBullet.getObjectLocation().y <= 0){
                    thisBullet.setHit(true);
                }
                if (thisBullet.isHit()) {
                    player1.getBullets().remove(thisBullet);
                }
                thisBullet.move(-5);
                System.out.println("Bullet position " + thisBullet.getObjectLocation().y);
            }
        }
    }

    public void alienMoveAndShoot(){
        int max = alienSpaceCrafts.size();
        if (System.currentTimeMillis() - lastMoved  >= 600) {
            lastMoved = System.currentTimeMillis();
            for (int i = max - 1; i >= 0; i--) {
                AlienSpaceCraft alienSpaceCraft = alienSpaceCrafts.get(i);
                alienSpaceCraft.move(currentRow % 2 == 0);
                //if current column returns a row that is the same as the spacecraft,
                //indicating this is the closest in that row to the player, then the spacecraft shoots
                if (alienLocationMap.get(alienSpaceCraft.getColumn()) == alienSpaceCraft.getRow()){
                try {
                    Random fireFactor = new Random();
                    //there is a 1 in 10 chance to fire
                    if (fireFactor.nextInt(5) == 0) {
                        //calls shoot
                        if (alienSpaceCraft.getBullet() == null) {
                            alienSpaceCraft.shoot();
                            enemyBullets.add(alienSpaceCraft.getBullet());
                        }
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
                if (alienSpaceCraft.isMoveDown()) {
                    //if it's time to move down
                    for (AlienSpaceCraft alienSpaceCraft1 : alienSpaceCrafts) {
                        alienSpaceCraft1.getObjectLocation().y += 20;
                        alienSpaceCraft1.setMoveDown(false);
                    }
                    currentRow++;
                }
            }
        }
    }

    public void alienBulletMovement(){
        //only shoot if there is no alien in front
        for (AlienSpaceCraft alienSpaceCraft : alienSpaceCrafts) {
            //moving the bullet
            if (alienSpaceCraft!=null) {
                Bullet thisBullet = alienSpaceCraft.getBullet();
                if (thisBullet != null) {
                    int y = alienSpaceCraft.getBullet().getObjectLocation().y;
                    thisBullet.move(5);
                    if ( y >= 800) {
                        alienSpaceCraft.getBullet().setHit(true);
                        alienSpaceCraft.setBullet(null);
                        enemyBullets.remove(thisBullet);
                    } else if (thisBullet.isHit()) {
                        alienSpaceCraft.setBullet(null);
                        enemyBullets.remove(thisBullet);
                    }
                }
            }
        }
    }

    public void processAlienHit(){
        for (int i = alienSpaceCrafts.size() - 1; i >= 0 ; i--) {
            AlienSpaceCraft alienSpaceCraft = alienSpaceCrafts.get(i);
            alienSpaceCraft.hitCheck(player1.getBullets());
            if (alienSpaceCraft.isHit()) {
                //if the space craft is hit, kill and remove bullet
                alienSpaceCraft.kill();
                alienSpaceCrafts.remove(alienSpaceCraft);
                int currentRow = alienLocationMap.get(alienSpaceCraft.getColumn());
                alienLocationMap.put(alienSpaceCraft.getColumn(), currentRow - 1);
            }
        }
    }

    public void render() {
        gameBoard.repaint();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
