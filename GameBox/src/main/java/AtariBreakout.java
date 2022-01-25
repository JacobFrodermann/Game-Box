import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.imageio.ImageIO;


public class AtariBreakout implements Game{
    //1366,768
    Rectangle[][] Blocks= new Rectangle[3][9];
    int X = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),Y = Toolkit.getDefaultToolkit().getScreenSize().height;
    Ellipse2D Ball = new Ellipse2D.Double(X/2-5,Y*0.7,10.0,10.0);
    double xv=0,yv = -5,Speed = X/400,inc=Speed/5;
    Rectangle Line = new Rectangle((int)(X*0.425),(int)(Y*0.9),(int)(X*0.15),(int)(Y*0.025));
    Color[][] Colors = new Color[3][9];
    float LineColor = 1f; 
    BufferedImage Victory;
    int removed = 0;
    AtariBreakout() {
        Main.INSTANCE.frame.setBounds(0,0,X,Y);
        try {Victory = ImageIO.read(AtariBreakout.class.getClassLoader().getResourceAsStream("Victory.png"));} catch (IOException e) {e.printStackTrace();}
        int xF = (int) (X/9.5);
        for (int i = 0;i<3;i++) {
            int x = 0;
            for (int j = 0 ; j<9;j++) {
                Colors[i][j] = new Color(Color.HSBtoRGB(new Random().nextFloat(), 0.8F, 0.99F));
                Blocks[i][j] = new Rectangle(x,50+i*50,new Random().nextInt(60)+xF,49);
                x += Blocks[i][j].getMaxX()+1-x;
                if (Blocks[i][j].x>X) {
                    removed ++;
                    Blocks[i][j].height = 0;
                    print("Removed "+i+" row's" +j+ "Block");
                }
            }
        }
    }

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g.setColor(new Color(16, 15, 28));
        g.fill(new  Rectangle(0,0,X,Y));
        g.setColor(new Color(14,200,181));

        if (Ball.getBounds().intersects(Line)) {
            double x = Ball.getCenterX() - Line.getCenterX();
            xv = (x/Line.width)*8*Speed*2;
            yv = Math.sqrt(Math.pow(Speed,2)-Math.pow(xv,2))*-1;
            if (Double.isNaN(yv)) {
                yv = xv;
            }
           print(Speed);
        }

        for (int i = 0;i<3;i++) {
            for (int j = 0 ; j<9;j++) {
                g.setColor(Colors[i][j]);
                g.fill(Blocks[i][j]);
                if (Blocks[i][j].intersects(Ball.getFrame())) {
                    if (Ball.getX()<Blocks[i][j].getMaxX()||Ball.getMaxX()<Blocks[i][j].x) {
                        yv *= -1;
                    } else{xv *= -1;}
                    Blocks[i][j].height = 0;
                    Speed +=inc;
                }
            }
        }

        g.setColor(Color.CYAN);
        g.fill(Ball);
        if(Ball.getX() < 0 || Ball.getX() > X-Ball.getWidth()){
            xv *= -1;
        }

        if(Ball.getY()<0) {
            yv *=-1;
        }

        if(Ball.getY()>Y){
            try {
                Main.INSTANCE.currentGame = new GameSelectionScreen();
            } catch (IOException e) {print("Error" + e.getCause());}}
        if (Ball.intersects(Line)) {
            xv = (Line.getCenterX()-Ball.getCenterX())/Line.width*-3*inc;
            yv = -1*Math.sqrt(Math.pow(Speed, 2)-Math.pow(xv,2));
        }
        if (Ball.getX()>X||Ball.getMaxX()<0) {
            xv*=-1;
        }
        
        if (Ball.getY()>Y){try {Main.INSTANCE.currentGame = new GameSelectionScreen();} catch (IOException e) {}}

        Ball.setFrame(Ball.getX()+xv,Ball.getY()+yv,X*0.015,X*0.015);

        LineColor += 0.001;
        g.setColor(new Color(Color.HSBtoRGB(LineColor, 1, 0.95f)));
        g.fill(Line);

        if (Speed == inc*(32-removed)) {
            g.drawImage(Victory, X/2-100, Y/2-25, null);
        }
    return result;
    }
    
    void print(Object obj) {
        System.out.println(obj);
    }

    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getKeyCode() == KeyEvent.VK_A) {
            Line.x -= 8;
        }  
        if (event.getKeyCode() == KeyEvent.VK_D) {
            Line.x += 8;
        } 
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            Main.INSTANCE.currentGame = new GameSelectionScreen();
        }     
    }

    public void keyReleased(KeyEvent event) {
        
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        Line.x=e.getX()-Line.width/2;
    }
}
