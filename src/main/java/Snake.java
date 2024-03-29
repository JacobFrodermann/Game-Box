import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
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

public class Snake implements Game {
    Clip eat;
    List<String> Read;
    Color snake;
    BufferedImage DeadImage, Grass, Apple, SnakeHead,TreeGround, Tree1,Grass1, Tree2, Tree3, Lake;
    int i=0;
    int lx = 3;
    int ly = 3;
    Boolean Dead = false;
    List<Integer> xkords = new ArrayList<Integer>(){{add(9);add(9);add(9);}};
    List<Integer> ykords = new ArrayList<Integer>(){{add(14);add(13);add(12);}};
    int xmov=0,ymov=1, helptime;
    int AppleX, AppleY;
    Rectangle part = new Rectangle(0,0,20,20), TreeGroundKol = new Rectangle(320,200,120,20);
    Rectangle Tree1Kol = new Rectangle(700,700,20,20),Tree2kol = new Rectangle(500,500,20,20);
    Rectangle LakeKol = new Rectangle(80,600,280,360), Tree3kol = new Rectangle(80,140,20,20);
    boolean ToAdd;
    JSONObject data;

    public Snake(JSONObject data) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
        this.data = data;

        if (!data.getBoolean("deadlyLake")) {
            LakeKol.setLocation(10000, 0);
        }

        DeadImage = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Dead.png"));
        Grass = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Grass.png"));
        Apple = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Apple.png"));
        SnakeHead = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Snake face.png"));
        TreeGround = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Tree Ground.png"));
        Tree1 = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Tree1.png"));
        Tree2 = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Tree2.png"));
        Tree3 = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Tree3.png"));
        Lake = ImageIO.read(Snake.class.getClassLoader().getResourceAsStream("Lake.png"));
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.baInputStream(Snake.class.getClassLoader().getResourceAsStream("Eat.wav")));
        eat = AudioSystem.getClip();
        eat.open(audioInputStream);
        GenApplePos();
    }

    public BufferedImage draw(Dimension size) {
        BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(Grass, 0, 0, null);
        g.drawImage(Grass, 400, 0, null);
        g.drawImage(Grass, 800, 0, null);
        g.drawImage(Grass, 0, 600, null);
        g.drawImage(Grass, 400, 600, null);
        g.drawImage(Grass, 800, 600, null);
        g.setColor(Color.BLACK);
        g.drawString("Highscore: " + String.valueOf(data.getInt("highscore")), 10, 20);
        g.drawString(String.valueOf(xkords.size()), 100,20);
        g.drawImage(Lake, 80,600, null);
        g.drawImage(Apple, AppleX*20-20, AppleY*20-20, null);

        i=0;
        while (xkords.size() > i) {
                snake = new Color(Color.HSBtoRGB((float) (55.0+i * 2)/100,(float)82.0/100,(float)56.0/100));
            g.setColor(snake);
            part.setLocation(xkords.get(i)*20-20, ykords.get(i)*20-20);
            g.fill(part);
            i++;
        }

        AffineTransform t = g.getTransform(); 
        if (ymov == 0) {
            if (xmov == -1){
                t.rotate(Math.toRadians(90),part.getCenterX(), part.getCenterY());
            } else {
                t.rotate(Math.toRadians(270),part.getCenterX(), part.getCenterY());
            }
        } else {
            if (ymov == -1) {
                t.rotate(Math.toRadians(180),part.getCenterX(), part.getCenterY());
            } else {
                t.rotate(Math.toRadians(0),part.getCenterX(), part.getCenterY());
            }
        }
        g.setTransform(t);
        g.drawImage(SnakeHead, (int) part.getMinX(),(int) part.getMinY(), null);
        i=0;
        g.setTransform(new AffineTransform());
        if (part.intersects(TreeGroundKol) || part.intersects(Tree1Kol) || part.intersects(Tree2kol) || part.intersects(Tree3kol) || part.intersects(LakeKol)) {
            Dead = true;
            xmov = 0;
            ymov = 0;
        }

        g.drawImage(TreeGround,320,186,null);
        g.drawImage(Tree1, 677, 607, null);
        g.drawImage(Tree2, 444, 261, null);
        g.drawImage(Tree3, 45, 28, null);
        

        if (helptime > 0 && data.getBoolean("help")) {
            helptime --;
            part.setBounds(AppleX*20-20,AppleY*20-20, 20, 20);
            g.setColor(Color.RED);
            g.fill(part);
        }

        if (Dead) {
            g.setTransform(new AffineTransform());
            g.drawImage(DeadImage, 350, 350, null);
        }

        try {
            Thread.sleep(data.getInt("ticktime"));
        } catch (InterruptedException e) {}
        if (!Dead) {
            xkords.add(xkords.get(xkords.size()-1)+xmov);
            ykords.add(ykords.get(ykords.size()-1)+ymov);
            if (!ToAdd) {
                ykords.remove(0);
                xkords.remove(0);
            } else {ToAdd = false;lx++;ly++;}
            Kol();
        }

        if (xkords.get(lx-1) == 0){
            xkords.set(lx-1,50);
        }
        if (xkords.get(lx-1) == 51) {
            xkords.set(lx-1,0);
        }
        if (ykords.get(ly-1) == 0){
            ykords.set(ly-1,50);
        }
        if (ykords.get(ly-1) == 51) {
            ykords.set(ly-1,0);
        }

        if(eat.getFramePosition() == 78959) {
            eat.setFramePosition(0);
            eat.stop();
        }

        if (xkords.get(lx-1) == AppleX && ykords.get(ly-1) == AppleY) {
            ToAdd = true;
            eat.start();
            GenApplePos();
        }
        return result;
    }
    public void keyPressed(KeyEvent event) throws IOException {
        if (!Dead) {
        if (event.getKeyCode() == KeyEvent.VK_A || event.getKeyCode() == KeyEvent.VK_LEFT) {
			xmov = -1;
            ymov = 0;
		}
        if (event.getKeyCode() == KeyEvent.VK_D || event.getKeyCode() == KeyEvent.VK_RIGHT) {
			xmov = 1;
            ymov = 0;
		}
        if (event.getKeyCode() == KeyEvent.VK_S || event.getKeyCode() == KeyEvent.VK_DOWN) {
			xmov = 0;
            ymov = 1;
		}
        if (event.getKeyCode() == KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_UP) {
			xmov = 0;
            ymov = -1;
		}
        } else {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                if (data.getInt("highscore") < xkords.size()) {
                    data.put("highscore", xkords.size());
                    Main.INSTANCE.saveData();
                }
                Main.INSTANCE.reset();
            }
        }
    }
    
    public void keyReleased(KeyEvent event) {}
    private void Kol() {
        i = 0;
        while (i != lx -2) {
        i++;
        if (xkords.get(lx-1) == xkords.get(i)) {
            if (ykords.get(ly-1) == ykords.get(i)) {
                Dead = true;
                xmov = 0;
                ymov = 0;
            }
        }
    }
    i = 0;
    }

    private void GenApplePos() {
        helptime = 5;
        AppleX = new Random().nextInt(48)+1;
        AppleY = new Random().nextInt(48)+1;
        int tempx = (int) part.getLocation().getX(),tempy = (int) part.getLocation().getY();
        part.setLocation(AppleX*20-20, AppleY*20-20);
        if (part.intersects(TreeGroundKol) || part.intersects(Tree1Kol) || part.intersects(Tree2kol) || part.intersects(Tree3kol) || part.intersects(LakeKol)) {
            part.setLocation(tempx,tempy);
            GenApplePos();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
         
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
         
        
    }
}