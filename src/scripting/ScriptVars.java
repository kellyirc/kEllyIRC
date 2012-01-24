/*
 * @author Kyle Kemp
 */
package scripting;

import org.pircbotx.Channel;
import org.wishray.copernicus.Sound;

import connection.KEllyBot;

/**
 * The Class ScriptVars.
 */
public final class ScriptVars {
	
	/** The current channel. */
	public static Channel curChannel = null;
	
	/** The current connection. */
	public static KEllyBot curConnection = null;
	
	/**
	 * Current channel.
	 *
	 * @return the channel
	 */
	public Channel currentChannel() { return curChannel;}
	
	/**
	 * Current connection.
	 *
	 * @return the k elly bot
	 */
	public KEllyBot currentConnection() { return curConnection;}
	
	/**
	 * Current sound.
	 *
	 * @return the sound
	 */
	public Sound currentSound() { return SoundData.curSound;}
	
}
