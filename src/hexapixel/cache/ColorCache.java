package hexapixel.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Class for caching colors.
 *
 * @author Emil
 */
public final class ColorCache {

	/** The Constant BLACK. */
	public static final RGB BLACK = new RGB(0, 0, 0);
	
	/** The Constant WHITE. */
	public static final RGB WHITE = new RGB(255, 255, 255);

	/** The _color table. */
	private static Map<RGB, Color> _colorTable;
	
	/** The _instance. */
	private static ColorCache _instance;

	static {
		_colorTable = new HashMap<RGB, Color>();
		new ColorCache();
	}

	/**
	 * Instantiates a new color cache.
	 */
	private ColorCache() {
		_instance = this;
	}

	/**
	 * Gets the single instance of ColorCache.
	 *
	 * @return single instance of ColorCache
	 */
	public static ColorCache getInstance() {
		return _instance;
	}

	/**
	 * Disposes of all colors. DO ONLY CALL THIS WHEN YOU ARE SHUTTING DOWN YOUR
	 * APPLICATION!
	 */
	public static void disposeColors() {
		Iterator<Color> e = _colorTable.values().iterator();
		while (e.hasNext())
			e.next().dispose();

		_colorTable.clear();
	}

	/**
	 * Gets the white.
	 *
	 * @return the white
	 */
	public static Color getWhite() {
		return getColorFromRGB(new RGB(255, 255, 255));
	}

	/**
	 * Gets the black.
	 *
	 * @return the black
	 */
	public static Color getBlack() {
		return getColorFromRGB(new RGB(0, 0, 0));
	}

	/**
	 * Gets the color from rgb.
	 *
	 * @param rgb the rgb
	 * @return the color from rgb
	 */
	public static Color getColorFromRGB(RGB rgb) {
		Color color = _colorTable.get(rgb);

		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			_colorTable.put(rgb, color);
		}

		return color;
	}

	/**
	 * Gets the color.
	 *
	 * @param r the r
	 * @param g the g
	 * @param b the b
	 * @return the color
	 */
	public static Color getColor(int r, int g, int b) {
		RGB rgb = new RGB(r, g, b);
		Color color = _colorTable.get(rgb);

		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			_colorTable.put(rgb, color);
		}

		return color;
	}

}