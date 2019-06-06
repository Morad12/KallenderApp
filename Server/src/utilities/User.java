package utilities;

import java.io.Serializable;

import exceptions.TerminException;
import exceptions.UserException;

public class User implements Serializable {

	private String userName;
	private String email;
	private String nachname;
	private String vorname;
	private String passwort;
	
	public User() {
		super();
		this.userName = "";
		this.email = "";
		this.nachname = "";
		this.vorname = "";
		this.passwort = "";
	}
	
	public User(String userName, String email, String nachname, String vorname, String passwort) throws UserException {
		super();
		this.setUserName(userName);
		this.setEmail(email);
		this.setNachname(nachname);
		this.setVorname(vorname);
		this.setPasswort(passwort);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) throws UserException {
		if(userName.length() > 20 || userName.isEmpty())
			throw new UserException("\nUserName ist leer oder sher gross\n");
		this.userName = userName;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) throws UserException {
		if(nachname.length() > 20 || nachname.isEmpty())
			throw new UserException("\nNachname ist leer oder sher gross\n");
		this.nachname = nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) throws UserException {
		if(vorname.length() > 20 || vorname.isEmpty())
			throw new UserException("\nVorname ist leer oder sher gross\n");
		this.vorname = vorname;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) throws UserException {
		if(passwort.length() > 20 || passwort.isEmpty())
			throw new UserException("\nPasswort ist leer oder sher gross\n");
		this.passwort = passwort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws UserException {
		if(email.length() > 20 || email.isEmpty())
			throw new UserException("\nEmail ist leer oder sher gross\n");
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", email=" + email + ", nachname=" + nachname + ", vorname=" + vorname
				+ ", passwort=" + passwort + "]\n";
	}

}
