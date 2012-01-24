/*
 * @author Kyle Kemp
 */
package hexapixel.notifier;

import org.eclipse.swt.graphics.Image;

import hexapixel.cache.ImageCache;

/**
 * The Enum NotificationType.
 */
public enum NotificationType {
    
    /** The ERROR. */
    ERROR(ImageCache.getImage("error.png")),
    
    /** The DELETE. */
    DELETE(ImageCache.getImage("delete.png")),
    
    /** The WARN. */
    WARN(ImageCache.getImage("warn.png")),
    
    /** The SUCCESS. */
    SUCCESS(ImageCache.getImage("ok.png")),
    
    /** The INFO. */
    INFO(ImageCache.getImage("info.png")),
    
    /** The LIBRARY. */
    LIBRARY(ImageCache.getImage("library.png")),
    
    /** The HINT. */
    HINT(ImageCache.getImage("hint.png")),
    
    /** The PRINTED. */
    PRINTED(ImageCache.getImage("printer.png")),
    
    /** The CONNECTIO n_ terminated. */
    CONNECTION_TERMINATED(ImageCache.getImage("terminated.png")),
    
    /** The CONNECTIO n_ failed. */
    CONNECTION_FAILED(ImageCache.getImage("connecting.png")),
    
    /** The CONNECTED. */
    CONNECTED(ImageCache.getImage("connected.png")),
    
    /** The DISCONNECTED. */
    DISCONNECTED(ImageCache.getImage("disconnected.png")),
    
    /** The TRANSACTIO n_ ok. */
    TRANSACTION_OK(ImageCache.getImage("ok.png")),
    
    /** The TRANSACTIO n_ fail. */
    TRANSACTION_FAIL(ImageCache.getImage("error.png"));

    /** The _image. */
    private Image _image;

    /**
     * Instantiates a new notification type.
     *
     * @param img the img
     */
    private NotificationType(Image img) {
        _image = img;
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    public Image getImage() {
        return _image;
    }
}
