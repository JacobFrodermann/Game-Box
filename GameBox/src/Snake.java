import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Snake implements Game {
    int i=0;
    int[] xkords = {9,9,9};
    int[] ykords= {13,14,15};
    int[] temp;
    int xmov=0,ymov=1;
    int AppleX = new Random().nextInt(20),AppleY = new Random().nextInt(30);
    Rectangle part = new Rectangle(0,0,20,20);
    boolean ToAdd;

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        /*System.out.println("rendering");
        System.out.println(xkords.length);*/
        g.setColor(Color.red);
        part.setLocation(AppleX*20,AppleY*20);
        g.fill(part);
        g.setColor(Color.green);
        while (xkords.length > i) {
            part.setLocation(xkords[i]*20, ykords[i]*20);
            g.fill(part);
            i++;
        }
        i=0;

        try {
            Thread.sleep(60L);
        } catch (InterruptedException e) {}

        while (i+1 < xkords.length) {
            xkords[i] = xkords[i+1];
            ykords[i] = ykords[i+1];
            i++;
        }
        i=0;
        xkords[xkords.length-1] = xkords[xkords.length-1] + xmov;
        ykords[ykords.length-1] = ykords[ykords.length-1] + ymov;
        
        if (ToAdd) {
            temp = xkords;
            int[] xkords = new int[temp.length+1];
            while(i<temp.length) {
                xkords[i] = temp[i];
                i++;;
            }
            xkords[xkords.length-1] = xkords[xkords.length-2] + xmov;

            //mirror for y

            temp = ykords;
            int[] ykords = new int[temp.length+1];
            while(i<temp.length) {
                ykords[i] = temp[i];
                i++;
            }
            ykords[ykords.length-1] = ykords[ykords.length-2] + ymov;
            ToAdd = false;
            System.out.println(xkords.length);
        }
        System.out.println(xkords.length);

        /*System.out.println("|");
        System.out.println(xkords[0]);
        System.out.println(xkords[1]);
        System.out.println(xkords[2]);
        System.out.println(xkords[3]);
        System.out.println("|");
        System.out.println(ykords[0]);
        System.out.println(ykords[1]);
        System.out.println(ykords[2]);
        System.out.println(ykords[3]);*/

        if (xkords[xkords.length-1] == -1) {
            xkords[xkords.length-1] = 20;
        }
        if (xkords[xkords.length-1] == 21) {
            xkords[xkords.length-1] = 0;
        }
        if (ykords[ykords.length-1] == 31) {
            ykords[ykords.length-1] = 0;
        }
        if (ykords[ykords.length-1] == 0) {
            ykords[ykords.length-1] = 31;
        }

        if (xkords[xkords.length-1] == AppleX && ykords[ykords.length-1] == AppleY) {
            ToAdd = true;
            AppleX = new Random().nextInt(20);
            AppleY = new Random().nextInt(30);
        }
        return result;
    }
    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getKeyCode() == KeyEvent.VK_A) {
			xmov = -1;
            ymov = 0;
		}
        if (event.getKeyCode() == KeyEvent.VK_D) {
			xmov = 1;
            ymov = 0;
		}
        if (event.getKeyCode() == KeyEvent.VK_S) {
			xmov = 0;
            ymov = 1;
		}
        if (event.getKeyCode() == KeyEvent.VK_W) {
			xmov = 0;
            ymov = -1;
		}
        
    }
    public void keyReleased(KeyEvent event) {}
}