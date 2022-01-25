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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.Toolkit;

import org.apache.commons.io.IOUtils;

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
	int Highscore = 0;
	double CloudX=-1000;
	List<String> Read;
	Clip Pling;
	AffineTransform t;
	boolean Jumped;

	Double Gravity, RotationFaktor, JumpHeight;

	Rectangle CollisionPipeUpper,CollisionPipeLower,CollisionBird;

	public FlappyBird() throws IOException, UnsupportedAudioFileException, LineUnavailableException  {
		Main.INSTANCE.frame.setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-200,Toolkit.getDefaultToolkit().getScreenSize().height/2-300,400,600);
		try {
			Gravity = (Double) Main.INSTANCE.Settings.get("Gravity");
			RotationFaktor = (Double) Main.INSTANCE.Settings.get("RotationFaktor");
			JumpHeight = (Double) Main.INSTANCE.Settings.get("JumpHeight");
		} catch (java.lang.StringIndexOutOfBoundsException | java.lang.NumberFormatException e) {e.printStackTrace();}

		if (new File("Data").exists()) {
			Read = IOUtils.readLines(new FileInputStream(new File("Data")), StandardCharsets.UTF_8);
		}else {
			IOUtils.write("0\n3", new FileOutputStream(new File("Data")), StandardCharsets.UTF_8);
			Read = IOUtils.readLines(new FileInputStream(new File("Data")), StandardCharsets.UTF_8);
		}
		Highscore = Integer.valueOf(Read.get(0));
		System.out.println("Highscore: "+Highscore);
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
		Main.INSTANCE.frame.setIconImage(bird);
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
		g.drawImage(Clouds, (int) CloudX, 510, null);
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
			if (VelX - 10 > Highscore) {
				Highscore = VelX - 10;
				try {
					try {
						IOUtils.write(String.valueOf(Highscore) +"\n" + Read.get(1), new FileOutputStream(new File("Data")), StandardCharsets.UTF_8);
				} catch (FileNotFoundException e){}
				} catch(IOException e1) {}
				}
		} else {
			t = g.getTransform();
			t.rotate(Math.toRadians(VelY*10*RotationFaktor), CollisionBird.getCenterX(), CollisionBird.getCenterY());
			g.setTransform(t);
			g.drawImage(bird, 20, BirdY, 40, 40, null);
			g.setTransform(new AffineTransform());
			
			BirdY += VelY;
			VelY += Gravity;
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
		g.drawString("Highscore: " + String.valueOf(Highscore), 15, 20);

		return result;


	}

	public void keyPressed(KeyEvent event) throws IOException {
		if (event.getKeyCode() == KeyEvent.VK_ENTER && ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper))) {
			Main.INSTANCE.currentGame = new GameSelectionScreen();
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			if (!Jumped) {
				VelY -= JumpHeight;
				Jumped = true;
			}
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE && ((BirdY > 600 || BirdY < -20) || CollisionBird.intersects(CollisionPipeLower) || CollisionBird.intersects(CollisionPipeUpper))) {
			try {
				Main.INSTANCE.currentGame = new FlappyBird();
			} catch (UnsupportedAudioFileException | LineUnavailableException e) {}
		}
	}
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			Jumped = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		VelY -= JumpHeight;
		Jumped = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
