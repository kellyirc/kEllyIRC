/*
comment
*/
//META<inuse=false>

function fact(connection, sargs) {
	var nick = global.currentConnection().getNick();
	
	util.error(global.currentConnection().getNick());

	connection.sendMessage(global.currentChannel(), nick);
}

function test() {
	var poop = 0;
}
