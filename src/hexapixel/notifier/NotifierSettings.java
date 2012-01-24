/*
 * @author Kyle Kemp
 */
package hexapixel.notifier;

import org.eclipse.swt.graphics.Color;

import hexapixel.cache.ColorCache;

/**
 * The Class NotifierSettings.
 */
public class NotifierSettings {

	// title foreground color
	/** The title color. */
	public static Color titleColor = ColorCache.getColor(40, 73, 97);

	// text foreground color
	/** The text color. */
	public static Color textColor = titleColor;

	// shell border color
	/** The border color. */
	public static Color borderColor = ColorCache.getColor(40, 73, 97);

	// shell gradient background color - top
	/** The background top gradient. */
	public static Color backgroundTopGradient = ColorCache.getColor(226, 239,
			249);

	// shell gradient background color - bottom
	/** The background bottom gradient. */
	public static Color backgroundBottomGradient = ColorCache.getColor(177,
			211, 243);

}
