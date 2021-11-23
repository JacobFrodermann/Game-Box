import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
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
import java.text.CollationElementIterator;
import java.util.Random;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class Snake implements Game {
    List<String> Read;
    int Highscore;
    BufferedImage DeadImage;
    int i=0;
    int lx = 2;
    int ly = 2;
    Boolean Dead = false;
    List<Integer> xkords = new ArrayList<Integer>(){{add(9);add(9);add(9);}};
    List<Integer> ykords = new ArrayList<Integer>(){{add(14);add(13);add(12);}};;
    int[] temp;
    int xmov=0,ymov=1;
    int AppleX = new Random().nextInt(20),AppleY = new Random().nextInt(30);
    Rectangle part = new Rectangle(0,0,20,20);
    boolean ToAdd;

    public Snake() throws IOException{
        Read = IOUtils.readLines(new FileInputStream(new File("Data")), StandardCharsets.UTF_8);
        Highscore = Integer.valueOf(Read.get(1));
        DeadImage = ImageIO.read(new File("Dead.png"));
    }

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.drawString("Highscore: " + String.valueOf(Highscore), 10, 20);
        g.setColor(Color.red);
        part.setLocation(AppleX*20,AppleY*20);
        g.fill(part);
        g.setColor(Color.green);
        while (xkords.size() > i) {
            part.setLocation(xkords.get(i)*20, ykords.get(i)*20);
            g.fill(part);
            i++;
        }
        i=0;
        if (Dead) {
            g.drawImage(DeadImage, 50, 250, null);
        }

        try {
            Thread.sleep(60L);
        } catch (InterruptedException e) {}
        xkords.add(xkords.get(xkords.size()-1)+xmov);
        ykords.add(ykords.get(ykords.size()-1)+ymov);
        ykords.remove(0);
        xkords.remove(0);
        Kol();
        if (ToAdd) {
            xkords.add(xkords.get(xkords.size()-1)+xmov);
            ykords.add(ykords.get(ykords.size()-1)+ymov);
            ToAdd = false;
            Kol();
        }
        if (xkords.get(lx) == AppleX && ykords.get(ly) == AppleY) {
            ToAdd = true;
            AppleX = new Random().nextInt(20);
            AppleY = new Random().nextInt(30);
            lx = xkords.size()-1;
            ly = ykords.size()-1;
        }
        return result;
    }
    public void keyPressed(KeyEvent event) throws IOException {
        if (event.getKeyCode() == KeyEvent.VK_A && !Dead) {
			xmov = -1;
            ymov = 0;
		}
        if (event.getKeyCode() == KeyEvent.VK_D && !Dead) {
			xmov = 1;
            ymov = 0;
		}
        if (event.getKeyCode() == KeyEvent.VK_S && !Dead) {
			xmov = 0;
            ymov = 1;
		}
        if (event.getKeyCode() == KeyEvent.VK_W && !Dead) {
			xmov = 0;
            ymov = -1;
		}
        if (event.getKeyCode() == KeyEvent.VK_ENTER && Dead) {
            try {
                Main.INSTANCE.currentGame = new GameSelectionScreen();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (Highscore < xkords.size()) {
                try {
                    try {
                        IOUtils.write(String.valueOf(Read.get(1) + "\n" + xkords.size()), new FileOutputStream(new File("Data")));
                    } catch (FileNotFoundException e){}
                } catch(IOException e1) {}
            }
        }
    }
    
    public void keyReleased(KeyEvent event) {}
    private void Kol() {
    i = 0;
        while (i != lx -1) {
            i++;
            if (xkords.get(lx) == xkords.get(i)) {
                if (ykords.get(ly) == ykords.get(i)) {
                    Dead = true;
                    xmov = 0;
                    ymov = 0;
                }
            }
        }
    i = 0;
    }
}