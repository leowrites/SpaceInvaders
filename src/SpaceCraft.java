import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public abstract class SpaceCraft{
    //An abstract class for all types of space crafts

    SpaceCraft(Point spawnLocation, Bullet bullet){}
  //move function takes in a location and returns a location
    void shoot(){}
    void hitCheck(){}

}

class PlayerSpaceCraft extends SpaceCraft{

    private Point objectLocation;
    private JLabel spaceCraft;
    private Image icon;
    private Bullet bullet = new Bullet();

    public Point getObjectLocation() {
        return objectLocation;
    }

    public JLabel getSpaceCraft() {
        return spaceCraft;
    }

    public Image getIcon() {
        return icon;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public Thread getBulletCoolDown() {
        return bulletCoolDown;
    }

    Thread bulletCoolDown;


    PlayerSpaceCraft(Point spawnLocation, Bullet bullet) {

        super( spawnLocation, bullet);
        //construct a space craft object with all its attributes

    }

    void move(String direction){

        if (direction.equals("right")){
            this.objectLocation.x ++;
            spaceCraft.setLocation(this.objectLocation);
        }else if (direction.equals("left")){
            this.objectLocation.x --;
            spaceCraft.setLocation(this.objectLocation);
        }

    }

    void shoot() {

        //fires first, then go to cool down.
        //when in cool down, spacecraft cannot fire.

        long coolDown = 1000;
        bullet.fire();
            try {
                Thread.sleep(coolDown);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
    }

    void hitCheck(){


    }

}

class AlienSpaceCraft extends SpaceCraft{


    AlienSpaceCraft(Image icon, Point spawnLocation, Bullet bullet) {
        super(spawnLocation, bullet);
    }
}