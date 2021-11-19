import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.io.IOException;
import java.util.Random;

public class Snake implements Game {
    int i;
    Rectangle h = new Rectangle(50,50,50,50);
/*    List<Point>;
    int x=0,y=1;
    Snake() {
        TheSnake[0] = new Point(20,30);
        TheSnake[1] = new Point(20,29);
        TheSnake[2] = new Point(20,28);
    }*/
    public BufferedImage draw(Dimension size) {/*
        for (TheSnake.length > i) {

        }
        */
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return result;
    }
    public void keyPressed(KeyEvent event) throws IOException {
        
        
    }
    public void keyReleased(KeyEvent event) {}
}