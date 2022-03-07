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
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

class Main {
	private static final File DATA_FILE = new File("Data.dat");
	
	public static Main INSTANCE;

	public JFrame frame;
	private Canvas canvas;
	private Game currentGame;

	public JSONObject data;

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
			public void mouseDragged(MouseEvent e) {}
		});
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				currentGame.mouseClicked(e);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		switchGame(data.getJSONObject("selectionScreen"));
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

	public void load() {
		try {
			readData();
		} catch (Exception e) {
			e.printStackTrace();
			resetData();
			saveAll();
		}
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
	public void saveAll() {
		try {
			IOUtils.write(data.toString(), new FileOutputStream(DATA_FILE), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void switchGame(JSONObject gameInfo) {
		try {
			String gameClassName = gameInfo.getString("class");
			Class<? extends Game> game = (Class<? extends Game>) Class.forName(gameClassName);
			canvas.setPreferredSize(new Dimension(gameInfo.getInt("baseScreenSizeW"), gameInfo.getInt("baseScreenSizeH")));
			frame.pack();
			frame.setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2 - frame.getWidth() / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - frame.getHeight() / 2
			);
			frame.setTitle("GameBox" + (gameInfo.has("name") ? " - " + gameInfo.getString("name") : ""));
			frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream(gameInfo.getString("icon"))));
			frame.setResizable(gameInfo.has("resizable") && gameInfo.getBoolean("resizable"));
			currentGame = game.getConstructor(JSONObject.class).newInstance(gameInfo.getJSONObject("data"));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error");
		}
	}
}
