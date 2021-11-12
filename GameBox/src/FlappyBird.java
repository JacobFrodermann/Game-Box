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

import javax.imageio.ImageIO;
import java.util.Random;

public class FlappyBird implements Game {
	BufferedImage bird;
	BufferedImage Pipe;
	BufferedImage dead;
	Color sky = new Color(52, 174, 235);

	Double VelY = 0.0;
	int BirdY = 300;
	int Score = 0;
	int pipe1Y; 
	int i = 0;
	int pipe1X;

	public FlappyBird() throws IOException {
		bird = ImageIO.read(new File("The Bird.png"));
		dead = ImageIO.read(new File("Dead.png"));
		Pipe = ImageIO.read(new File("Pipe.png"));
		pipe1Y = new Random().nextInt(400);
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));

		pipe1X = (200-Score/2)+i;
		if ((BirdY > 600 || BirdY < -20) || intersects(40, BirdY, 40, 40, pipe1X, pipe1Y , 40, 600)) {
			g.setColor(Color.BLACK);
			g.drawImage(Pipe, pipe1X, -600 + pipe1Y, null);
			g.drawImage(dead, 50, 250, null);
		} else {
			g.drawImage(Pipe, pipe1X, -600 + pipe1Y, null);
			g.drawImage(bird, 20, BirdY, 40, 40, null);
			BirdY += VelY;
			VelY += 0.1;
			Score += 1;
		}
		if ((150-Score/2)+i < -50) {
			i += 450;
			pipe1Y = new Random().nextInt(400);
		}
		return result;


	}

	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			VelY -= 5;
		}
	}
	public void keyReleased(KeyEvent event) {
	}
	public static boolean intersects(int x1, int y1, int xl1,int yl1, int x2, int y2, int xl2, int yl2) {
		if ((x1 > x2 +xl2)/*rechts*/) {
			if (x2 +xl2 < x1)/*links*/ {
				if (y1 + yl1 < y2)/*darüber*/{
					if (y2 + yl2 < y1) /*darunter*/{
						return true;
					}
				}
			}
		}
		return false;
	}
}
