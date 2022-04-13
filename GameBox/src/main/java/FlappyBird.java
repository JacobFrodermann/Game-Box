import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.JSONObject;

public class FlappyBird implements Game {
	BufferedImage bird;
	BufferedImage Pipe;
	BufferedImage dead;
	BufferedImage deadbird;
	BufferedImage Clouds;
	Color sky = new Color(52, 174, 235);

	double VelY = 0.0;
	int BirdY = 300;
	int Score = 0;
	int pipe1Y; 
	int pipe2Y; 
	int i = 1;
	int l = 0;
	int pipe1X;
	int pipe2X;
	int VelX = 10;
	double CloudX=-1000;
	Clip Pling;
	AffineTransform t;
	boolean Jumped;
	JSONObject data;
	Logger log;

	Rectangle CollisionPipeUpper,CollisionPipeLower,CollisionBird;

	public FlappyBird(JSONObject data, Logger logger) throws IOException, UnsupportedAudioFileException, LineUnavailableException  {
		this.data = data;
		log = logger;

		deadbird = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("The Bird Dead.png"));
		bird = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("The Bird.png"));
		dead = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("Dead.png"));
		Pipe = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("Pipe.png"));
		Clouds = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("Clouds.png"));
		
		pipe1Y = new Random().nextInt(400);
		pipe1X = 100;
		pipe2Y = new Random().nextInt(400);
		pipe2X = 100;
		CollisionBird = new Rectangle(24, BirdY-8,34,24);
		CollisionPipeLower = new Rectangle(pipe1X, pipe1Y+100,40,600);
		CollisionPipeUpper = new Rectangle(pipe1X, pipe1Y-600,40,600);

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.baInputStream(FlappyBird.class.getClassLoader().getResourceAsStream("Pling.wav")));
		Pling = AudioSystem.getClip();
		Pling.open(audioInputStream);
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));
		g.drawImage(Clouds, (int) CloudX, 560, null);
		CloudX += 0.25;
		if (CloudX==100) {CloudX=-1000;}
		pipe1X = (200-Score/2)+i;
		pipe2X = (400-Score/2)+l;

		g.drawImage(Pipe, pipe1X, -600 + pipe1Y, 40,1300,null);
		g.drawImage(Pipe, pipe2X, -600 + pipe2Y, 40,1300,null);

		CollisionBird.setLocation(24,BirdY+8);

		if (Pling.getFramePosition() == 5211) {
			Pling.setFramePosition(0);
			Pling.stop();
		}

		if (pipe1X < pipe2X) {
			CollisionPipeLower.setLocation(pipe1X,pipe1Y+100);
			CollisionPipeUpper.setLocation(pipe1X,pipe1Y-600);
		} else {
			CollisionPipeLower.setLocation(pipe2X,pipe2Y+100);
			CollisionPipeUpper.setLocation(pipe2X,pipe2Y-600);
		}
		if ((BirdY > 600 || BirdY < -60) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper)) {
			g.drawImage(deadbird,20, BirdY,40,40,null);
			g.drawImage(dead, 50, 250, null);
			if (VelX - 10 > data.getInt("highscore")) {
				data.put("highscore", VelX - 10);
				Main.INSTANCE.saveAll();
			}
		} else {
			t = g.getTransform();
			t.rotate(Math.toRadians(VelY*10*data.getInt("rotationFactor")), CollisionBird.getCenterX(), CollisionBird.getCenterY());
			g.setTransform(t);
			g.drawImage(bird, 20, BirdY, 40, 40, null);
			g.setTransform(new AffineTransform());
			
			BirdY += VelY;
			VelY += data.getDouble("gravity");
			Score += VelX/10 + 0.5;
		}
		if ((150-Score/2)+i < -80 ){
			i += 450;
			VelX += 1;
			pipe1Y = new Random().nextInt(400);
			Pling.start();
		}
		if ((150-Score/2)+l < -280 ){
			l += 450;
			VelX += 1;
			pipe2Y = new Random().nextInt(400);
			Pling.start();
		}
		g.setColor(Color.black);
		g.drawString(String.valueOf(VelX - 10), 350, 20);
		g.drawString("Highscore: " + String.valueOf(data.getInt("highscore")), 15, 20);

		return result;


	}

	public void keyPressed(KeyEvent event) throws IOException {
		if (event.getKeyCode() == KeyEvent.VK_ENTER && ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper))) {
            Main.INSTANCE.switchGame(0);
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			if (!Jumped) {
				VelY -= data.getDouble("jumpHeight");
				Jumped = true;
			}
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE && ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper))) {
			Score = 0;
			BirdY =300;
			VelY = 0;
		}
	}
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			Jumped = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		VelY -= data.getDouble("jumpHeight");
		Jumped = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
