package utilities;

import java.io.Serializable;
import java.util.Date;

import exceptions.TerminException;

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
	
	public Termin(int terminId, String terminInhaber, String terminName, Date dateTime) throws TerminException {
		super();		
		this.setTerminId(terminId);
		this.setTerminInhaber(terminInhaber);
		this.setTerminName(terminName);
		this.setDateTime(dateTime);
	}

	public Termin(String terminInhaber, String terminName, Date dateTime) throws TerminException {
		super();
		this.setTerminInhaber(terminInhaber);
		this.setTerminName(terminName);
		this.setDateTime(dateTime);		
	}

	public int getTerminId() {
		return terminId;
	}

	public void setTerminId(int terminId) throws TerminException {
		if(terminId < 0 || terminId > 9999)
			throw new TerminException("terminId kleiner gleich 0 oder grosser als 9999\n");
		this.terminId = terminId;
	}

	public String getTerminInhaber() {
		return terminInhaber;
	}

	public void setTerminInhaber(String terminInhaber) throws TerminException {
		if(terminInhaber.length() > 20 || terminInhaber.isEmpty())
			throw new TerminException("\nterminInhaber ist leer oder sher gross\n");
		this.terminInhaber = terminInhaber;
	}

	public String getTerminName() {
		return terminName;
	}

	public void setTerminName(String terminName) throws TerminException {
		if(terminName.length() > 20 || terminName.isEmpty())
			throw new TerminException("\nterminName ist leer oder sher gross\n");
		this.terminName = terminName;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) throws TerminException {
		java.util.Date now = new Date();
		if(dateTime.before(now))
			throw new TerminException("Termin in der vergangenheit ist nicht moeglich\n");
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "Termin [terminId=" + terminId + ", terminInhaber=" + terminInhaber + ", terminName=" + terminName
				+ ", dateTime=" + dateTime + "]\n";
	}
}
