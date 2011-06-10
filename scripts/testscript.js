function onPrivateMessage(event) {
}

function post_onMessage(event) {
	gui.window(event.getUser().getNick() +" - "+event.getChannel().getName(),event.getMessage());
}

function onDisconnect(event) {

}