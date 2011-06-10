
function onMessage(event) {
	//gui.window("title","message");
	
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
