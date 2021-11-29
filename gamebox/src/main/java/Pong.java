import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Pong implements Game {
    Ellipse2D Ball;
    Rectangle Line1, Line2;
    BufferedImage Logo, Plate;
    Double VelX , VelY = 2.0;
    Double BallX = 190.0, BallY = 290.0;
    int P1Score = 0 , P2Score = 0;
    Clip Ping, Pong;

    public Pong() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
        Logo = ImageIO.read(Pong.class.getClassLoader().getResourceAsStream("pongLogo.png"));
        Plate = ImageIO.read(Pong.class.getClassLoader().getResourceAsStream("Plate.png"));

        Main.INSTANCE.frame.setIconImage(Logo);
        Main.INSTANCE.frame.setBounds(646,219,415,640);

        Ball = new Ellipse2D.Double(0,0,20,20);
        Line1 = new Rectangle(180,20,40,5);
        Line2 = new Rectangle(180,575,40,5);
        VelX = Double.valueOf(new Random().nextInt(6)) - 3.0;

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.baInputStream(Pong.class.getClassLoader().getResourceAsStream("Pong.wav")));
        Pong = AudioSystem.getClip();
        Pong.open(audioInputStream);

        audioInputStream = AudioSystem.getAudioInputStream(Main.baInputStream(Pong.class.getClassLoader().getResourceAsStream("Ping.wav")));
        Ping = AudioSystem.getClip();
        Ping.open(audioInputStream);
    }
    public BufferedImage draw(Dimension size) {
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

        if (Pong.getFramePosition() == 9216) {
            Pong.setFramePosition(0);
            Pong.stop();
        }
        if (Ping.getFramePosition() == 9216) {
            Ping.setFramePosition(0);
            Ping.stop();
        }

        if (BallY < -5) {
            BallX = 190.0;
            BallY = 290.0;
            VelX = Double.valueOf(new Random().nextInt(6)) - 3.0;
            VelY = 2.0;
            P2Score += 1;
            System.out.println(P1Score+"/" + P2Score);
        }
        if (BallY > 605) {
            BallX = 190.0;
            BallY = 290.0;
            VelX = Double.valueOf(new Random().nextInt(6)) - 3.0;
            VelY = -2.0;
            P1Score += 1;
            System.out.println(P1Score+"/" + P2Score);
        }

        if ( P1Score > 4) {
            g.drawString("Player 1 Won", 190, 295);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {}
            try {
                Main.INSTANCE.currentGame = new GameSelectionScreen();
            } catch (IOException e) {}
        }
            
        if ( P2Score > 4) {
            g.drawString("Player 2 Won", 190, 295);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {}
            try {
                new GameSelectionScreen();
            } catch (IOException e) {}
        }


    if (Ball.intersects(Line1)) {
        VelY = 2.0;
        Pong.start();
    }
    if (Ball.intersects(Line2)) {
        Ping.start();
        VelY = -2.0;

    }
    if (BallX <= 0 || BallX >= 380) {
        VelX *= -1;
    }
        return result;
    }
    public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_A) {
			Line1.x -= 6;
        }
        if (event.getKeyCode() == KeyEvent.VK_D) {
            Line1.x += 6;
        } 
        if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            Line2.x -= 6;
        }
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            Line2.x += 6;
        }
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                Main.INSTANCE.currentGame = new GameSelectionScreen();
            } catch (IOException e) {}
        }
    }
    public void keyReleased(KeyEvent Event) {

    }
}