import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.JFrame;
import org.apache.commons.io.IOUtils;

class Main {
	public static Main INSTANCE;

	public JFrame frame;
	public Canvas canvas;
	public Game currentGame;
	public List<String> Read;

	public static void main(String[] args) throws IOException, InterruptedException {
		INSTANCE = new Main();
		INSTANCE.init();
		while(true) {
			render(INSTANCE.canvas, INSTANCE.draw(INSTANCE.canvas.getSize()));
			Thread.sleep(1000 / 60);
		}
	}

	public static InputStream baInputStream(InputStream in) throws IOException {
		return new ByteArrayInputStream(in.readAllBytes());
	}

	public void init() throws IOException {
		frame = new JFrame();
		frame.setTitle("GameBox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);
		frame.setBounds(new Rectangle(646,219,400,600));
		canvas = new Canvas();
		frame.add(canvas);
		canvas.setPreferredSize(new Dimension(400, 600));
		frame.pack();
		canvas.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				try {
					currentGame.keyPressed(e);
				} catch (IOException e1) {}
			}
			public void keyReleased(KeyEvent e) {
				currentGame.keyReleased(e);
			}
		});
		frame.setVisible(true);
		canvas.createBufferStrategy(2);
		canvas.requestFocus();

		if (new File("Settings.txt").exists()) {
            Read = IOUtils.readLines(new FileInputStream(new File("Settings.txt")),StandardCharsets.UTF_8);
            } else {
                reset();
            }
		if (Read.size() != 12) {
			System.out.println("reseted");
			System.out.println(Read.size());
			reset();
		}

		currentGame = new GameSelectionScreen();
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Rendering
		g.drawImage(currentGame.draw(size), 0, 0, null);

		return result;
	}

	public static void render(Canvas canvas, BufferedImage img) {
		BufferStrategy bs = canvas.getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void reset() throws FileNotFoundException, IOException {
		IOUtils.write("FlappyBird:\n    Gravity=0.125\n    RotationFaktor=1\n    JumpHeight=4\nPong:\n    Xamp=1\n    Yamp=1\n    Linespeed=6\nSnake:\n    help=false\n    DeadlyLake=true\n    ticktime=60", new FileOutputStream(new File("Settings.txt")), StandardCharsets.UTF_8);
		try {
			Read = IOUtils.readLines(new FileInputStream(new File("Settings.txt")),StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Read ater Write Failed\nManual input");
			IOUtils.close();
			Read = IOUtils.readLines(new FileInputStream(new File("Settings.txt")),StandardCharsets.UTF_8);
		}
	}
}