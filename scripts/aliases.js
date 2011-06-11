

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