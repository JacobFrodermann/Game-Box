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
	BufferedImage deathText;
	BufferedImage deadbird;
	BufferedImage Clouds;
	Color sky = new Color(52, 174, 235);

	double VelY = 0.0, VelX = 10.0;
	int BirdY = 300;
	int Score = 0;
	int pipe1Y; 
	int pipe2Y; 
	int x = 1;
	int l = 0;
	int pipe1X;
	int pipe2X;
	double CloudX=-1000;
	Clip Pling;
	AffineTransform t;
	boolean Jumped, dead;
	JSONObject data;
	Rectangle BirdCollision;
	Pipe[] pipes = new Pipe[] {new Pipe(200, new Random().nextInt(400)), new Pipe(400, new Random().nextInt(400))};
	private class Pipe{
		double x,y;
		Pipe (int x,int y){
			this.x = x;
            this.y = y;
		}
		Rectangle getLowerRect(){
			return new Rectangle((int)x,(int)y-600,40,600);
		}
		Rectangle getUpperRect(){
			return new Rectangle((int)x,(int)y+100,40,600);
        }
	}

	public FlappyBird(JSONObject data) throws IOException, UnsupportedAudioFileException, LineUnavailableException  {
		this.data = data;

		deadbird = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("The Bird Dead.png"));
		bird = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("The Bird.png"));
		deathText = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("Dead.png"));
		Pipe = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("Pipe.png"));
		Clouds = ImageIO.read(FlappyBird.class.getClassLoader().getResourceAsStream("Clouds.png"));
		BirdCollision = new Rectangle(24, BirdY-8,34,24);
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
 
		for (Pipe p : pipes) {g.drawImage(Pipe, (int)p.x,(int)p.y-600 , 40,1300,null);}

		BirdCollision.setLocation(24,BirdY+8);

		if (Pling.getFramePosition() == 5211) {//restart Audio
			Pling.setFramePosition(0);
			Pling.stop();
		}
		if((BirdY > 600 || BirdY < -60) || BirdCollision.intersects(pipes[0].getLowerRect()) || BirdCollision.intersects(pipes[0].getUpperRect()) && !dead) {
			dead = true;
			if (VelX - 10 > data.getInt("highscore")) {
				data.put("highscore", VelX - 10);
				Main.INSTANCE.saveData();
			}
		}
		if (dead) {
			g.drawImage(deadbird,20, BirdY,40,40,null);
			g.drawImage(deathText, 50, 250, null);
		} else {
			for(Pipe p : pipes) p.x -= VelX/20;
			t = g.getTransform();
			t.rotate(Math.toRadians(VelY*10*data.getInt("rotationFactor")), BirdCollision.getCenterX(), BirdCollision.getCenterY());
			g.setTransform(t);
			g.drawImage(bird, 20, BirdY, 40, 40, null);
			g.setTransform(new AffineTransform());
			
			BirdY += VelY;
			VelY += data.getDouble("gravity");
			Score += VelX/10 + 0.5;
		}

		if (pipes[0].x < -40) {
    			VelX += 1;
			System.arraycopy(pipes, 1, pipes, 0, 1);
			pipes[1] = new Pipe(400, new Random().nextInt(400));
			Pling.start();
		}
			

		g.setColor(Color.black);
		g.drawString(String.valueOf(VelX - 10), 350, 20);
		g.drawString("Highscore: " + String.valueOf(data.getInt("highscore")), 15, 20);
      
		g.draw(pipes[0].getLowerRect());
		g.draw(pipes[0].getUpperRect());
		g.draw(pipes[1].getLowerRect());
		g.draw(pipes[1].getUpperRect());

		return result;
	}

	public void keyPressed(KeyEvent event) throws IOException {
		if (event.getKeyCode() == KeyEvent.VK_ENTER && dead) {
            Main.INSTANCE.reset();
		}
		if (event.getKeyCode() == KeyEvent.VK_SPACE && dead) {
			Score = 0;
			BirdY =300;
			VelY = 0;
		}
	}
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			VelY -= data.getDouble("jumpHeight");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		VelY -= data.getDouble("jumpHeight");
	}
	public void mouseMoved(MouseEvent e) {}
}
