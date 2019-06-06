package utilities;

import java.io.Serializable;

import exceptions.NewsException;
import exceptions.TerminException;

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

	public News(String senderUserName, String recipientUserName, int terminId, int newsId) throws NewsException {
		super();
		this.setSenderUserName (senderUserName);
		this.setRecipientUserName(recipientUserName);
		this.setTerminId(terminId);
		this.setNewsId(newsId);
	}

	public News(String senderUserName, String recipientUserName, int terminId) throws NewsException {
		super();
		this.setSenderUserName (senderUserName);
		this.setRecipientUserName(recipientUserName);
		this.setTerminId(terminId);
	}
	
	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) throws NewsException {
		if(senderUserName.length() > 20 || senderUserName.isEmpty())
			throw new NewsException("\nException: SenderUserName ist leer oder sher gross\n");
		this.senderUserName = senderUserName;
	}

	public String getRecipientUserName() {
		return recipientUserName;
	}

	public void setRecipientUserName(String recipientUserName) throws NewsException {
		if(recipientUserName.length() > 20 || recipientUserName.isEmpty())
			throw new NewsException("\nException: RecipientUserName ist leer oder sher gross\n");
		this.recipientUserName = recipientUserName;
	}

	public int getTerminId() {
		return terminId;
	}

	public void setTerminId(int terminId) throws NewsException {
		if(terminId < 0 || terminId > 9999)
			throw new NewsException("terminId kleiner gleich 0 oder grosser als 9999");
		this.terminId = terminId;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) throws NewsException {
		if(newsId < 0 || newsId > 9999)
			throw new NewsException("NewsId kleiner gleich 0 oder grosser als 9999");
		this.newsId = newsId;
	}

	@Override
	public String toString() {
		return "\nnewsId= " + getNewsId() + ", senderUserName=" + getSenderUserName() + ", recipientUserName=" + getRecipientUserName() + ", terminId="
				+ getTerminId() + "\n";
	}
	
	
}
