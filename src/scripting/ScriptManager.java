package scripting;

public class ScriptManager {
	/* TODO: find a way to keep a constantly updated list of files
	 * 
	 * To execute scripts:
	 * 	every time a message is recieved or sent, check every file in a directory (/scripts)
	 * 	if the file has changed (check name vs list of objects, 
	 * 		somehow work some magic with last-modified dates), 
	 * 		load the new version of it
	 * 	cache the files (or modify existing ones) as they're loaded to reduce load times in the future
	 * 	create a "script" object which contains the contents of the script in question 
	 * 		(assuming it's not cached already -- this object will require lots of data)
	 * 	run through all current scripts (need to keep a "tagging" system to more quickly access objects)
	 *  	1. check script language
	 *  	2. load proper engine (or just have all three loaded at any time anyway)
	 *  	3. execute the script if it contains the function (maintain a list of functions in a script)
	 *  	RUN IN SEPARATE THREAD. 
	 */
}
