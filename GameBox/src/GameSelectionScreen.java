import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;

public class GameSelectionScreen implements Game {
	BufferedImage[] gameThumbnails;
	Class<?>[] gameClasses;
	int selected = 0;
	double scroll = 0;
	double anim = 0;
	double animMovement = 0;
	Color sky = new Color(52, 174, 235);
	Color selectionColor = new Color(255, 60, 0);

	public GameSelectionScreen() throws IOException {
		gameThumbnails = new BufferedImage[] { ImageIO.read(new File("FlappyBird.png")) , ImageIO.read(new File("Pong.png")) };
		gameClasses = new Class<?>[] { FlappyBird.class };
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));
		for(int i = 0; i < gameThumbnails.length; i++) {
			g.drawImage(gameThumbnails[i], 75, 50 + i * 190, 250, 140, null);
			if(i == selected) {
				g.setColor(selectionColor);
				int a = (int) Math.round(8 * Math.sin(anim));
				g.fillPolygon(new int[] { 35 + a, 35 + a, 55 + a }, new int[] { (int) (105 + (i + animMovement) * 190), (int) (135 + (i + animMovement) * 190), (int) (120 + (i + animMovement) * 190) }, 3);
				g.fillPolygon(new int[] { 400 - 35 - a, 400 - 35 - a, 400 - 55 - a }, new int[] { (int) (105 + (i + animMovement) * 190), (int) (135 + (i + animMovement) * 190), (int) (120 + (i + animMovement) * 190) }, 3);
			}
		}
		anim += 0.09;
		animMovement *= 0.9;

		return result;
	}

	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				Main.INSTANCE.currentGame = (Game) gameClasses[selected].getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		if(event.getKeyCode() == KeyEvent.VK_UP) {
			if(selected > 0) {
				selected--;
				animMovement += 1;
			}
		}
		if(event.getKeyCode() == KeyEvent.VK_DOWN) {
			if(selected < gameThumbnails.length - 1) {
				selected++;
				animMovement -= 1;
			}
		}
	}

	public void keyReleased(KeyEvent event) {}
}
