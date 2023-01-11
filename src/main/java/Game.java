import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public interface Game {
	public BufferedImage draw(Dimension size) throws Throwable;
	public void keyPressed(KeyEvent event) throws Throwable;
	public void keyReleased(KeyEvent event) throws Throwable;
    public void mouseClicked(MouseEvent e) throws Throwable;
    public void mouseMoved(MouseEvent e) throws Throwable;
}