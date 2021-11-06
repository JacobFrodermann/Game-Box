import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.Buffer;
import java.sql.Array;
import java.awt.Color;
import java.awt.*;

import javax.imageio.ImageIO;

public class FlappyBird implements Game {
	Array pipes[];
	BufferedImage bird;
	BufferedImage section;
	BufferedImage end;
	Color Sky;

	Double VelY;
	Double BirdY;
	int Score;
	Boolean alive;

	public BufferedImage draw(Dimension size) throws IOException{

		VelY = 0.0;
		BirdY = 300.0;
		Score = 0;
		alive = true;

		Sky = new Color(52, 174, 235);
		bird = ImageIO.read(new File("The Bird.png"));
		section = ImageIO.read(new File("PipeSegment"));
		end = ImageIO.read(new File("PipeEnd"));
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.setColor(Sky);;
		g.fill(new Rectangle(new Point(),size));
		while alive {
			g.drawImage(bird, 20, BirdY, 40, 40, Sky, null);

		}
		return result;
	}
}
