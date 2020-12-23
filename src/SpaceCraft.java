import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SpaceCraft {
    //An abstract class for all types of space crafts

    //move function takes in a location and returns a location
    void shoot() throws IOException {
    }

    void hitCheck() {
        //a universal method for checking


    }

}

class PlayerSpaceCraft extends SpaceCraft{

    private final Point objectLocation;
    private final Image image = ImageIO.read(new File("player.png"));

    PlayerSpaceCraft(Point spawnLocation) throws IOException {
        super();
        this.objectLocation = spawnLocation;
        //construct a space craft object with all its attributes

    }

    private boolean right = false;
    private boolean left = false;
    void move() {
        /*
        There was a lag when changing directions, for example when changing direction from left to right
        the space craft would stop for 1 second and then move. This was fixed by using a boolean, and set
        that direction to false when the key is released
         */
        int velocityX = 0;
        if (right) {
            if (objectLocation.x < 750) {
                    velocityX = 5;
            }else {
                objectLocation.x = 750;
            }
            objectLocation.x += velocityX;
        }
        else if (left) {
            if (objectLocation.x > 0) {
                    velocityX = -5;

            }else {
                objectLocation.x = 0;
            }
            objectLocation.x += velocityX;
        }
    }

    private final ArrayList<Bullet> bullets = new ArrayList<>();

    void shoot() throws IOException {

        //fires first, then go to cool down.
        //when in cool down, spacecraft cannot fire.
        //user can fire 5 bullets
        int maximumAmountOfBullets = 5;
        if (bullets.size() - 1 < maximumAmountOfBullets){
            Point currentPosition = new Point(objectLocation.x, objectLocation.y);
            Bullet bullet = new Bullet(currentPosition, "player");
            bullets.add(bullet);
           System.out.println("Bullet Fired!");
        }
    }

    void hitCheck() {
    }

    public Point getObjectLocation() {
        return objectLocation;
    }

    public Image getImage(){
        return image;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
}

class AlienSpaceCraft extends SpaceCraft {

    private final Point objectLocation;
    private boolean moveDown;

    AlienSpaceCraft(Point spawnLocation) throws IOException {
        super();
        this.objectLocation = spawnLocation;
    }
    private final Image image = ImageIO.read(new File("enemy.png"));

    public void move(boolean right) {
        if (right) {
            objectLocation.x += 1;
            if (objectLocation.x > 700) {
                objectLocation.x = 700;
                moveDown = true;
            }
        } else {
            objectLocation.x -= 1;
            if (objectLocation.x < 0) {
                objectLocation.x = 0;
                moveDown = true;
            }
        }
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }
    public boolean isMoveDown() {
        return moveDown;
    }
    public Point getObjectLocation() {
        return objectLocation;
    }
    public Image getImage() {
        return image;
    }
}