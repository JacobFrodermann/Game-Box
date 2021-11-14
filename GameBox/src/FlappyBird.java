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
	int pipe2Y; 
	int i = 0;
	int l = 0;
	int pipe1X;
	int pipe2X;
	int VelX = 10;

	Rectangle CollisonPipeUpper,CollisonPipeLower,CollisonBird;

	public FlappyBird() throws IOException {

		deadbird = ImageIO.read(new File("The Bird Dead.png"));
		bird = ImageIO.read(new File("The Bird.png"));
		dead = ImageIO.read(new File("Dead.png"));
		Pipe = ImageIO.read(new File("Pipe.png"));
		pipe1Y = new Random().nextInt(400);
		pipe1X = 100;
		pipe2Y = new Random().nextInt(400);
		pipe2X = 100;
		CollisonBird = new Rectangle(24, BirdY-8,34,24);
		CollisonPipeLower = new Rectangle(pipe1X, pipe1Y+100,40,600);
		CollisonPipeUpper = new Rectangle(pipe1X, pipe1Y-600,40,600);
		Main.INSTANCE.frame.setIconImage(bird);
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
		pipe2X = (400-Score/2)+l;

		g.drawImage(Pipe, pipe1X, -600 + pipe1Y, 40,1300,null);
		g.drawImage(Pipe, pipe2X, -600 + pipe2Y, 40,1300,null);

		CollisonBird.setLocation(24,BirdY+8);

if (pipe1X < pipe2X) {
	CollisonPipeLower.setLocation(pipe1X,pipe1Y+100);
	CollisonPipeUpper.setLocation(pipe1X,pipe1Y-600);
} else {
	CollisonPipeLower.setLocation(pipe2X,pipe2Y+100);
	CollisonPipeUpper.setLocation(pipe2X,pipe2Y-600);
}

		if ((BirdY > 600 || BirdY < -20) || CollisonBird.intersects(CollisonPipeLower) || CollisonBird.intersects(CollisonPipeUpper)) {
			g.drawImage(deadbird,20, BirdY,40,40,null);
			g.drawImage(dead, 50, 250, null);
		} else {

			g.drawImage(bird, 20, BirdY, 40, 40, null);
			BirdY += VelY;
			VelY += 0.125;
			Score += VelX/10;
		}
		if ((150-Score/2)+i < -80 ){
			i += 450;
			VelX += 1;
			pipe1Y = new Random().nextInt(400);
		}
		if ((150-Score/2)+l < -280 ){
			l += 450;
			VelX += 1;
			pipe2Y = new Random().nextInt(400);
		}
		g.setColor(Color.black);
		g.drawString(String.valueOf(VelX - 10), 350, 20);

		return result;


	}

	public void keyPressed(KeyEvent event) throws IOException {
		if (event.getKeyCode() == KeyEvent.VK_ENTER && ((BirdY > 600 || BirdY < -20) || CollisonBird.intersects(CollisonPipeLower) || CollisonBird.intersects(CollisonPipeUpper))) {
			Main.INSTANCE.currentGame = new GameSelectionScreen();
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			VelY -= 4;
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE && ((BirdY > 600 || BirdY < -20) || CollisonBird.intersects(CollisonPipeLower) || CollisonBird.intersects(CollisonPipeUpper))) {
			Main.INSTANCE.currentGame = new FlappyBird();
		}
	}
	public void keyReleased(KeyEvent event) {
	}
}
