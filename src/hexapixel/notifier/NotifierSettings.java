package hexapixel.notifier;

import org.eclipse.swt.graphics.Color;

import hexapixel.cache.ColorCache;

public class NotifierSettings {

	// title foreground color
	public static Color titleColor = ColorCache.getColor(40, 73, 97);

	// text foreground color
	public static Color textColor = titleColor;

	// shell border color
	public static Color borderColor = ColorCache.getColor(40, 73, 97);

	// shell gradient background color - top
	public static Color backgroundTopGradient = ColorCache.getColor(226, 239,
			249);

	// shell gradient background color - bottom
	public static Color backgroundBottomGradient = ColorCache.getColor(177,
			211, 243);

}
