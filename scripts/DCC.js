function sendFile(filename, nick, timeout)
{
	importClass(java.io.File);
	var f = new File(filename);
	var u = global.currentConnection().getUser(nick);
	if(f.exists() && f.isFile())
		global.currentConnection().dccSendFile(f, u, timeout);
	else
		util.error("Your file either does not exist, or is not a file");
}

function sendFile(filename, nick)
{
	importClass(java.io.File);
	var f = new File(filename);
	var u = global.currentConnection().getUser(nick);
	if(f.exists() && f.isFile())
		global.currentConnection().dccSendFile(f, u, 30);
	else
		util.error("Your file either does not exist, or is not a file");
}