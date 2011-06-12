package hexapixel.notifier;

import lombok.Data;
import lombok.Getter;

import org.eclipse.swt.graphics.Color;

import hexapixel.cache.ColorCache;

@Data
public class NotifierSettings {

	// title foreground color
	@Getter
	private static Color titleColor = ColorCache.getColor(40, 73, 97);

	// text foreground color
	@Getter
	private static Color textColor = titleColor;

	// shell border color
	@Getter
	private static Color borderColor = ColorCache.getColor(40, 73, 97);

	// shell gradient background color - top
	@Getter
	private static Color backgroundTopGradient = ColorCache.getColor(226, 239,
			249);

	// shell gradient background color - bottom
	@Getter
	private static Color backgroundBottomGradient = ColorCache.getColor(177,
			211, 243);

}
