//META<inuse=true>
//META<author=EllyMent>

function getContextCommands() {
	return [
	"Slap","Poke","DCCSend"
	];
}

function Slap(connection, targetUser, targetChannel){
	connection.sendAction(targetChannel, "slaps "+targetUser.getNick()+"!");
}

function Poke(connection, targetUser, targetChannel){
	connection.sendAction(targetChannel, "pokes "+targetUser.getNick()+" with a sharp stick!");
}

function DCCSend(connection, targetUser, targetChannel){
	var filename = util.openFileDialog();
	util.error(filename);
//TODO: Make this invoke work.
//	util.invoke("DCC.js", "sendFile"(filename, targetUser.getNick(), 30);
}


