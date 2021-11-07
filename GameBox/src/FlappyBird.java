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
	Double BirdY = 300.0;
	int Score = 0;
	int pipe1Y; 
	int i = 0;

	public FlappyBird() throws IOException {
		bird = ImageIO.read(new File("The Bird.png"));
		dead = ImageIO.read(new File("Dead.png"));
		Pipe = ImageIO.read(new File("Pipe.png"));
		pipe1Y = new Random().nextInt(500);
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));


		if (BirdY > 600 || BirdY < -20 || (BirdY < pipe1Y  || BirdY > pipe1Y-100 && (Score > 80+i*2 && Score < 160+i*2))) {
			g.setColor(Color.BLACK);
			g.drawImage(Pipe, (100-Score/2)+i, -600 + pipe1Y, null);
			g.drawImage(dead, 50, 250, null);
		} else {
			g.drawImage(Pipe, (100-Score/2)+i, -600 + pipe1Y, null);
			g.drawImage(bird, 20, (int) Math.round(BirdY), 40, 40, null);
			BirdY += VelY;
			VelY += 0.1;
			Score += 1;
		}
		if ((100-Score/2)+i < -50) {
			i += 450;
			pipe1Y = new Random().nextInt(500);
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
}
