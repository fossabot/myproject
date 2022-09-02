package com.demoapp.core.services.gfx;

import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * An Image Utilities library to perform simple operations on images
 *
 * @author Frédéric Delorme
 * @since 1.0.1
 */
public class ImageUtils {

    /**
     * Split the img into multiple tiles of size tileWidth x tileHeight.
     *
     * @param img        the source image to be split.
     * @param offsetX    the starting offset on horizontal axis
     * @param offsetY    the starting offset on vertical axis
     * @param tileWidth  the width of a tile
     * @param tileHeight the height of tile
     * @return a list of tiles as {@link BufferedImage} image.
     */
    public static List<BufferedImage> splitImageToTile(BufferedImage img, int offsetX, int offsetY, int tileWidth, int tileHeight) {
        List<BufferedImage> tiles = new ArrayList<>();
        int maxWidth = (img.getWidth() - offsetX);
        int maxHeight = (img.getHeight() - offsetY);
        for (int ix = offsetX; ix < maxWidth; ix += tileWidth) {
            for (int iy = offsetX; iy < maxHeight; iy += tileHeight) {
                tiles.add(crop(img, ix, iy, tileWidth, tileHeight));
            }
        }
        return tiles;
    }

    /**
     * Crop the img at offsetX,offsetY for a size of width x height.
     *
     * @param img     the Image to be cropped.
     * @param offsetX the horizontal offset to crop from.
     * @param offsetY the vertical offset to crop from.
     * @param width   the width of image to be cropped.
     * @param height  the height of image to be cropped.
     * @return the modified {@link BufferedImage} image.
     */
    public static BufferedImage crop(BufferedImage img, int offsetX, int offsetY, int width, int height) {
        return img.getSubimage(offsetX, offsetY, width, height);
    }
}
