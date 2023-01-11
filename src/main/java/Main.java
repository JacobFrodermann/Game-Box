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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Main {
	private static final File DATA_FILE = new File("Data.dat");
	
	public static Main INSTANCE;

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public JFrame frame;
	private Canvas canvas;
	private Game currentGame;

	public JSONObject data;

	public static void main(String[] args) throws IOException, InterruptedException {
		INSTANCE = new Main();
		INSTANCE.init();
		if (INSTANCE.currentGame != null) {
			while(true) {
				render(INSTANCE.canvas, INSTANCE.draw(INSTANCE.canvas.getSize()));
				Thread.sleep(1000 / 60);
			}
		}
	}

	public static InputStream baInputStream(InputStream in) throws IOException {
		return new ByteArrayInputStream(in.readAllBytes());
	}

	public void handleError(Throwable e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "GameBox", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public void init() throws IOException {
		readData();
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		canvas = new Canvas();
		frame.add(canvas);
		canvas.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				try {
					currentGame.keyPressed(e);
				} catch (Throwable e1) { handleError(e1); }
			}
			public void keyReleased(KeyEvent e) {
				try {
					currentGame.keyReleased(e);
				} catch (Throwable e1) { handleError(e1); }
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					currentGame.mouseMoved(e);
				} catch (Throwable e1) { handleError(e1); }
			}
			public void mouseDragged(MouseEvent e) {}
		});
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					currentGame.mouseClicked(e);
				} catch (Throwable e1) { handleError(e1); }
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		reset();
		frame.setVisible(true);
		canvas.createBufferStrategy(2);
		canvas.requestFocus();
	}

	public BufferedImage draw(Dimension size) {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		try {
			g.drawImage(currentGame.draw(size), 0, 0, null);
		} catch (Throwable e1) { handleError(e1); }
		
		return result;
	}

	public static void render(Canvas canvas, BufferedImage img) {
		BufferStrategy bs = canvas.getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void readData() throws IOException {
		try {
			data = new JSONObject(IOUtils.toString(new FileInputStream(DATA_FILE), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
			resetData();
		}
	}
	public void resetData(){
		try {
			IOUtils.write(Main.class.getClassLoader().getResources("Default.dat").toString(), new FileOutputStream("Data.dat"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveData() {
		try {
			IOUtils.write(data.toString(), new FileOutputStream(DATA_FILE), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		switchGame(data.getJSONObject("selectionScreen"));
	}

	@SuppressWarnings("unchecked")
	public void switchGame(JSONObject gameInfo) {
		try {
			Class<? extends Game> clazz = (Class<? extends Game>) Class.forName(gameInfo.getString("class"));
			Constructor<? extends Game> constructor = clazz.getDeclaredConstructor(JSONObject.class);
			currentGame = constructor.newInstance(gameInfo.getJSONObject("data"));

			if(true) { // TODO check whether user has changed window bound previously
				// Window size
				assert gameInfo.has("baseScreenSizeW") == gameInfo.has("baseScreenSizeH");
				if(gameInfo.has("baseScreenSizeW") && gameInfo.has("baseScreenSizeH")) {
					frame.setSize(gameInfo.getInt("baseScreenSizeW"), gameInfo.getInt("baseScreenSizeH"));
				}

				// Re-center
				double w = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
				double h = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
				frame.setLocation((int)(w/2-frame.getWidth()/2),(int)(h/2-frame.getHeight()/2));
			}

			// Other window properties
			frame.setTitle("GameBox" + (gameInfo.has("name")?" - "+gameInfo.getString("name"):""));
			// TODO set icon
		} catch (Throwable e) {
			handleError(e);
		}
	}
}