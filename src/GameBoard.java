import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class GameBoard extends JPanel {
    // creates the board panel in different stages, start, in game, pause, and game over
    GameBoard(){
        setPreferredSize(new Dimension(800,800));
        setBackground(Color.BLACK);
        isFocusable();
        requestFocus();

        PlayerSpaceCraft player1 = new PlayerSpaceCraft(new Point(200,200), new Bullet());
        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        getInputMap(IFW).put(KeyStroke.getKeyStroke("Left"),"left");

        //handles movement
        Action moveLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.move("left");    //call method
            }
        };
        getActionMap().put("left", moveLeft);
        getInputMap(IFW).put(KeyStroke.getKeyStroke("Right"),"right");
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


    public void paintComponent(Graphics graphics, Image image, int x, int y){
        super.paintComponent(graphics);

        repaint();

    }

    void drawPlayerSpaceCraft(){


    }

    BufferedImage scaleImage(Image image){
        Image afterScaling = image.getScaledInstance(20,20,Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
        output.createGraphics().drawImage(afterScaling,0,0,null);
        return output;
    }

}
