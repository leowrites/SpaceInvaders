import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
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
    private Bullet bullet = new Bullet();
    private int vX = 0;
    private int vY = 0;
    Thread bulletCoolDown;

    public Point getObjectLocation() {
        return objectLocation;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public Thread getBulletCoolDown() {
        return bulletCoolDown;
    }

    PlayerSpaceCraft(Point spawnLocation, Bullet bullet) {

        super(spawnLocation, bullet);

        this.objectLocation = spawnLocation;
        this.bullet = bullet;
        //construct a space craft object with all its attributes

    }

    void move(String direction){

        if (direction.equals("right")){
            this.objectLocation.x += 10;
        }else if (direction.equals("left")){
            this.objectLocation.x -= 10;
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


    AlienSpaceCraft( Point spawnLocation, Bullet bullet) {
        super(spawnLocation, bullet);
    }
}