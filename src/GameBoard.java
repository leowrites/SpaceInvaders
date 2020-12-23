import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameBoard extends JPanel {
    // creates the board panel in different stages, start, in game, pause, and game over

    private PlayerSpaceCraft player1;
    private int score;


    GameBoard(PlayerSpaceCraft spaceCraft, Dimension size){
        setPreferredSize(size);
        setBackground(Color.BLACK);
        isFocusable();
        requestFocus();

        this.player1 = spaceCraft;
    }

    public void paintComponent(Graphics graphics){
        //objects will be drawn directly onto the panel, no need for JLabel
        super.paintComponent(graphics);
        try {
            drawPlayerSpaceCraft(graphics);
            drawBullet(graphics);
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    void drawPlayerSpaceCraft(Graphics graphics) throws IOException {
        int playerX = player1.getObjectLocation().x;
        int playerY = player1.getObjectLocation().y;
        BufferedImage scaledImage = scaleImage(player1.getImage(),50,50);
        graphics.drawImage(scaledImage,playerX,playerY,null);
    }

    void drawAlienSpaceCraft(Graphics graphics){

    }

    void drawBullet(Graphics graphics){
        if (player1.getBullet() != null) {
            int bulletX = player1.getBullet().getObjectLocation().x;
            int bulletY = player1.getBullet().getObjectLocation().y;
            BufferedImage scaledImage = scaleImage(player1.getBullet().getImage(), 30,30);
            graphics.drawImage(scaledImage, bulletX, bulletY, null);
        }
    }


    BufferedImage scaleImage(Image image, int targetWidth, int targetHeight){
        Image afterScaling = image.getScaledInstance(targetWidth,targetHeight,Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(targetWidth,targetHeight,BufferedImage.TYPE_INT_RGB);
        output.createGraphics().drawImage(afterScaling,0,0,null);
        return output;
    }

}
