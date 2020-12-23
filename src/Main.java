import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Main implements Runnable {

    /*the game loop
     * Handles the game status
     * Needs to receive status update from the game loop
     * Does not handle adding or removing panels from the window
     *
     * Pass in spaceship to component that needs to see them
     * Keep them at a high level
     * Responding to key strokes
     * Make an object higher up so you can pass it in
     * Getters
     */

    public static void main(String[] args) throws IOException {

        int maximumAlienCapacity = 50;
        ArrayList<AlienSpaceCraft> alienSpaceCrafts = new ArrayList<>();
        int objectsInRow = 0;
        int rowCount = 0;
        for (int i = 0; i < maximumAlienCapacity; i++) {
            alienSpaceCrafts.add(new AlienSpaceCraft(new Point(20 + 60 * objectsInRow, 20 + 50 * rowCount)));
            objectsInRow++;
            if (objectsInRow == 10) {
                objectsInRow = 0;
                rowCount++;
            }
        }
        GameWindow gameWindow = new GameWindow();
        int actualHeight = gameWindow.getContentPane().getHeight();
        int actualWidth = gameWindow.getContentPane().getWidth();
        PlayerSpaceCraft player1 = new PlayerSpaceCraft(new Point(400, 700));
        GameBoard gameBoard = new GameBoard(player1, new Dimension(actualWidth, actualHeight), alienSpaceCrafts);
        gameWindow.add(gameBoard);
        gameWindow.revalidate();
        Main game = new Main(player1, alienSpaceCrafts, gameBoard);
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

    Main(PlayerSpaceCraft player1, ArrayList<AlienSpaceCraft> alienSpaceCrafts, GameBoard gameBoard) {
        this.player1 = player1;
        this.alienSpaceCrafts = alienSpaceCrafts;
        this.gameBoard = gameBoard;
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

    long lastMoved;
    public void run() {
        //gets run once
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;  //Update 60 times per second, or 60FPS
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;   //calculates the time passed to catch itself up
        int updates = 0;
        int frames = 0;
        long timer = lastMoved = System.currentTimeMillis();
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
    void tick() {
        //everything that updates
        player1.move();
        if (player1.getBullets() != null) {
            for (int i = player1.getBullets().size() - 1; i > 0; i--) {
                Bullet thisBullet = player1.getBullets().get(i);
                if (thisBullet.isHit()) {
                    player1.getBullets().remove(thisBullet);
                }
                thisBullet.move();
            }
        }
        int max = alienSpaceCrafts.size();
        if (System.currentTimeMillis() - lastMoved  >= 500) {
            System.out.println("Waited 1 second");
            lastMoved = System.currentTimeMillis();
            for (int i = max - 1; i >= 0; i--) {
                AlienSpaceCraft alienSpaceCraft = alienSpaceCrafts.get(i);
                alienSpaceCraft.move(currentRow % 2 == 0);
                if (alienSpaceCraft.isMoveDown()) {
                    for (AlienSpaceCraft alienSpaceCraft1 : alienSpaceCrafts) {
                        alienSpaceCraft1.getObjectLocation().y += 20;
                        alienSpaceCraft1.setMoveDown(false);
                    }
                    currentRow++;
                }
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
