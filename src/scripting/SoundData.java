/*
 * @author Kyle Kemp
 */
package scripting;

import org.wishray.copernicus.Sound;
import org.wishray.copernicus.SoundReference;

/**
 * The Class SoundData.
 */
public class SoundData {
	
	/** The current sound. */
	public static Sound curSound = null;
	
	/** The sound reference. */
	public static SoundReference soundRef = null;
	
	/** Whether or not the sound is paused. */
	public static boolean isPaused = false;
	
	/** The current sound id. */
	public static int curId = 0;

}
