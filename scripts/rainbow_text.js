//META<inuse=false>
//META<author=FireFreek (Rahat Ahmed)>
//META<description=Creates a rainbow flavor of text>

function rainbow(connection, sargs)
{
	if(util.checkArgs(sargs, 1))
	{
		var args = util.getArgs(sargs,1);
		var arr = args[0].split(" ");
		var output = "";
		for(var i=0;i<arr.length;i++)
		{
			switch(i%7)
			{
				case 0: output+=Colors.RED; break;
				case 1: output+=Colors.YELLOW; break;
				case 2: output+=Colors.GREEN; break;
				case 3: output+=Colors.CYAN; break;
				case 4: output+=Colors.BLUE; break;
				case 5: output+=Colors.PURPLE; break;
				case 6: output+=Colors.MAGENTA; break;
			}
			output+= arr[i] + " ";
		}
		connection.sendMessage(global.currentChannel(),output);
	}
}

function superRainbow(connection, sargs)
{
	if(util.checkArgs(sargs, 1))
	{
		var args = util.getArgs(sargs,1);
		var arr = args[0].toCharArray();
		var output = Colors.BOLD + "";
		for(var i=0;i<arr.length;i++)
		{
			switch(i%7)
			{
				case 0: output+=Colors.RED; break;
				case 1: output+=Colors.YELLOW; break;
				case 2: output+=Colors.GREEN; break;
				case 3: output+=Colors.CYAN; break;
				case 4: output+=Colors.BLUE; break;
				case 5: output+=Colors.PURPLE; break;
				case 6: output+=Colors.MAGENTA; break;
			}
			output+= String.fromCharCode(arr[i]);
		}
		connection.sendMessage(global.currentChannel(),output);
	}
}