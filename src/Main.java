import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    public static void main (String[] args) throws IOException {

        GameWindow gameWindow = new GameWindow();
        int actualHeight = gameWindow.getContentPane().getHeight();
        int actualWidth = gameWindow.getContentPane().getWidth();
        PlayerSpaceCraft player1 = new PlayerSpaceCraft(new Point(400,700));
        GameBoard gameBoard = new GameBoard(player1, new Dimension (actualWidth, actualHeight));
        gameWindow.add(gameBoard);
        gameWindow.revalidate();
        Main game = new Main(player1);
        game.start();

        //handles movement

        //on press
        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,false),"left");
        Action moveLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection("left");
            }
        };
        gameBoard.getActionMap().put("left", moveLeft);
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,false),"right");
        Action moveRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection("right");
            }
        };
        gameBoard.getActionMap().put("right",moveRight);

        //on release
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true),"releaseLeft");
        Action releaseLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection("");
            }
        };
        gameBoard.getActionMap().put("releaseLeft", releaseLeft);
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true),"releaseRight");
        Action releaseRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection("");
            }
        };
        gameBoard.getActionMap().put("releaseRight", releaseRight);

        //handles shooting
        gameBoard.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),"shoot");
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
        gameBoard.getActionMap().put("shoot",fire);

        // windows and other initializations can take place in different functions

    }

    private Thread gameLoop;
    private boolean running = false;
    private GameStatus gameStatus = GameStatus.start;
    private final PlayerSpaceCraft player1;

    Main(PlayerSpaceCraft player1){
        this.player1 = player1;
    }

    private synchronized void start(){
        if(running){
            return;
        }
        running = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private synchronized void stop(){
        if (!running){
            return;
        }
        running = false;
        try {
            gameLoop.join();    //joins all the threads and wait for them to die
        }catch (InterruptedException exception){
            exception.printStackTrace();
        }
        System.exit(1);
    }

    public enum GameStatus{
        start, inGame, paused, gameOver
    }

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

            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + " Ticks, Fps " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();

    }

    void tick(){
        //everything that updates
        player1.move();
        if (player1.getBullet() != null){
            player1.getBullet().move();
        }
        //figure out how to return the velocity to 0

    }

    void render(){
        //everything that renders

    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }
}
