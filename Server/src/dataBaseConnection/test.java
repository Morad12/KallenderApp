package dataBaseConnection;


import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utilities.News;
import utilities.Termin;
import utilities.User;

public class test {

	public static void main(String[] args) {
		try {
			
//			MySqlConnetion.insertUser(user);			
			/*User user1;
			user1 = MySqlConnetion.searchUser("lufy","email");			
			if (user1 == null) {
				user1=MySqlConnetion.searchUser("luf","username");
				System.out.println(user1);	
				if(user1 == null)
					System.out.println("leer");
			}*/
			
//			System.out.println(creatKonto(user));		
//			user.setNachname("najmi");
//			MySqlConnetion.updateUser(user, "nachname");			
//			MySqlConnetion.deleteUser("jhghk");
//			System.out.println(addTermin(termin));
//			getMyTermine("eren");
//			System.out.println(MySqlConnetion.getTerminList());
//			System.out.println(MySqlConnetion.getTermineInhaber("eren"));
			
			
//			System.out.println(MySqlConnetion.searchTerminTime("eren500", "20:15:14"));
			
//			System.out.println(getMyTermine("eren501"));
			
//			System.out.println(MySqlConnetion.getUserList());
			
			/*java.util.Date uDate;
			uDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2021-02-11 15:30:10");
			
			Termin termin = new Termin("samira","Veranstaltung", uDate);
			
			MySqlConnetion.insertTermin(termin);
			
			int i = addTermin(termin);
			int i = MySqlConnetion.insertTermin(termin);
			System.out.println(i);*/
			
			
			
//			System.out.println(MySqlConnetion.getTerminList());
			
			/*java.util.Date date1;
			date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2008-12-30 00:00:00");
			
			java.util.Date date2; 
			date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2011-02-11 16:30:10");
			
			System.out.println(searchSpan(date1, date2, "superman"));*/
			
//			News news = new News("eren","lufy", 1);
//			System.out.println(userEinladen(news));
			
//			System.out.println(userEinladen(news));
			
//			List<News> tab = getNewsSenderList("eren");
			
//			System.out.println(tab);
			
//			deleteNews(tab.get(0));
			
//			System.out.println(getNewsRecipientList("sanji"));
			
//			int a = acceptNews(getNewsRecipientList("sanji").get(0));
//			System.out.println(a);
			
//			System.out.println(emailValide("morad@gmailcom.ss"));
			
			
//			User user = new User("samira","neuemail@gmail,com","ne","morad", "12+3456");
			
//			updateKonto(user, "passwort");
			
			java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2018-07-06 17:05:00");
			
			Termin termin = new Termin("sanji","geburtstag",date1);
			
			addTermin(termin);
			
			
			
			
			/*LocalDateTime.now();
			
			
			DateTimeFormatter c = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
//			System.out.println(java.sql.Timestamp.valueOf(LocalDateTime.now().format(myFormatObj)));
			System.out.println(LocalDateTime.now().format(myFormatObj));
			
			
			System.out.println(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));*/
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	public static int addTermin(Termin termin) throws RemoteException, Exception {				
		User user = MySqlConnetion.searchUser(termin.getTerminInhaber(), "username");
		if(user == null) {
			System.out.println("Exp2");
			return -1;
		}
		Termin termin1 = MySqlConnetion.searchTerminTime(termin.getTerminInhaber(), termin.getDateTime());
		if(termin1 == null) {			
			MySqlConnetion.insertTermin(termin);
			termin1 = MySqlConnetion.searchTerminTime(termin.getTerminInhaber(), termin.getDateTime());
			return termin1.getTerminId();
		}
		System.out.println("exp5");
		return -1;		
	}
	
	/*public static int addTermin(Termin termin) throws RemoteException, Exception {				
		User user = MySqlConnetion.searchUser(termin.getTerminInhaber(), "username");
		if(user == null) {
			System.out.println("Exp");
			return -1;
		}
		Termin termin1 = MySqlConnetion.searchTerminTime(termin.getTerminInhaber(), termin.getDateTime());
		if(termin1 == null) {			
			int terminId = MySqlConnetion.insertTermin(termin);
			return terminId;
		}
		return -1;		
	}*/

	public boolean deleteTermin(int terminId) throws RemoteException, Exception {
		
		Termin null_test = MySqlConnetion.searchTermin(terminId);
		if(null_test != null) {			
			MySqlConnetion.deleteTermin(terminId);
			return true;
		}		
		return false;
	}

	
	public Termin updateTermin(int terminId, String where) throws RemoteException, Exception {
		
		Termin termin = MySqlConnetion.searchTermin(terminId);
		if(termin == null)
			System.out.println("Exception");
		MySqlConnetion.updateTermin(termin, where);	
		termin = MySqlConnetion.searchTermin(terminId);
		return termin;
	}
	
	
	public static List<Termin> getMyTermine(String username) throws RemoteException, Exception{
		User user = MySqlConnetion.searchUser(username, "username");
		if(user == null) {
			System.out.println("Exp");
			return null;
		}
		List<Termin> termine = MySqlConnetion.getTermineInhaber(username);
		return termine;
	}
	
	public static List<Termin> searchSpan(Date date_von, Date date_bis, String userName) throws RemoteException, Exception {
		List<Termin> termine = MySqlConnetion.getTermineInhaber(userName);
		List<Termin> termineSpan = new ArrayList<Termin>();
		
		for(Termin termin : termine) {
			if(termin.getDateTime().after(date_von) && termin.getDateTime().before(date_bis))
				termineSpan.add(termin);
		}	
		return termineSpan;
	}
	
	
	public static boolean userEinladen(News news) throws Exception {
		
		User null_test = MySqlConnetion.searchUser(news.getRecipientUserName(), "username");
		if(null_test == null) {
				System.out.println("Exp1");
				return false;
		}
		
		Termin termin = MySqlConnetion.searchTermin(news.getTerminId());
		if(termin == null) {
			System.out.println("Exp2");
			return false;
		}
		
		News news1 = MySqlConnetion.searchNewsReciepientAndTerminId(news.getRecipientUserName(), news.getTerminId());
		if(news1 == null) {
			MySqlConnetion.insertNews(news);
			return true;
		}
				
		return false;		
}
	
	public static  List<News> getNewsRecipientList(String recipientUsername) throws Exception {
		return MySqlConnetion.getNewsRecipientList(recipientUsername);		
	}
	
	
	public static List<News> getNewsSenderList(String senderUsername) throws Exception {
		return MySqlConnetion.getNewsSenderList(senderUsername);		
	}

	
	public static int acceptNews(News news) throws RemoteException, Exception {		
				
		Termin termin = MySqlConnetion.searchTermin(news.getTerminId());
		Termin neuTermin = new Termin(news.getRecipientUserName(), termin.getTerminName(), termin.getDateTime());
		return addTermin(neuTermin);		
	}
	
	public static void deleteNews(News news) throws RemoteException, Exception {
		MySqlConnetion.deleteNews(news.getNewsId());
	} 
	
	public static User updateKonto(User user, String where) throws RemoteException, Exception {		
		
		User userReturn = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(userReturn == null) {
			userReturn = MySqlConnetion.searchUser(user.getEmail(), "email");
			if(userReturn == null) {				
				System.out.println("Exeption");
			}
		}
		MySqlConnetion.updateUser(user, where);
		userReturn = MySqlConnetion.searchUser(user.getUserName(), "username");
		if(userReturn == null)
			userReturn = MySqlConnetion.searchUser(user.getEmail(), "email");
		return userReturn;
	}
	
	public static boolean emailValide(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
}
