import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bullet {

    //Has a y velocity
    //This location depends on player coordinate
    //Has a move method
    //in the player class, if bullet "hits" a space craft, the spacecraft dies
    //if a bullet hits a bullet, it cancels out

    private String owner;
    private final Point objectLocation;
    private boolean hit = false;
    private final BufferedImage bulletImage = scaleImage(ImageIO.read(new File("bullet.png")));

    Bullet(Point spaceCraftLocation, String owner) throws IOException {
        this.objectLocation = spaceCraftLocation;
        this.owner = owner;
    }

    void move() {
        int velocityY = 5;
        objectLocation.y -= velocityY;
        if (objectLocation.y <= 0) {
            hit = true;
        }
    }

    BufferedImage scaleImage(Image image) {
        Image afterScaling = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        output.createGraphics().drawImage(afterScaling, 0, 0, null);
        return output;
    }

    public BufferedImage getBulletImage() {
        return bulletImage;
    }

    public Point getObjectLocation() {
        return objectLocation;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isHit() {
        return hit;
    }
}
