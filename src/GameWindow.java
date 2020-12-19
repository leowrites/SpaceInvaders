import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    //Where the frame is created
    GameWindow(){
        setPreferredSize(new Dimension(500,500));
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
