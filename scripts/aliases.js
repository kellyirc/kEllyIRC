
//join a channel (password optional)
function join(connection, args) {
	
	if(util.checkArgs(args, 1)) {
		var args = util.getArgs(args,1);
		connection.joinChannel(cleanChanStr(args[0]));
	} else if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.joinChannel(cleanChanStr(args[0]), args[1]);
	}

}

//used by join()
function cleanChanStr(string) {
	if(!string.startsWith("#")) {
		return "#"+string;
	}
	return string;
}

//make an action
function action(connection, args) {
	connection.sendAction(global.currentChannel(), args);
}

//alias of an alias, woo!
function me(connection, args) { action(connection, args); }

//send a message to all channels on the given connection
function amsg(connection, args) {
	var channels = connection.getChannels().toArray();
	for(var c in channels) {
		connection.sendMessage(channels[c], args);
	}
}

//send a message to something or someone or somewhere, somehow
function msg(connection, args) {

	if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.sendMessage(args[0], args[1]);
	}
}