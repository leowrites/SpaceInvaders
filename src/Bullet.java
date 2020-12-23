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

    Bullet(Point spaceCraftLocation, String owner) throws IOException {
        this.objectLocation = spaceCraftLocation;
    }

    private final Point objectLocation;
    private final Image image = ImageIO.read(new File("bullet.png"));
    private boolean hit = false;

    void move(){
        int velocityY = 5;
        objectLocation.y -= velocityY;
        if (objectLocation.y <= 0){
            hit = true;
        }
    }

    public Point getObjectLocation() {
        return objectLocation;
    }

    public Image getImage() {
        return image;
    }

    public boolean isHit() {
        return hit;
    }
}
