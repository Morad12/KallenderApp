package utilities;

import java.io.Serializable;

public class News implements Serializable {

	private String senderUserName;
	private String recipientUserName;
	private int terminId;
	private int newsId;
	
	public News() {
		super();
		this.senderUserName = "";
		this.recipientUserName = "";
		this.terminId = 0;
		this.newsId = 0;
	}

	public News(String senderUserName, String recipientUserName, int terminId, int newsId) {
		super();
		this.senderUserName = senderUserName;
		this.recipientUserName = recipientUserName;
		this.terminId = terminId;
		this.newsId = newsId;
	}

	public News(String senderUserName, String recipientUserName, int terminId) {
		super();
		this.senderUserName = senderUserName;
		this.recipientUserName = recipientUserName;
		this.terminId = terminId;
	}
	
	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	public String getRecipientUserName() {
		return recipientUserName;
	}

	public void setRecipientUserName(String recipientUserName) {
		this.recipientUserName = recipientUserName;
	}

	public int getTerminId() {
		return terminId;
	}

	public void setTerminId(int terminId) {
		this.terminId = terminId;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	@Override
	public String toString() {
		return "newsId= " + getNewsId() + ", senderUserName=" + getSenderUserName() + ", recipientUserName=" + getRecipientUserName() + ", terminId="
				+ getTerminId() + "\n";
	}
	
	
}
