package utilities;

import java.io.Serializable;
import java.util.Date;

public class Termin implements Serializable {
	
	private int terminId;
	private String terminInhaber;
	private String terminName;
	private Date dateTime;
	
	
	public Termin() {
		super();
		this.terminId = 0;
		this.terminInhaber = "";
		this.terminName = "";
		this.dateTime = null;		
	}
	
	public Termin(int terminId, String terminInhaber, String terminName, Date dateTime) {
		super();
		this.terminId = terminId;
		this.terminInhaber = terminInhaber;
		this.terminName = terminName;
		this.dateTime = dateTime;
	}

	public Termin(String terminInhaber, String terminName, Date dateTime) {
		super();
		this.terminInhaber = terminInhaber;
		this.terminName = terminName;
		this.dateTime = dateTime;		
	}

	public int getTerminId() {
		return terminId;
	}

	public void setTerminId(int terminId) {
		this.terminId = terminId;
	}

	public String getTerminInhaber() {
		return terminInhaber;
	}

	public void setTerminInhaber(String terminInhaber) {
		this.terminInhaber = terminInhaber;
	}

	public String getTerminName() {
		return terminName;
	}

	public void setTerminName(String terminName) {
		this.terminName = terminName;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "Termin [terminId=" + terminId + ", terminInhaber=" + terminInhaber + ", terminName=" + terminName
				+ ", dateTime=" + dateTime + "]\n";
	}
}
