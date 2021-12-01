import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class SpaceDestroyer implements Game {
    BufferedImage Ship, opponent, opponent2,Space;
    Clip Pew,Pew2;

    SpaceDestroyer() throws IOException {
        Space = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("Space.png"));
        Ship = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("Ship.png"));
        opponent = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("opponent.png"));
        opponent2 = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("opponent2.png"));
    }

    public void init() throws IOException{
        System.out.println("WHY?");
    }

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(Space, 0, 0, null);

        return result;
    }


    public void keyPressed(KeyEvent event) throws IOException {
    }


    public void keyReleased(KeyEvent event) {
    }

} 