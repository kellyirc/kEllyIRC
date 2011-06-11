
function onMessage(event) {

	var nicks = new Array("pie","Elly","Mentos");
	var splitmessage = event.getMessage().split(" ");
	
	for(var s in splitmessage){
		for(var ss in nicks){
			if(nicks[ss] == splitmessage[s]){
				gui.window("Nick Alert",event.getUser().getNick()+ " is requesting your presence in "+event.getChannel().getName(), NotificationType.INFO);
			}
		}
	}
}

function join(connection, args) {
	
	if(util.checkArgs(args, 1)) {
		var args = util.getArgs(args,1);
		connection.joinChannel(cleanChanStr(args[0]));
	} else if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.joinChannel(cleanChanStr(args[0]), args[1]);
	}

}

function cleanChanStr(string) {
	if(!string.startsWith("#")) {
		return "#"+string;
	}
	return string;
}
