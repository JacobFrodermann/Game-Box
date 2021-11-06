import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FlappyBird implements Game {
	BufferedImage bird;
	BufferedImage section;
	BufferedImage end;
	Color sky = new Color(52, 174, 235);

	Double VelY = 0.0;
	Double BirdY = 300.0;
	int Score = 0;

	public FlappyBird() throws IOException {
		bird = ImageIO.read(new File("The Bird.png"));
		section = ImageIO.read(new File("PipeSegment.png"));
		end = ImageIO.read(new File("PipeEnd.png"));
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));
		g.drawImage(bird, 20, (int) Math.round(BirdY), 40, 40, null);

		return result;
	}
}
