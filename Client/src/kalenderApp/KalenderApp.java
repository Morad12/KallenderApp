    
package kalenderApp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utilities.News;
import utilities.Termin;
import utilities.User;

public interface KalenderApp extends Remote {
	
	public boolean creatKonto(User user) throws RemoteException, Exception;
	public User login(String username, String passwort) throws RemoteException, Exception;
	public boolean logout(User user) throws RemoteException, Exception;
	public User updateKonto(User user, String where) throws RemoteException, Exception;
	public boolean deleteKonto(User user) throws RemoteException, Exception;
	
	public int addTermin(Termin termin) throws RemoteException, Exception;
	public boolean deleteTermin(int terminId) throws RemoteException, Exception;
	public Termin updateTermin(int terminId, String where) throws RemoteException, Exception;
	public List<Termin> getMyTermine(String username) throws RemoteException, Exception;
	public List<Termin> searchSpan(Date date_von, Date date_bis, String terminInhaber) throws RemoteException, Exception;
	
	public boolean userEinladen(News news) throws RemoteException, Exception;
	public List<News> getNewsRecipientList(String recipientUsername) throws RemoteException, Exception;
	public List<News> getNewsSenderList(String senderUsername) throws RemoteException, Exception;
	public int acceptNews(News news) throws RemoteException, Exception;
	public boolean deleteNews(News news) throws RemoteException, Exception;
	
	public boolean isEmailValide(String email)throws RemoteException;
		
}