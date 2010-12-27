package shared;

public class Message {
	
	private String content;
	
	//other things like author, timestamp, etc
	public Message(String content){
		this.setContent(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}
