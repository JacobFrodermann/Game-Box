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
import java.util.Random;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class Snake implements Game {
    List<String> Read;
    int Highscore;
    Color snake;
    BufferedImage DeadImage, Grass, Apple, SnakeHead;
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
        Grass = ImageIO.read(new File("Grass.png"));
        Apple = ImageIO.read(new File("Apple.png"));
        SnakeHead = ImageIO.read(new File("Snake face.png"));
    }

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(Grass, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawString("Highscore: " + String.valueOf(Highscore), 10, 20);
        g.drawImage(Apple, AppleX*20, AppleY*20, null);
        i=0;
        while (xkords.size() > i) {
            if (i > 12) {
                snake = new Color(235-(i*4),219-(i*4),0);
            } else {
                snake = new Color(235-(i*4),219-(i*4),52-(i*4));
            }
            g.setColor(snake);
            part.setLocation(xkords.get(i)*20, ykords.get(i)*20);
            g.fill(part);
            i++;
        }
        if (ymov == 0) {
            if (xmov == 1){
                g.getTransform().rotate(90);
            } else {
                g.getTransform().rotate(270);
            }
        } else {
            if (ymov == -1) {
                g.getTransform().rotate(180,part);
            }
        }
        g.drawImage(SnakeHead, part.getMinX(),part.getMinY(), null);
        i=0;
        if (Dead) {
            g.drawImage(DeadImage, 50, 250, null);
        }

        try {
            Thread.sleep(60L);
        } catch (InterruptedException e) {}
        if (!Dead) {
            xkords.add(xkords.get(xkords.size()-1)+xmov);
            ykords.add(ykords.get(ykords.size()-1)+ymov);
            ykords.remove(0);
            xkords.remove(0);
            Kol();
        }

        if (xkords.get(lx)<0 || xkords.get(lx)>19 || ykords.get(ly) < 0 || ykords.get(ly) > 29) {
            Dead = true;
            xmov = 0;
            ymov = 0;
        }

        //länger werden
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
        if (!Dead) {
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
        } else {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    Main.INSTANCE.currentGame = new GameSelectionScreen();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (Highscore < xkords.size()) {
                    try {
                        try {
                            IOUtils.write(String.valueOf(Read.get(1) + "\n" + xkords.size()), new FileOutputStream(new File("Data")), StandardCharsets.UTF_8);
                        } catch (FileNotFoundException e){}
                    } catch(IOException e1) {}
                }
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