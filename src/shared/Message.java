package shared;

public class Message {
	
	private String content, sender;
	
	//other things like author, timestamp, etc
	public Message(String content, String sender){
		this.setContent(content+"\n");
		this.setSender(sender);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}
}
