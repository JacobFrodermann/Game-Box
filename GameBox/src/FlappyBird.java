import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

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
	int i = 1;
	int l = 0;
	int pipe1X;
	int pipe2X;
	int VelX = 10;
	int Highscore = 0;
	List<String> Read;
	byte[] bytes,md5;

	Rectangle CollisionPipeUpper,CollisionPipeLower,CollisionBird;

	public FlappyBird() throws IOException {
		Read = IOUtils.readLines(new FileInputStream(new File("Data.txt")), StandardCharsets.UTF_8);
		bytes = Read.get(0).getBytes("UTF-8");
		try {
			md5 = MessageDigest.getInstance("MD5").digest(bytes);
		} catch (NoSuchAlgorithmException e) {md5 = bytes;/*will not happen*/}
		if(new String(md5) != Read.get(1)) {
			Highscore = 0;
			try {
				try {
					IOUtils.write(String.valueOf(Highscore) + "\ncfcd208495d565ef66e7dff9f98764da", new FileOutputStream(new File("Data.txt")));
			} catch (FileNotFoundException e){}
			} catch(IOException e1) {}
		}
		System.out.println(Highscore);
		deadbird = ImageIO.read(new File("The Bird Dead.png"));
		bird = ImageIO.read(new File("The Bird.png"));
		dead = ImageIO.read(new File("Dead.png"));
		Pipe = ImageIO.read(new File("Pipe.png"));
		pipe1Y = new Random().nextInt(400);
		pipe1X = 100;
		pipe2Y = new Random().nextInt(400);
		pipe2X = 100;
		CollisionBird = new Rectangle(24, BirdY-8,34,24);
		CollisionPipeLower = new Rectangle(pipe1X, pipe1Y+100,40,600);
		CollisionPipeUpper = new Rectangle(pipe1X, pipe1Y-600,40,600);
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

		CollisionBird.setLocation(24,BirdY+8);

if (pipe1X < pipe2X) {
	CollisionPipeLower.setLocation(pipe1X,pipe1Y+100);
	CollisionPipeUpper.setLocation(pipe1X,pipe1Y-600);
} else {
	CollisionPipeLower.setLocation(pipe2X,pipe2Y+100);
	CollisionPipeUpper.setLocation(pipe2X,pipe2Y-600);
}

		if ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper)) {
			g.drawImage(deadbird,20, BirdY,40,40,null);
			g.drawImage(dead, 50, 250, null);
			if (VelX - 10 > Highscore) {
				Highscore = VelX - 10;
				try {
					try {
						IOUtils.write(String.valueOf(Highscore), new FileOutputStream(new File("Data.txt")));
				} catch (FileNotFoundException e){}
				} catch(IOException e1) {}
				}
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
		g.drawString("Highscore: " + String.valueOf(Highscore), 15, 20);

		return result;


	}

	public void keyPressed(KeyEvent event) throws IOException {
		if (event.getKeyCode() == KeyEvent.VK_ENTER && ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper))) {
			Main.INSTANCE.currentGame = new GameSelectionScreen();
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			VelY -= 4;
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE && ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper))) {
			Main.INSTANCE.currentGame = new FlappyBird();
		}
	}
	public void keyReleased(KeyEvent event) {
	}
}
