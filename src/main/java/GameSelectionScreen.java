import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameSelectionScreen implements Game {
	private static final Color sky = new Color(52, 174, 235);
	private static final Color selectionColor = new Color(255, 60, 0);
	private JSONArray games;
	private int selected = 0;
	private double anim = 0;
	private double animMovement = 0;
	private int XRow = 0;
	private boolean settings = false;
	JSONObject data;
	private Map<Integer,Character> options = new HashMap<Integer, Character>();
	public GameSelectionScreen(JSONObject data) throws IOException {
		games = data.getJSONArray("games");

		try {
		int yOff = 20;
		for(int i = 0; i<games.length();i++) {
			JSONObject game = games.getJSONObject(i);
			yOff += 30;
			JSONObject x = game.getJSONObject("settings");
			for(int j = 0; j<x.length(); j++){
				Object val = x.toMap().values().toArray()[j];
				if (val.getClass() == Integer.class || val.getClass() == java.math.BigDecimal.class) {
					options.put(yOff, 'i');
				} else if (val.getClass() == Boolean.class){
					options.put(yOff, 'b');
				} else if (val.getClass() == String.class) {
					options.put(yOff, 's');
				}
				yOff +=20;
			}
		} } catch (Throwable t) {Main.INSTANCE.handleError(t);}
	}

	public BufferedImage draw(Dimension size) throws ExecutionException {
		BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) result.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Rendering
		g.setColor(sky);
		g.fill(new Rectangle(new Point(), size));
		g.drawImage(AssetCache.IMAGES.get("Gear"), 710, 20, 50, 50, null);

		if(!settings) {
			for(int i = 0; i < games.length(); i++) {
				XRow = ((int) Math.floor((i+1)/4))*300;
				g.drawImage(AssetCache.IMAGES.get(games.getJSONObject(i).getString("thumbnail")), 75 + XRow, 50 + i * 190 - XRow * 2, 250, 140, null);
				if(i == selected) {
					g.setColor(selectionColor);
					int a = (int) Math.round(8 * Math.sin(anim));
					g.fillPolygon(new int[] { 35 + a + XRow, 35 + a + XRow , 55 + a + XRow }, new int[] { (int) (105 + (i + animMovement) * 190) - XRow*2, (int) (135 + (i + animMovement) * 190) - XRow*2, (int) (120 + (i + animMovement) * 190 - XRow*2) }, 3);
					g.fillPolygon(new int[] { 400 - 35 - a + XRow, 400 - 35 - a + XRow, 400 - 55 - a + XRow}, new int[] { (int) (105 + (i + animMovement) * 190) - XRow*2, (int) (135 + (i + animMovement) * 190) - XRow*2, (int) (120 + (i + animMovement) * 190) - XRow*2 }, 3);
				}
			}
			anim += 0.09;
			animMovement *= 0.9;
		} else {
			g.setColor(Color.BLACK);
			int yOff = 20;
			try {
			for(int i = 0; i<games.length();i++) {
				JSONObject game = games.getJSONObject(i);
				g.drawString(game.getString("name"), 50,yOff);
				yOff += 30;
				JSONObject x = game.getJSONObject("settings");
				for(int j = 0; j<x.length(); j++){
					Object val = x.toMap().values().toArray()[j];
					String key = (String)x.toMap().keySet().toArray()[j];
					if (val.getClass() == Integer.class || val.getClass() == java.math.BigDecimal.class) {
						g.drawString(String.valueOf(val), 500, yOff);
					} else if (val.getClass() == Boolean.class){
						if ((Boolean)val) {
							g.drawImage(AssetCache.IMAGES.get("Switch"), 500, yOff-8, null);
						} else {
							g.drawImage(AssetCache.IMAGES.get("SwitchOff"), 500, yOff-8, null);
						}
					} else if (val.getClass() == String.class) {
						g.drawString((String) val, 500, yOff);	
					}
					g.drawString(key, 300, yOff);	
					yOff +=20;
				}
			} } catch (Throwable t) {Main.INSTANCE.handleError(t);}
		}
		return result;
	}

	public void keyPressed(KeyEvent event) {
		if(!settings) {
			if(event.getKeyCode() == KeyEvent.VK_ENTER) {
				Main.INSTANCE.switchGame(games.getJSONObject(selected));
			}
			if(event.getKeyCode() == KeyEvent.VK_UP) {
				if(selected > 0) {
					selected--;
					animMovement += 1;
				}
			}
			if(event.getKeyCode() == KeyEvent.VK_DOWN) {
				if(selected < games.length()) {
					selected++;
					animMovement -= 1;
				}
			}
		}
	}

	public void keyReleased(KeyEvent event) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (new Rectangle(710,20,50,50).contains(e.getPoint())) {
			settings = !settings;
		}
		if(!settings) {
			for (int i = 0; i<Math.floor(games.length()*3);i++){
				for (int j = 0;j<3;j++){
					if (new Rectangle(3+300*i,3+190*j,250,140).contains(e.getPoint())) {
						selected = i*3+j;
						Main.INSTANCE.switchGame(games.getJSONObject(selected));
					}
				}
			}
		} else {
			int x = e.getPoint().x;
			int clickY = e.getPoint().y;
			if (x > 500 && x < 540) {
				System.out.println(x + " , " + clickY);
				options.forEach((Integer y,Character t) -> {
					//System.out.println(y);
					if (y-10<clickY && y+10>clickY) {
						System.out.println(t);
						switch (t) {
							case 'b':
								
						}
					}
				});
			}
		}
	}

	public void mouseMoved(MouseEvent e) {}
}