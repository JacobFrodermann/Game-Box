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
	BufferedImage deadbird;
	Color sky = new Color(52, 174, 235);

	Double VelY = 0.0;
	int BirdY = 300;
	int Score = 0;
	int pipe1Y; 
	int i = 0;
	int pipe1X;

	Rectangle CollisonPipeUpper,CollisonPipeLower,CollisonBird;

	public FlappyBird() throws IOException {
		deadbird = ImageIO.read(new File("The Bird Dead.png"));
		bird = ImageIO.read(new File("The Bird.png"));
		dead = ImageIO.read(new File("Dead.png"));
		Pipe = ImageIO.read(new File("Pipe.png"));
		pipe1Y = new Random().nextInt(400);
		pipe1X = 100;
		CollisonBird = new Rectangle(24, BirdY-8,34,24);
		CollisonPipeLower = new Rectangle(pipe1X, pipe1Y+100,40,600);
		CollisonPipeUpper = new Rectangle(pipe1X, pipe1Y-600,40,600);
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
		if ((BirdY > 600 || BirdY < -20) || CollisonBird.intersects(CollisonPipeLower) || CollisonBird.intersects(CollisonPipeUpper)) {
			g.setColor(Color.BLACK);
			g.drawImage(Pipe, pipe1X, -600 + pipe1Y, 40,1300,null);
			g.drawImage(deadbird,20, BirdY,40,40,null);
			g.drawImage(dead, 50, 250, null);
		} else {
			CollisonBird.setLocation(24,BirdY+8);
			CollisonPipeLower.setLocation(pipe1X,pipe1Y+100);
			CollisonPipeUpper.setLocation(pipe1X,pipe1Y-600);
			g.drawImage(Pipe, pipe1X, -600 + pipe1Y, 40,1300,null);
			g.drawImage(bird, 20, BirdY, 40, 40, null);
			BirdY += VelY;
			VelY += 0.1;
			Score += 1;
			/*System.out.println(""+BirdY);
			System.out.println("PipieX"+pipe1X);
			System.out.println("PipieY"+pipe1Y);*/
		}
		if ((150-Score/2)+i < -80) {
			i += 450;
			pipe1Y = new Random().nextInt(400);
		}
		return result;


	}

	public void keyPressed(KeyEvent event) throws IOException {
		if (event.getKeyCode() == KeyEvent.VK_ENTER && ((BirdY > 600 || BirdY < -20) || CollisonBird.intersects(CollisonPipeLower) || CollisonBird.intersects(CollisonPipeUpper))) {
			Main.INSTANCE.currentGame = new GameSelectionScreen();
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			VelY -= 5;
		}
	}
	public void keyReleased(KeyEvent event) {
	}
}
