import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Pong implements Game {

    Ellipse2D Ball;
    Rectangle Line1, Line2;
    BufferedImage Logo;
    Double VelX = new Double(new Random().nextInt(6)) - 3.0, VelY = 2.0;
    Double BallX = 190.0, BallY = 290.0;
    int P1Score = 0 , P2Score = 0;

    public Pong() throws IOException{
        Logo = ImageIO.read(new File("PongLogo.png"));
        Main.INSTANCE.frame.setIconImage(Logo);
        Ball = new Ellipse2D.Double(0,0,20,20);
        Line1 = new Rectangle(180,20,40,5);
        Line2 = new Rectangle(180,575,40,5);
    }
    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //rendering
        g.setColor(Color.WHITE);
        g.fill(new Rectangle(new Point(), size));
        g.setColor(Color.red);
        g.fill(Ball);
        g.setColor(Color.black);
        g.fill(Line1);
        g.fill(Line2);

        g.drawString(String.valueOf(P1Score), 5, 10);
        g.drawString(String.valueOf(P2Score), 380, 590);

        Ball.setFrame(BallX, BallY, 20, 20);
        
        BallX += VelX;
        BallY += VelY;

        if (BallY < -5) {
            BallX = 190.0;
            BallY = 290.0;
            VelX = new Double(new Random().nextInt(6)) - 3.0;
            VelY = 2.0;
            P2Score += 1;
            System.out.println(P1Score+"/" + P2Score);
        }
        if (BallY > 605) {
            BallX = 190.0;
            BallY = 290.0;
            VelX = new Double(new Random().nextInt(6)) - 3.0;
            VelY = -2.0;
            P1Score += 1;
            System.out.println(P1Score+"/" + P2Score);
        }

        if ( P1Score > 4) {
            g.drawString("Player 1 Won", 190, 295);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                Main.INSTANCE.currentGame = new GameSelectionScreen();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
            
        if ( P2Score > 4) {
            g.drawString("Player 2 Won", 190, 295);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                Game currentGame = new GameSelectionScreen();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    if (Ball.intersects(Line1)) {
        VelY = 2.0;
    }
    if (Ball.intersects(Line2)) {
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
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void keyReleased(KeyEvent Event) {

    }
}