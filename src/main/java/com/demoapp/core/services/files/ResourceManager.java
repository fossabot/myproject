package com.demoapp.core.services.files;

import com.demoapp.core.services.gfx.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link ResourceManager} is the internal cache for any file resource need by the {@link com.demoapp.core.Application}:
 * {@link BufferedImage}, {@link java.awt.Font}, or the coming `Sound`, all those resources would be loaded.
 *
 * @author Frédéric Delorme
 * @since 1.0.1
 */
public class ResourceManager {
    private static Map<String, Object> cache = new ConcurrentHashMap<>();

    /**
     * Add a new resource to the internal cache.
     *
     * @param path the file path to the required resource.
     */
    public static void addResource(String path) {
        String[] fileext = path.split("\\.");
        switch (fileext[1].toUpperCase(Locale.ROOT)) {
            case "PNG":
            case "JPG":
            case "JPEG":
                try {
                    BufferedImage img = ImageIO.read(
                            ResourceManager.class.getResourceAsStream(path));
                    cache.put(
                            img.getClass().getCanonicalName() + ":" + path,
                            img);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.err.printf("ERR : ResourceManager | Unknown resource type for %s%n", path);
                break;
        }
    }

    /**
     * Retrieve a {@link BufferedImage} from the internal {@link ResourceManager#cache}.
     * If the BufferedImage resource type does not already exist in the cache, it is automatically preloaded,
     * and served.
     *
     * @param path the file path to the required file of the image.
     * @return the corresponding {@link BufferedImage}.
     */
    public static BufferedImage getImage(String path) {
        if (!cache.containsKey(BufferedImage.class.getCanonicalName() + ":" + path)) {
            addResource(path);
        }
        return (BufferedImage) cache.get(BufferedImage.class.getCanonicalName() + ":" + path);
    }


    /**
     * Retrieve a {@link BufferedImage} from the internal {@link ResourceManager#cache}.
     * If the BufferedImage resource type does not already exist in the cache, it is automatically preloaded,
     * and served.
     *
     * @param path the file path to the required file of the image.
     * @param x    the horizontal offset to crop from.
     * @param y    the vertical offset to crop from.
     * @param w    the width of image to be cropped.
     * @param h    the height of image to be cropped.
     * @return the corresponding {@link BufferedImage}.
     */
    public static BufferedImage getCroppedImage(String path, int x, int y, int w, int h) {
        if (!cache.containsKey(BufferedImage.class.getCanonicalName() + ":" + path)) {
            addResource(path);
        }
        BufferedImage imgCached = (BufferedImage) cache.get(BufferedImage.class.getCanonicalName() + ":" + path);
        return ImageUtils.crop(imgCached, x, y, w, h);
    }
}
