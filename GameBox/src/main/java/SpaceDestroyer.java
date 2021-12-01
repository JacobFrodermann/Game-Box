import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.spec.ECPrivateKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.geom.Ellipse2D;

public class SpaceDestroyer implements Game {
    BufferedImage Ship, opponent, opponent2,Space, Space2;
    Clip Pew,Pew2;
    Rectangle ShipCol = new Rectangle(180,500,40,32);
    int tick = 0;
    List<double[]> Projektiles;
    int Coldown = 0;

    SpaceDestroyer() throws IOException {
        double[] temp = {0,0,0};
        Projektiles = new ArrayList<double[]>();
        Projektiles.add(temp);
        Main.INSTANCE.frame.setBounds(646,219,400,600);        
        Ship = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("Ship.png"));
        opponent = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("opponent1.png"));
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
        g.setColor(Color.white);

        try {
            if (tick == 0) {
                Space = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("Space.png"));
            }
            if (tick == 75) {
                Space = ImageIO.read(SpaceDestroyer.class.getResourceAsStream("Space2.png"));
                tick = -75;
            }
            tick ++;
        } catch (IOException e) {}
        g.drawImage(Space, 0, 0,400,600, null);

        g.drawImage(Ship,(int) ShipCol.getMinX(), (int) ShipCol.getMinY(), 40,32, null);

        for (int i = 0; i < Projektiles.size(); i++) {
            g.fill(new Ellipse2D.Double(Projektiles.get(i)[0], Projektiles.get(i)[1],10,10));
        }
        MoveProjektiles();
        Coldown --;

        return result;
    }


    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getKeyCode() == KeyEvent.VK_A) {
            ShipCol.setLocation((int) ShipCol.getMinX()-5,(int) ShipCol.getMinY());
        }
        if (event.getKeyCode() == KeyEvent.VK_D) {
            ShipCol.setLocation((int) ShipCol.getMinX()+5,(int) ShipCol.getMinY());
        }
        if (event.getKeyCode() == KeyEvent.VK_W) {
            ShipCol.setLocation((int) ShipCol.getMinX(),(int) ShipCol.getMinY()-5);
        }
        if (event.getKeyCode() == KeyEvent.VK_S) {
            ShipCol.setLocation((int) ShipCol.getMinX(),(int) ShipCol.getMinY()+5);
        }
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            if (Coldown<0) {
                double[] temp  = {ShipCol.getCenterX(),ShipCol.getMinY(),-5.0};
                Projektiles.add(temp);
                Coldown = 10;
            }
        }
    }


    public void keyReleased(KeyEvent event) {
    }
    void MoveProjektiles() {
        double X,Y,VelY;
        for (int i = 0;i < Projektiles.size();i++) {
            X = Projektiles.get(i)[0];
            Y = Projektiles.get(i)[1];
            VelY = Projektiles.get(i)[2];

            double[] temp = {X, Y + VelY, VelY};
            Projektiles.set(i, temp);
        }
    }
} 