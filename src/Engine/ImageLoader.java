package Engine;

import Utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// contains a bunch of helpful methods for loading images file into the game
public class ImageLoader {

    // loads an image and sets its transparent color to the one defined in the Config class
    public static BufferedImage load(String imageFileName) {
        return ImageLoader.load(imageFileName, Config.TRANSPARENT_COLOR);
    }

    // loads an image and allows the transparent color to be specified
    public static BufferedImage load(String imageFileName, Color transparentColor) {
        System.out.println("Loading image " + imageFileName);
        try {
            BufferedImage initialImage = ImageIO.read(ImageLoader.class.getClassLoader().getResource(Config.RESOURCES_PATH + imageFileName));
            return ImageUtils.transformColorToTransparency(initialImage, transparentColor);
        } catch (IOException e) {
            System.out.println("Unable to find file " + Config.RESOURCES_PATH + imageFileName);
            throw new RuntimeException(e);
        }
    }

    // loads a piece of an image from an image file and sets its transparent color to the one defined in the Config class
    public static BufferedImage loadSubImage(String imageFileName, int x, int y, int width, int height) {
        return ImageLoader.loadSubImage(imageFileName, Config.TRANSPARENT_COLOR, x, y, width, height);
    }

    // loads a piece of an image from an image file and allows the transparent color to be specified
    public static BufferedImage loadSubImage(String imageFileName, Color transparentColor, int x, int y, int width, int height) {
        try {
            BufferedImage initialImage = ImageIO.read(ImageLoader.class.getClassLoader().getResource(Config.RESOURCES_PATH + imageFileName));
            BufferedImage transparentImage = ImageUtils.transformColorToTransparency(initialImage, transparentColor);
            return transparentImage.getSubimage(x, y, width, height);
        } catch (IOException e) {
            System.out.println("Unable to find file " + Config.RESOURCES_PATH + imageFileName);
            throw new RuntimeException(e);
        }
    }
}
