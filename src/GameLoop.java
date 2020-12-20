
public class GameLoop implements Runnable {

    /*the game loop
     * Handles the game status
     * Needs to receive status update from the game loop
     * Does not handle adding or removing panels from the window
     */

    private GameWindow gameWindow = new GameWindow();
    private GameBoard gameBoard = new GameBoard();
    public enum GameStatus{
        start, inGame, paused, gameOver
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }

    private GameStatus gameStatus = GameStatus.start;


    public void run(){

        gameWindow.add(gameBoard);
        gameWindow.revalidate();

        while (gameStatus == GameStatus.inGame) {
            update();

        }

    }


    void update(){



    }

    public static void main (String[] args){

        // windows and other initializations can take place in different functions

        GameLoop gameLoop = new GameLoop();
        gameLoop.setGameStatus(GameStatus.inGame);
        switch (gameLoop.gameStatus) {  //only the inGame status will have its own thread
            case start:
            case inGame:
                Thread gameThread = new Thread(gameLoop);
                gameThread.start();
            case paused:
            case gameOver:
        }

    }

}
