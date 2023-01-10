import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class AssetCache {
    public static final LoadingCache<String, BufferedImage> IMAGES = CacheBuilder.newBuilder()
        .build(new CacheLoader<String, BufferedImage>() {
            public BufferedImage load(String name) throws Exception {
                if(name == null) return null;
                return ImageIO.read(ClassLoader.getSystemResourceAsStream(name + ".png"));
            }
        });
}
