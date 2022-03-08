import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.awt.RenderingHints;
import java.awt.Color;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.JSONObject;

import java.awt.Graphics2D;

public class Tetris implements Game{


	Color[][] blocks = new Color[10][22], moving = new Color[4][4];
	Color gray = new Color(0x32333b),gray2 = new Color(0x212124);

	public Tetris(JSONObject data){
		for (int x = 0;x < blocks.length;x++) {
			for (int y = 0;y < blocks[0].length;y++) {
				blocks[x][y] = gray2;
			}
		}
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.setColor(gray);
		g.fillRect(0, 0, 25, 660);
		g.fillRect(325, 0, 25, 660);
		int i = 0;
		for (Color[] x:blocks) {
			int j = 0;
			for (Color y:x) {
				g.setColor(y);
				g.fillRect(25+30*i, 30*j, 30, 30);
				g.setColor(y.darker());
				g.fillRect(30+30*i, 5+30*j, 20, 20);
				j++;
			}
			i++;
		}
		return result;
	}

	
	public void keyPressed(KeyEvent event) throws IOException {
		
		
	}

	
	public void keyReleased(KeyEvent event) {
		
		
	}

	
	public void mouseClicked(MouseEvent e) {
		
		
	}

	
	public void mouseMoved(MouseEvent e) {
		
		
	}
	void newPart(){
		Color c =Color.red;
		switch(new Random().nextInt(5)){
			case 1://umgedretes T
				moving[2][0]=c;
				moving[2][1]=c;
				moving[2][2]=c;
				moving[1][3]=c;
				moving[2][3]=c;
				moving[3][3]=c;
				break;
			case 2://|
				moving[1][0]=c;
				moving[1][1]=c;
				moving[1][2]=c;
				moving[1][3]=c;
				moving[1][4]=c;
				break;
			case 3://block
				moving[1][1]=c;
				moving[2][1]=c;
				moving[1][2]=c;
				moving[2][2]=c;
				break;
			case 4://+
				moving[1][0]=c;
				moving[0][1]=c;
				moving[1][1]=c;
				moving[2][1]=c;
				moving[1][2]=c;
				break;
		}
	}
	void rotate(){
		for(int i=0; i<moving.length; i++) {  
			for(int j=i; j<moving[i].length; j++)  {  
			//checks if i is not equal to j because in transpose moving diagonal elements will not swap  
			if(i!=j)  {  
				//swapping elements  
				Color temp = moving[i][j];  
				moving[i][j]=moving[j][i];  
				moving[j][i]=temp;  
				}  
			}  
		}  
	}
}
