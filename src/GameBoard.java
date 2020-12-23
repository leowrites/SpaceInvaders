import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameBoard extends JPanel {
    // creates the board panel in different stages, start, in game, pause, and game over

    private final PlayerSpaceCraft player1;
    private ArrayList<AlienSpaceCraft> alienSpaceCrafts;

    GameBoard(PlayerSpaceCraft spaceCraft, Dimension size, ArrayList<AlienSpaceCraft> alienSpaceCrafts) {
        setPreferredSize(size);
        setBackground(Color.BLACK);
        isFocusable();
        requestFocus();
        //need to scale the images

        this.alienSpaceCrafts = alienSpaceCrafts;
        this.player1 = spaceCraft;
    }


    public void paintComponent(Graphics graphics) {
        //objects will be drawn directly onto the panel, no need for JLabel
        super.paintComponent(graphics);
        try {
            drawPlayerSpaceCraft(graphics);
            drawAlienSpaceCraft(graphics);
            drawBullet(graphics);
        } catch (IOException e) {
            e.printStackTrace();
        }
        graphics.dispose();
    }

    void drawPlayerSpaceCraft(Graphics graphics) throws IOException {
        int playerX = player1.getObjectLocation().x;
        int playerY = player1.getObjectLocation().y;
        graphics.drawImage(player1.getPlayerSpaceCraftImage(), playerX, playerY, null);
    }

    void drawAlienSpaceCraft(Graphics graphics) {
        for (AlienSpaceCraft alienSpaceCraft : alienSpaceCrafts) {
            int alienX = alienSpaceCraft.getObjectLocation().x;
            int alienY = alienSpaceCraft.getObjectLocation().y;
            graphics.drawImage(alienSpaceCraft.getAlienSpaceCraftImage(), alienX, alienY, null);
        }
    }

    void drawBullet(Graphics graphics) {
        if (player1.getBullets() != null) {
            for (int i = player1.getBullets().size() - 1; i > 0; i--) {
                Bullet thisBullet = player1.getBullets().get(i);
                int bulletX = thisBullet.getObjectLocation().x;
                int bulletY = thisBullet.getObjectLocation().y;
                graphics.drawImage(thisBullet.getBulletImage(), bulletX, bulletY, null);
            }
        }
    }

}
