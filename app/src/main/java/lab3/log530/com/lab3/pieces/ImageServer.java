package lab3.log530.com.lab3.pieces;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//TODO
/*
import java.awt.Image;
import javax.imageio.ImageIO;
*/

/**
 * Serves cached images of requsted size.
 *
 * This will cache the recent requests so it's not hitting the disk
 * every time the display needs an image.
 */
public final class ImageServer {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("ImageServer");

    /** The image cache. */
    //TODO
    /*private static Map<String, Image> cache =
        new HashMap<String, Image>();*/

    /**
     * Hidden constructor.
     */
    private ImageServer() {
    }

    /**
     * Return named image scaled to given size.
     *
     * @param name name of the image
     * @return     the requested image
     */
    //TODO
    public static void/*Image*/ getTile(final String name) {
        /*Image cached = cache.get(name);
        if (cached != null) {
            return cached;
        }

        String file = name + ".png";
        try {
            Image i = ImageIO.read(ImageServer.class.getResource(file));
            cache.put(name, i);
            return i;
        } catch (java.io.IOException e) {
            String message = "Failed to read image: " + file + ": " + e;
            LOG.severe(message);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            String message = "Failed to find image: " + file + ": " + e;
            LOG.severe(message);
            System.exit(1);
        }
        return null;*/
    }
}
