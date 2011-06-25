//META<inuse=false>
//META<author=KR>

importClass(Packages.shared.RoomManager);
importClass(Packages.ui.composites.MainWindow);
importClass(org.eclipse.swt.widgets.Display);
importPackage(org.pircbotx);
importClass(java.lang.Thread);
importPackage(java.util);
importPackage(java.lang);

function generateMorseMap() {
	var map = new HashMap();
	map.put(" ","/");
	map.put("a",".-");
	map.put("b","-...");
	map.put("c","-.-.");
	map.put("d","-..");
	map.put("e",".");
	map.put("f","..-.");
	map.put("g","--.");
	map.put("h","....");
	map.put("i","..");
	map.put("j",".---");
	map.put("k","-.-");
	map.put("l",".-..");
	map.put("m","--");
	map.put("n","-.");
	map.put("o","---");
	map.put("p",".--.");
	map.put("q","--.-");
	map.put("r",".-.");
	map.put("s","...");
	map.put("t","-");
	map.put("u","..-");
	map.put("v","...-");
	map.put("w",".--");
	map.put("x","-..-");
	map.put("y","-.--");
	map.put("z","--..");
	return map;
}

function text2morse(c,string) {
	string = string.toLowerCase();
	var alphabet = generateMorseMap();
	RoomManager.getMain().getDisplay().asyncExec(function() {
		for(var i = 0; i < string.length; i++) {
			var curDelays = alphabet.get(""+string.charAt(i));
			if(curDelays == null) continue;
			for(var j = 0; j < curDelays.length(); j++) {
				var curChar = (curDelays.charAt(j));
				if(curChar == 46) { util.beep(); Thread.sleep(250); }
				if(curChar == 45) { util.beep(); Thread.sleep(500); }
				if(curChar == 47) { Thread.sleep(500); }
			}
		}
	});
}

function onMessage(event) {
	(new Thread(function() { text2morse(null,event.getUser().getNick() + " says " + event.getMessage()); })).start();
}