import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public interface Game {
	public BufferedImage draw(Dimension size);
	public void keyPressed(KeyEvent event);
	public void keyReleased(KeyEvent event);
}
