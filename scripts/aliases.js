//META<inuse=true>
//META<author=EllyMent>

//join a channel (password optional)
function join(connection, sargs) {
	if(util.checkArgs(sargs, 1)) {
		var args = util.getArgs(sargs,1);
		connection.joinChannel(_cleanChanStr(args[0]));
	} else if(util.checkArgs(sargs,2)) {
		var args = util.getArgs(sargs,2);
		connection.joinChannel(_cleanChanStr(args[0]), args[1]);
	}
}

//used by join()
function _cleanChanStr(string) {
	if(!string.startsWith("#")) {
		return "#"+string;
	}
	return string;
}

function _cleanMakeChan(string) {
	if(!string.startsWith("#")) {
		string = "#"+string;
	}
	return global.currentConnection().getChannel(string);
}

function _errNoUser() {
	util.error("Could not find the requested user.");
}

//make an action
function action(connection, sargs) {
	connection.sendAction(global.currentChannel(), sargs);
}

//alias of an alias, woo!
function me(connection, sargs) { action(connection, sargs); }

//send an emote to a specific channel
function emote(connection, sargs) { 
	if(util.checkArgs(sargs,2)) {
		var args = util.getArgs(sargs,2);
		connection.sendAction(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /emote [channel|user] [message]");
	}
}

//send a message to all channels on the given connection
function amsg(connection, sargs) {
	var channels = connection.getChannels().toArray();
	for(var c in channels) {
		connection.sendMessage(channels[c], sargs);
	}
}

//send an action to all channels on the given connection
function ame(connection, sargs) {
	var channels = connection.getChannels().toArray();
	for(var c in channels) {
		connection.sendAction(channels[c], sargs);
	}
}

//send a message to something or someone or somewhere, somehow
function msg(connection, sargs) {
	if(util.checkArgs(sargs,2)) {
		var args = util.getArgs(sargs,2);
		connection.sendMessage(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /msg [channel|user] [message]");
	}
}

function query(connection, sargs) { msg(connection, sargs); }

//send a notice to something or someone or somewhere, somehow
function notice(connection, sargs) {
	if(util.checkArgs(sargs,2)) {
		var args = util.getArgs(sargs,2);
		connection.sendNotice(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /notice [channel|user] [message]");
	}
}

//quit with an optional message
function quit(connection, sargs) {	
	if(util.checkArgs(sargs, 1)) {
		connection.quitServer(sargs);
	} else {
		connection.disconnect();
	}
}

//send a message only you can see
function echo(connection, sargs) {
	util.error("Echo Service",args);
}

//invite a user to a channel
function invite(connection, sargs) {
	if(util.checkArgs(sargs,2)) {
		var args = util.getArgs(sargs,2);
		connection.sendInvite(args[0], args[1]);
	} else {
		util.error("Invalid format; please use /invite [user] [channel]");
	}
}

//send a raw line of text
function raw(connection, sargs) {
	connection.sendRawLineViaQueue(sargs);
}

//change your nickname
function nick(connection, sargs) {
	connection.changeNick(sargs);
}

//leave the channel
function part(connection, sargs) {
	if(util.checkArgs(sargs,2)) {
		var args = util.getArgs(sargs,2);
		connection.partChannel(args[0], args[1]);
	} else if(util.checkArgs(sargs,1)) {
		connection.partChannel(sargs);
	} else {
		connection.partChannel(global.currentChannel().getName());
	}
}

//leave all channels
function partall(connection) {
	var channels = connection.getChannels().toArray();
	for(var c in channels) {
		connection.partChannel(channels[c].getName());
	}
}

//nickserv alias
function ns(connection, sargs) {
	connection.sendMessage("NickServ", sargs);
}

//kick a user from the channel
function kick(connection, sargs) {
	try { 
		if(util.checkArgs(sargs, 3)) {
			var args = util.getArgs(sargs, 3);
			connection.kick(_cleanMakeChan(args[0]), util.findUser(args[1]), args[2]);
		} else if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.kick(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else {
			connection.kick(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
	
}

function op(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.op(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.op(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function deop(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.deOp(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.deOp(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function voice(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.voice(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.voice(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function deVoice(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.deVoice(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.deVoice(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function hop(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.halfOp(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.halfOp(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function dehop(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.deHalfOp(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.deHalfOp(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function sop(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.superOp(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.superOp(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function desop(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.deSuperOp(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.deSuperOp(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function owner(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.owner(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.owner(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function deowner(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.deOwner(_cleanMakeChan(args[0]), util.findUser(args[1]));
		} else if(util.checkArgs(sargs, 1)) {
			connection.deOwner(global.currentChannel(), util.findUser(sargs));
		}
	} catch(e) {
		_errNoUser();
	}
}

function ban(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.ban(_cleanMakeChan(args[0]), args[1]);
		} else if(util.checkArgs(sargs, 1)) {
			connection.ban(global.currentChannel(), args);
		}
	} catch(e) {
		_errNoUser();
	}
}

function unban(connection, sargs) {
	try {
		if(util.checkArgs(sargs, 2)) {
			var args = util.getArgs(sargs, 2);
			connection.unBan(_cleanMakeChan(args[0]), args[1]);
		} else if(util.checkArgs(sargs, 1)) {
			connection.unBan(global.currentChannel(), args);
		}
	} catch(e) {
		_errNoUser();
	}
}

//set the topic in a given channel
function topic(connection, sargs) {
	if(util.checkArgs(sargs, 2)) {
		var args = util.getArgs(sargs, 2);
		connection.setTopic(_cleanMakeChan(args[0]), args[1]);
	} else {
		util.error(global.currentChannel().getTopic());
	}
}

//set a mode on a user
function mode(connection, sargs) {
	if(util.checkArgs(sargs, 3)) {
		var args = util.getArgs(sargs, 3);
		if(util.findUser(args[2])!=null) {
			connection.setMode(_cleanMakeChan(args[0]), args[1], util.findUser(args[2]));
		} else {
			connection.setMode(_cleanMakeChan(args[0]), args[1], args[2]);
		}
		
	} else if(util.checkArgs(sargs, 2)) {
		var args = util.getArgs(sargs, 2);
		connection.setMode(_cleanMakeChan(args[0]), args[1]);
		
	} else if(util.checkArgs(sargs, 1)) {
		connection.setMode(global.currentChannel(), sargs);
	}
}


function sendFile(filename, nick, timeout)
{
	importClass(java.io.File);
	var f = new File(filename);
	var u = global.currentConnection().getUser(nick);
	if(f.exists() && f.isFile())
		global.currentConnection().dccSendFile(f, u, Integer.parseInt(timeout));
	else
		util.error("Your file either does not exist, or is not a file");
}
//send a file via DCC. Usage: /dccSend [filename] [user] [timeout]
//timeout is optional
function dccSend(connection, sargs)
{
//TODO: make the invokes work
	if(util.checkArgs(sargs, 3)) {
		var args = util.getArgs(sargs, 3);
		sendFile(args[0], args[1], args[3]);
	} else if(util.checkArgs(sargs, 2)) {
		var args = util.getArgs(sargs, 2);
		sendFile(args[0], args[1], 30);
	}
}

