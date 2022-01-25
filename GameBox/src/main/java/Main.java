import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;

class Main {
	public static Main INSTANCE;

	public JFrame frame;
	public Canvas canvas;
	public Game currentGame;

	public String Types;
	public Map<String,Object> Settings = new LinkedHashMap<String,Object>();
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
		ReadSettings ReadSet = new ReadSettings();
		ReadSet.start();
		frame = new JFrame();
		frame.setTitle("GameBox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);
		frame.setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-200,Toolkit.getDefaultToolkit().getScreenSize().height/2-300,400,600);
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
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				currentGame.mouseMoved(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentGame.mouseClicked(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				 
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				 
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				 
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				 
				
			}
		});
		currentGame = new GameSelectionScreen();
		frame.setVisible(true);
		canvas.createBufferStrategy(2);
		canvas.requestFocus();

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
}