import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bullet {

    //Has a y velocity
    //This location depends on player coordinate
    //Has a move method
    //in the player class, if bullet "hits" a space craft, the spacecraft dies
    //if a bullet hits a bullet, it cancels out

    Bullet(Point spaceCraftLocation) throws IOException {
        this.objectLocation = spaceCraftLocation;
    }
    private Point objectLocation;

    private Image image = ImageIO.read(new File("bullet.png"));

    void move(){
        int velocityY = 3;
        objectLocation.y -= velocityY;
    }

    public Point getObjectLocation() {
        return objectLocation;
    }

    public Image getImage() {
        return image;
    }

}
