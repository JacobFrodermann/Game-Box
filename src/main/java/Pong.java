import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.json.JSONObject;


public class Pong implements Game {
	Ellipse2D Ball;
	Rectangle Line1, Line2;
	BufferedImage Logo, Plate;
	Double VelX , VelY = 2.0, speed = 3d , dir;
	Double BallX = 190.0, BallY = 290.0;
	int P1Score = 0 , P2Score = 0;
	Clip[] Ping = new Clip[2];
	List<Integer> keys;
	JSONObject data;
	int cooldown = 0;

	public Pong(JSONObject data) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		this.data = data;

		keys = new ArrayList<Integer>();

		Logo = ImageIO.read(Pong.class.getClassLoader().getResourceAsStream("pongLogo.png"));
		Plate = ImageIO.read(Pong.class.getClassLoader().getResourceAsStream("Plate.png"));

		Ball = new Ellipse2D.Double(0,0,20,20);
		Line1 = new Rectangle(180,20,40,5);
		Line2 = new Rectangle(180,575,40,5);
		dir = new Random().nextDouble(Math.PI/2)-Math.PI/4 + new Random().nextInt(0, 1) * Math.PI;
		System.out.println(dir);
		genVelocity();

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.baInputStream(Pong.class.getClassLoader().getResourceAsStream("Pong.wav")));
		Ping[1] = AudioSystem.getClip();
		Ping[1].open(audioInputStream);

		audioInputStream = AudioSystem.getAudioInputStream(Main.baInputStream(Pong.class.getClassLoader().getResourceAsStream("Ping.wav")));
		Ping[0] = AudioSystem.getClip();
		Ping[0].open(audioInputStream);
	}
	public BufferedImage draw(Dimension size) {
		cooldown --;
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		//rendering
		g.drawImage(Plate, 0, 0, null);

		g.setColor(new Color(217, 222, 219));
		g.fill(Ball);

		g.setColor(Color.black);
		g.fill(Line1);
		g.fill(Line2);

		g.drawString(String.valueOf(P1Score), 10, 10);
		g.drawString(String.valueOf(P2Score), 380, 590);

		Ball.setFrame(BallX, BallY, 20, 20);
		
		BallX += VelX;
		BallY += VelY;

		for (Clip c : Ping) {
			if (c.getFramePosition() >= 9216) {
				c.setFramePosition(0);
				c.stop();
			}
		}

		if (BallY < -5) {
			BallX = 190.0;
			BallY = 290.0;
			dir = new Random().nextDouble(Math.PI/2)-Math.PI/4 + Math.PI;
			speed /= 1.5;
			genVelocity();
			P2Score += 1;
			System.out.println(P1Score+"/" + P2Score);
		}
		if (BallY > 605) {
			BallX = 190.0;
			BallY = 290.0;
			dir = new Random().nextDouble(Math.PI/2)-Math.PI/4;
			speed /= 1.5;
			genVelocity();
			P1Score += 1;
			System.out.println(P1Score+"/" + P2Score);
		}

		if ( P1Score > 4) {
			g.drawString("Player 1 Won", 190, 295);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {}
			Main.INSTANCE.reset();
		}
			
		if ( P2Score > 4) {
			g.drawString("Player 2 Won", 190, 295);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {}
			Main.INSTANCE.reset();
		}


	if (Ball.intersects(Line1) && cooldown < 0) {
		speed += .2;
		cooldown = 5;
		VelY *=-1;
		genDir();
		dir += new Random().nextDouble(-.1, .1);
		Ping[new Random().nextInt(0,1)].start();
		genVelocity();
	}
	if (Ball.intersects(Line2) && cooldown < 0) {
		speed += .2;
		cooldown = 5;
		VelY *=-1;
		genDir();
		dir += new Random().nextDouble(-.1, .1)+Math.PI;
		Ping[new Random().nextInt(0,1)].start();
		genVelocity();
	}
	if (BallX <= 0 || BallX >= 380) {
		VelX *= -1;
	}

	if (keys.contains(KeyEvent.VK_A)) {
		Line1.x -= 4;
		if(speed > 4) Line1.x --;
	}
	if (keys.contains(KeyEvent.VK_D)) {
		Line1.x += 4;
		if(speed > 4) Line1.x ++;
	}
	if (keys.contains(KeyEvent.VK_LEFT)) {
		Line2.x -= 4;
		if(speed > 4) Line2.x --;
	}
	if (keys.contains(KeyEvent.VK_RIGHT)) {
		Line2.x += 4;
		if(speed > 4) Line2.x ++;
	}

		return result;
	}
	public void keyPressed(KeyEvent e) throws IOException {
		if (!keys.contains(e.getKeyCode())) {
			keys.add(e.getKeyCode());
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Main.INSTANCE.reset();
		}
	}
	public void keyReleased(KeyEvent e) {
		if (keys.contains(e.getKeyCode())) {
		keys.remove(keys.indexOf(e.getKeyCode()));
		}
	}
	public void genVelocity(){
		VelX = Math.sin(dir) * speed;
		VelY = Math.cos(dir) * speed;
	}
	void genDir() {
		dir = Math.atan((VelX/4)/(VelY/4));
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
}