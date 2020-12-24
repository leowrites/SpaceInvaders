import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SpaceCraft {
    //An abstract class for all types of space crafts

    //move function takes in a location and returns a location

    private final BufferedImage explosionImage;
    private boolean hit = false;

    SpaceCraft() throws IOException {
        this.explosionImage = scaleImage(ImageIO.read(new File("explosion.png")));
    }

    void shoot() throws IOException {
    }

    BufferedImage scaleImage(Image image) {
        Image afterScaling = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        output.createGraphics().drawImage(afterScaling, 0, 0, null);
        return output;
    }

    public BufferedImage getExplosionImage() {
        return explosionImage;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

}

class PlayerSpaceCraft extends SpaceCraft {

    private final Point objectLocation;
    private BufferedImage playerSpaceCraftImage = super.scaleImage(ImageIO.read(new File("player.png")));

    PlayerSpaceCraft(Point spawnLocation) throws IOException {
        super();
        this.objectLocation = spawnLocation;
        //construct a space craft object with all its attributes

    }

    private boolean right = false;
    private boolean left = false;

    public void move() {
        /*
        There was a lag when changing directions, for example when changing direction from left to right
        the space craft would stop for 1 second and then move. This was fixed by using a boolean, and set
        that direction to false when the key is released
         */
        int velocityX = 0;
        if (right) {
            if (objectLocation.x < 750) {
                velocityX = 5;
            } else {
                objectLocation.x = 750;
            }
            objectLocation.x += velocityX;
        } else if (left) {
            if (objectLocation.x > 0) {
                velocityX = -5;

            } else {
                objectLocation.x = 0;
            }
            objectLocation.x += velocityX;
        }
    }

    private final ArrayList<Bullet> bullets = new ArrayList<>();

    public void shoot() throws IOException {

        //fires first, then go to cool down.
        //when in cool down, spacecraft cannot fire.
        //user can fire 5 bullets
        int maximumAmountOfBullets = 1;
        if (bullets.size() - 1 < maximumAmountOfBullets) {
            Point currentPosition = new Point(objectLocation.x, objectLocation.y);
            Bullet bullet = new Bullet(currentPosition, "player");
            bullets.add(bullet);
        }
    }

    public void hitCheck(ArrayList<Bullet> enemyBullets) {
        for (Bullet bullet : enemyBullets) {
            if (bullet != null) {
                int xMax = this.getObjectLocation().x + 30;
                int xMin = this.getObjectLocation().x - 30;
                int yMax = this.getObjectLocation().y + 30;
                int yMin = this.getObjectLocation().y - 30;
                int bulletX = bullet.getObjectLocation().x;
                int bulletY = bullet.getObjectLocation().y;
                if (bulletX <= xMax && bulletX >= xMin && bulletY <= yMax && bulletY >= yMin) {
                    bullet.setHit(true);
                    this.setHit(true);
                }
            }
        }
    }

    public void kill() {
        this.playerSpaceCraftImage = getExplosionImage();
    }

    public BufferedImage getPlayerSpaceCraftImage() {
        return playerSpaceCraftImage;
    }

    public Point getObjectLocation() {
        return objectLocation;
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
    private BufferedImage alienSpaceCraftImage = super.scaleImage(ImageIO.read(new File("enemy.png")));
    private final int row;
    private final int column;
    private Bullet bullet;

    AlienSpaceCraft(Point spawnLocation, int column, int row) throws IOException {

        super();
        this.objectLocation = spawnLocation;
        this.row = row;
        this.column = column;
    }

    public void move(boolean right) {
        if (right) {
            objectLocation.x += 5;
            if (objectLocation.x > 700) {
                objectLocation.x = 700;
                moveDown = true;
            }
        } else {
            objectLocation.x -= 5;
            if (objectLocation.x < 50) {
                objectLocation.x = 50;
                moveDown = true;
            }
        }
    }

    public void shoot() throws IOException {
        Point currentPosition = new Point(objectLocation.x, objectLocation.y);
        bullet = new Bullet(currentPosition, "alien");
    }


    public void kill() {
        this.alienSpaceCraftImage = getExplosionImage();
    }

    public void hitCheck(ArrayList<Bullet> playerBullets) {

        if (playerBullets != null) {
            for (Bullet bullet : playerBullets) {
                int xMax = this.objectLocation.x + 30;
                int xMin = this.objectLocation.x - 30;
                int yMax = this.objectLocation.y + 30;
                int yMin = this.objectLocation.y - 30;
                int bulletX = bullet.getObjectLocation().x;
                int bulletY = bullet.getObjectLocation().y;
                if (bulletX <= xMax && bulletX >= xMin && bulletY <= yMax && bulletY >= yMin) {
                    //within the vicinity
                    bullet.setHit(true);
                    this.setHit(true);
                }
            }
        }
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public BufferedImage getAlienSpaceCraftImage() {
        return alienSpaceCraftImage;
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}