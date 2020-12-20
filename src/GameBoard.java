import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameBoard extends JPanel {
    // creates the board panel in different stages, start, in game, pause, and game over

    private PlayerSpaceCraft player1;
    private int score;


    GameBoard(){
        setPreferredSize(new Dimension(800,800));
        setBackground(Color.BLACK);
        isFocusable();
        requestFocus();


        player1 = new PlayerSpaceCraft(new Point(200,200), new Bullet());
        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"),"left");

        //handles movement
        Action moveLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.move("left");    //call method
            }
        };
        getActionMap().put("left", moveLeft);
        getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"),"right");
        Action moveRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.move("right");
            }
        };
        getActionMap().put("right", moveRight);

        //handles shooting
        getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),"shoot");
        Action fire = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.bulletCoolDown = new Thread(player1::shoot);
                player1.bulletCoolDown.start();
            }
        };
        getActionMap().put("shoot",fire);
    }

    public void paintComponent(Graphics graphics){
        //objects will be drawn directly onto the panel, no need for JLabel
        super.paintComponent(graphics);
        try {
            drawPlayerSpaceCraft(graphics);
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    void drawPlayerSpaceCraft(Graphics graphics) throws IOException {
        int playerX = player1.getObjectLocation().x;
        int playerY = player1.getObjectLocation().y;
        graphics.drawImage(scaleImage(ImageIO.read(new File("player.png"))),playerX,playerY,null);
    }

    void drawAlienSpaceCraft(Graphics graphics){

    }


    BufferedImage scaleImage(Image image){
        Image afterScaling = image.getScaledInstance(50,50,Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
        output.createGraphics().drawImage(afterScaling,0,0,null);
        return output;
    }

}
