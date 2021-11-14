import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pong implements Game {
    int Line1x;
    int Line2x;
    Ellipse2D Ball;
    Rectangle Line1, Line2;
    public Pong(){
        Ball = new Ellipse2D.Double(0,0,40,40);
    }
    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //rendering
        g.setColor(Color.WHITE);
        g.setColor(Color.red);
        g.draw(Ball);

        return result;
    }
    public void keyPressed(KeyEvent Event) {

    }
    public void keyReleased(KeyEvent Event) {

    }
}