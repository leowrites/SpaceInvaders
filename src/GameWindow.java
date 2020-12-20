import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    //Where the frame is created
    GameWindow(){
        super("Space Invaders By Leo");
        setSize(new Dimension(500,500));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
