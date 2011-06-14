
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

//send an action to all channels on the given connection
function ame(connection, args) {
	var channels = connection.getChannels().toArray();
	for(var c in channels) {
		connection.sendAction(channels[c], args);
	}
}

//send a message to something or someone or somewhere, somehow
function msg(connection, args) {
	if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.sendMessage(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /msg [channel|user] [message]");
	}
}

function query(connection, args) { msg(connection, args); }

//send a notice to something or someone or somewhere, somehow
function notice(connection, args) {
	if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.sendNotice(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /notice [channel|user] [message]");
	}
}

//quit with an optional message
function quit(connection, args) {	
	if(util.checkArgs(args, 1)) {
		var args = util.getArgs(args,1);
		connection.quitServer(cleanChanStr(args[0]));
	} else {
		connection.disconnect();
	}
}

//send a message only you can see
function echo(connection, args) {
	util.error("Echo Service",args);
}

//invite a user to a channel
function invite(connection, args) {
	if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.sendInvite(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /invite [user] [channel]");
	}
}

//send a raw line of text
function raw(connection, args) {
	connection.sendRawLineViaQueue(args);
}

//change your nickname
function nick(connection, args) {
	connection.changeNick(args);
}

//leave the channel
function part(connection, args) {
	if(util.checkArgs(args,2)) {
		var args = util.getArgs(args,2);
		connection.partChannel(args[0], args[1]);
	} else if(util.checkArgs(args,1)) {
		channel.partChannel(args);
	} else {
		channel.partChannel(global.currentChannel().getName());
	}
}

//leave all channels
function partall(connection, args) {
	var channels = connection.getChannels().toArray();
	for(var c in channels) {
		connection.partChannel(channels[c].getName());
	}
}
//TODO: kick, op, halfop, voice, ban, mode, topic, whois, whowas