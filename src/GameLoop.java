import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameLoop implements Runnable {

    /*the game loop
     * Handles the game status
     * Needs to receive status update from the game loop
     */

    PlayerSpaceCraft player1;
    AlienSpaceCraft alien1;

    public enum GameStatus{
        start, inGame, paused, gameOver
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private GameStatus gameStatus = GameStatus.start;


    public void run(){

        //while (state from main RUNNING) = true
        //this should be where everything comes together
        while (gameStatus == GameStatus.inGame) {
            update();

        }

    }


    void update(){



    }

    public static void main (String[] args){

        GameLoop gameLoop = new GameLoop();

        switch (gameLoop.gameStatus) {  //only the inGame status will have its own thread

            case inGame:
            Thread gameThread = new Thread(new GameLoop());
            gameThread.start();
        }

    }

}
