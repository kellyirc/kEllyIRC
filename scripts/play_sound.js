
function play(connection, sound_path) {

	if(!util.playSound(sound_path)) {
		util.error("Your sound was unable to be played; either it could not be found or it is unsupported.");
	}
	util.error(global.currentSound().getName());
	connection.sendMessage(global.currentChannel(), "Now playing: "+sound.currentSound().getName());

}

function stop(connection, args) {
	util.stopSound();
}
