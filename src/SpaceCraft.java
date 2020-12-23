import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class SpaceCraft {
    //An abstract class for all types of space crafts

    SpaceCraft(Point spawnLocation) {
    }

    //move function takes in a location and returns a location
    void shoot() throws IOException {
    }

    void hitCheck() {
    }

}

class PlayerSpaceCraft extends SpaceCraft{

    private Point objectLocation;
    private int velocityX = 0;
    private String direction = "";
    private Image image = ImageIO.read(new File("player.png"));
    private boolean resetting = false;

    PlayerSpaceCraft(Point spawnLocation) throws IOException {

        super(spawnLocation);

        this.objectLocation = spawnLocation;
        //construct a space craft object with all its attributes

    }

    void move() {

        if (direction.equals("right")) {
            if (objectLocation.x < 750) {
                if (velocityX < 10) {
                    velocityX = 5;
                    System.out.println("Current Velocity" + velocityX);
                }
            } else {
                velocityX = 0;
                objectLocation.x = 750;
            }
            objectLocation.x += velocityX;
        } else if (direction.equals("left")) {
            if (objectLocation.x > 0) {
                if (velocityX > -10) {
                    velocityX = -5;

                    System.out.println("Current Velocity" + velocityX);
                }
            } else {
                velocityX = 0;
                objectLocation.x = 0;
            }
            objectLocation.x += velocityX;
        }else{
           velocityX = 0;
        }

    }

    private long lastFired = 0;
    private Bullet bullet;

    void shoot() throws IOException {

        //fires first, then go to cool down.
        //when in cool down, spacecraft cannot fire.

        if (System.currentTimeMillis() - lastFired >= 500) {
            Point currentPosition = new Point(objectLocation.x, objectLocation.y);
            bullet = new Bullet(currentPosition);
            bullet.move();
            System.out.println("Bullet Fired!");
            lastFired = System.currentTimeMillis();
        } else {
            System.out.println("In cool down!");
        }
    }

    public Bullet getBullet() {
        return bullet;
    }

    void hitCheck() {


    }

    public Point getObjectLocation() {
        return objectLocation;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Image getImage(){
        return image;
    }

    public boolean isResetting() {
        return resetting;
    }

    public void setResetting(boolean resetting) {
        this.resetting = resetting;
    }


}

class AlienSpaceCraft extends SpaceCraft {


    AlienSpaceCraft(Point spawnLocation) {
        super(spawnLocation);
    }
}