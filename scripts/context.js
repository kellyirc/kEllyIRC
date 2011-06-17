function getContextCommands() {
	return [
	"Slap","Poke"
	];
}

function Slap(connection, targetUser, targetChannel){
	connection.sendAction(targetChannel, "slaps "+targetUser.getNick()+"!");
}

function Poke(connection, targetUser, targetChannel){
	connection.sendAction(targetChannel, "pokes "+targetUser.getNick()+" with a sharp stick!");
}
