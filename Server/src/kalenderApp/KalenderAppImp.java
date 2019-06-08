package kalenderApp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dataBaseConnection.MySqlConnetion;
import exceptions.NewsException;
import exceptions.TerminException;
import exceptions.UserException;
import utilities.News;
import utilities.Termin;
import utilities.User;

public class KalenderAppImp extends UnicastRemoteObject implements KalenderApp {
	
	public KalenderAppImp() throws RemoteException {
		super();
	}

	@Override
	public boolean creatKonto(User user) throws RemoteException, UserException, Exception {
		
		User istUserda = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(istUserda == null) {
			istUserda = MySqlConnetion.searchUser(user.getEmail(), "email");
			if(istUserda == null) {
				MySqlConnetion.insertUser(user);
				return true;
			}
		}		
		throw new UserException("User ist schon vorhanden\n");
	}

	@Override
	public User login(String usernameORemail, String passwort) throws RemoteException, UserException, Exception{
				
		User user = MySqlConnetion.searchUser(usernameORemail, "username");
		if(user == null) {
			user = MySqlConnetion.searchUser(usernameORemail, "email");
			if(user == null) {
				throw new UserException("UserName oder Email nicht gefunden !\n");
			}
		}
		if(user.getPasswort().equals(passwort)) {
//			System.out.println("sie sind eingeloggen");
			return user;
		}
		else 
			throw new UserException("PassWort falsch !\n");
	}

	@Override
	public boolean logout(User user) throws RemoteException, Exception{
		
		User null_test = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(null_test == null) {
			null_test = MySqlConnetion.searchUser(user.getEmail(), "email");
			if(null_test == null) {				
				return false;
			}
		}	
		return true;
	}

	@Override
	public User updateKonto(User user, String where) throws RemoteException, Exception {		
		
		User userReturn = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(userReturn == null) {
			userReturn = MySqlConnetion.searchUser(user.getEmail(), "email");
			if(userReturn == null) {				
				throw new UserException("User nicht gefunden !\n");
			}
		}
		MySqlConnetion.updateUser(user, where);
		userReturn = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(userReturn == null)
			userReturn = MySqlConnetion.searchUser(user.getEmail(), "email");
		return userReturn;
	}

	@Override
	public boolean deleteKonto(User user) throws RemoteException, Exception {
		
		User null_test = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(null_test == null) {
			null_test = MySqlConnetion.searchUser(user.getEmail(), "email");
			if(null_test == null) {	
				throw new UserException("User nicht gefunden !\n");
			}
		}
		MySqlConnetion.deleteUser(user.getUserName());
		return true;
	}

	@Override
	public int addTermin(Termin termin) throws RemoteException, Exception, TerminException, UserException{		

		User user = MySqlConnetion.searchUser(termin.getTerminInhaber(), "username");
		if(user == null) {
			throw new UserException("User nicht gefunden !\n");
		}
		Termin termin1 = MySqlConnetion.searchTerminTime(termin.getTerminInhaber(), termin.getDateTime());
		if(termin1 == null) {			
			MySqlConnetion.insertTermin(termin);
			termin1 = MySqlConnetion.searchTerminTime(termin.getTerminInhaber(), termin.getDateTime());
			return termin1.getTerminId();
		}
		throw new TerminException("Termin schon vorhanden !\n");
	}

	@Override
	public boolean deleteTermin(int terminId) throws RemoteException, TerminException, Exception {
		
		Termin null_test = MySqlConnetion.searchTermin(terminId);
		if(null_test != null) {			
			MySqlConnetion.deleteTermin(terminId);
			return true;
		}		
		throw new TerminException("Termin nicht gefunden !\n");
	}

	@Override
	public Termin updateTermin(int terminId, String where) throws RemoteException, TerminException, Exception {
		
		Termin termin = MySqlConnetion.searchTermin(terminId);
		if(termin == null)
			throw new TerminException("Termin nicht gefunden !\n");
		MySqlConnetion.updateTermin(termin, where);	
		termin = MySqlConnetion.searchTermin(terminId);
		return termin;
	}
	
	@Override
	public List<Termin> getMyTermine(String username) throws RemoteException, UserException, Exception{
		User user = MySqlConnetion.searchUser(username, "username");
		if(user == null) {
			throw new UserException("User nicht gefunden !\n");
		}
		List<Termin> termine = MySqlConnetion.getTermineInhaber(username);
		return termine;
	}

	@Override
	public List<Termin> searchSpan(Date date_von, Date date_bis, String terminInhaber) throws RemoteException, Exception {

		List<Termin> termine = MySqlConnetion.getTermineInhaber(terminInhaber);
		List<Termin> termineSpan = new ArrayList<Termin>();
		
		for(Termin termin : termine) {
			if(termin.getDateTime().after(date_von) && termin.getDateTime().before(date_bis))
				termineSpan.add(termin);
		}	
		return termineSpan;
	}

	@Override
	public boolean userEinladen(News news) throws UserException, TerminException, Exception {
		
		User null_test = MySqlConnetion.searchUser(news.getRecipientUserName(), "username");
		if(null_test == null) {
			throw new UserException("User nicht gefunden !\n");
		}
		
		Termin termin = MySqlConnetion.searchTermin(news.getTerminId());
		if(termin == null) {
			throw new TerminException("Termin nicht gefunden !\n");
		}
		
		News news1 = MySqlConnetion.searchNewsReciepientAndTerminId(news.getRecipientUserName(), news.getTerminId());
		if(news1 != null) {
			throw new NewsException("Termin schon besetzt!");
		}
				
		MySqlConnetion.insertNews(news);
		return true;
}

	@Override
	public List<News> getNewsRecipientList(String recipientUsername) throws Exception {
		return MySqlConnetion.getNewsRecipientList(recipientUsername);		
	}
	
	@Override
	public List<News> getNewsSenderList(String senderUsername) throws Exception {
		return MySqlConnetion.getNewsSenderList(senderUsername);		
	}

	@Override
	public int acceptNews(News news) throws RemoteException, Exception {
		
		Termin termin = MySqlConnetion.searchTermin(news.getTerminId());
		Termin neuTermin = new Termin(news.getRecipientUserName(), termin.getTerminName(), termin.getDateTime());
		int terminId = addTermin(neuTermin);
		deleteNews(news);
		return terminId;		
	}

	@Override
	public boolean deleteNews(News news) throws RemoteException, Exception {
		
		MySqlConnetion.deleteNews(news.getNewsId());
		if(MySqlConnetion.searchNews(news.getNewsId()) == null)
			return true;
		return false;
	} 

	@Override
	public boolean isEmailValide(String email) {
		
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
}

