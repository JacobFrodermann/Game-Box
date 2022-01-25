import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.Toolkit;


public class Settings implements Game {
    Color sky = new Color(52, 174, 235);
    BufferedImage Switch, SwitchOff;

    Settings() {
        try {
            Switch = ImageIO.read(Settings.class.getClassLoader().getResourceAsStream("Switch.png"));
            SwitchOff = ImageIO.read(Settings.class.getClassLoader().getResourceAsStream("SwitchOff.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));
        int i = 0;
        String types = Main.INSTANCE.Types;
        Main.INSTANCE.Settings.forEach((x,y) -> {
            g.drawString(x, 50, 50+20*i);
            if (types.charAt(i) == 'd') {
                g.drawString(String.valueOf((Double)y), 100, 50+20*i);
            } else {
                if ((Boolean)y) {
                    g.drawImage(Switch, 100, 50+20*i, null);
                } else {
                    g.drawImage(SwitchOff, 100, 50+20*i, null);
                }
                
            }
        });

        return result;
    }

    @Override
    public void keyPressed(KeyEvent event) throws IOException {
         
        
    }

    @Override
    public void keyReleased(KeyEvent event) {
         
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
         
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
         
        
    }
    
}
