import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    //Where the frame is created
    GameWindow(){
        super("Space Invaders By Leo");
        setPreferredSize(new Dimension(800,800));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
