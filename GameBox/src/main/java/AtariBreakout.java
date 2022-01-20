import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.Random;


public class AtariBreakout implements Game{

    Rectangle[][] Blocks= new Rectangle[3][10];
    Ellipse2D Ball = new Ellipse2D.Double(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-5,Toolkit.getDefaultToolkit().getScreenSize().height*0.8,10.0,10.0);
    double xv=0,yv = -5,Speed = 5;

    AtariBreakout() {
        Main.INSTANCE.frame.setBounds(0,0,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
        int xF = (int) (Toolkit.getDefaultToolkit().getScreenSize().width/9.5);
        for (int i = 0;i<3;i++) {
            int x = 0;
            for (int j = 0 ; j<10;j++) {
                Blocks[i][j] = new Rectangle(x,50+i*50,new Random().nextInt(30)+xF,49);
                x += Blocks[i][j].getMaxX()+1-x;
            }
        }
    }

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g.setColor(new Color(16, 15, 28));
        g.fill(new  Rectangle(0,0,result.getWidth(),result.getHeight()));

        g.setColor(new Color(14,0,181));
        for (int i = 0;i<3;i++) {
            for (int j = 0 ; j<10;j++) {
                g.fill(Blocks[i][j]);
            }
        }

        g.setColor(Color.CYAN);
        g.fill(Ball);

        Ball.setFrame(Ball.getX()+xv,Ball.getY()+yv,10,10);

        return result;
    }

    void print(Object obj) {
        System.out.println(obj);
    }

   
    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getKeyCode() == KeyEvent.VK_A) {

        }  
        if (event.getKeyCode() == KeyEvent.VK_D) {

        }       
    }

    
    public void keyReleased(KeyEvent event) {
        
    }

}
