package clientRMI;



import java.util.List;
import java.util.Scanner;
import java.io.Console;
import kalenderApp.KalenderApp;
import utilities.*;

public class Menu {


	private static enum Login_Create{EXIT, LOGIN, CREATE};
	private static enum News_Choice{WEITER, ANNEHMEN, ABLEHNEN};
	private static enum News_Utilities{ZURUECK, TERMIN, ALLES};
	private static enum Konto_Utilities{ZURUECK, UPDATE, DELETE}
	private static enum Konto_Manage{ZURUECK, PASSWORT, EMAIL, VORNAME, NACHNAME, ALLES};
	private static enum Logged_Utilities{EXIT, KONTO, TERMIN};
	private static enum Ja_Nein{JA, NEIN};
	private static User user;
	private static Scanner s = new Scanner(System.in);
	private static KalenderApp stub = null;
	
	Menu() {
		
	}
	Menu(KalenderApp stub){
		//user = new User();
		Menu.stub = stub;
	}
	
	public static User getUser() {
		return user;
	}
	public static void setUser(User user) {
		Menu.user = user;
	}
	
	public static void HauptMenu() {
		user = new User();
		
		try {
			if(Login_Menu())
				Menu_Logged();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void Menu_News(String username) {
		
		String eingabe = null;
		News_Choice choice = null;
		News_Utilities accepted = null;
		News_Utilities refused = null;
		
		try {
			List<News> NewsRecipientList = stub.getNewsRecipientList(username);
			
			for(int i = 0; i < NewsRecipientList.size(); i++) {
				System.out.println((i+1)+": "+ NewsRecipientList.get(i).toString());
			}
			System.out.print("\nWollen Sie:\n"
					+ "1. Annehmen.\n"
					+ "2. Ablehnen.\n"
					+ "0. Weiter.\n"
					+ "Wählen Sie: ");
			eingabe = s.next();
			while(!eingabe.equals("0") && 
					!eingabe.equals("1") && 
					!eingabe.equals("2")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = s.next();
			}
			
			choice = News_Choice.values()[Integer.valueOf(eingabe)];
			
			switch(choice) {
			case ANNEHMEN:
				System.out.print("\nWollen Sie:\n"
						+ "1. News wählen.\n"
						+ "2. Alles annehmen.\n"
						+ "0. Zurück.\n"
						+ "Wählen Sie: ");
				eingabe = s.next();
				while(!eingabe.equals("0") && 
						!eingabe.equals("1") && 
						!eingabe.equals("2")){
					
					System.out.print("Falsche Eingabe! Wiederholen: ");
					eingabe = s.next();
				}
				
				accepted = News_Utilities.values()[Integer.valueOf(eingabe)];
				
				switch(accepted) {
				case TERMIN:
					System.out.print("Geben Sie Bitte die Zahl des News: ");
					eingabe = s.next();
					while(Integer.valueOf(eingabe) <= 0 || Integer.valueOf(eingabe) > NewsRecipientList.size()) {
						System.out.print("Falsche Eingabe! Wiederholen: ");
						eingabe = s.next();
					}
					
					if(stub.acceptNews(NewsRecipientList.get(Integer.valueOf(eingabe)-1)) != -1){
						System.out.println("Termin hinzugefügt");
						Menu_Logged();
					}
					else {
						System.out.println("Fehler ist eingetreten!");
						Menu_Logged();
					}
					break;
				case ALLES:
					for(News n : NewsRecipientList) {
						if(stub.acceptNews(n) != -1) {
							System.out.println("Termine hinzugefügt");
							Menu_Logged();
						}
						else
						{
							System.out.println("Fehler eingetreten");
							Menu_Logged();
						}
					}
					break;
				case ZURUECK:
					Menu_Logged();
					break;
				}
				break;
			case ABLEHNEN:
				System.out.print("\nWollen Sie:\n"
						+ "1. News wählen.\n"
						+ "2. Alles ablehnen.\n"
						+ "0. Zurück.\n"
						+ "Wählen Sie: ");
				eingabe = s.next();
				while(!eingabe.equals("0") && 
						!eingabe.equals("1") && 
						!eingabe.equals("2")){
					
					System.out.print("Falsche Eingabe! Wiederholen: ");
					eingabe = s.next();
				}
				
				refused = News_Utilities.values()[Integer.valueOf(eingabe)];
				
				switch(refused) {
				case TERMIN:
					System.out.print("Geben Sie Bitte die Zahl des News: ");
					eingabe = s.next();
					while(Integer.valueOf(eingabe) <= 0 || Integer.valueOf(eingabe) > NewsRecipientList.size()) {
						System.out.print("Falsche Eingabe! Wiederholen: ");
						eingabe = s.next();
					}
					
					if(stub.deleteNews(NewsRecipientList.get(Integer.valueOf(eingabe)-1))){
						System.out.println("Termin abgelehnt");
						Menu_Logged();
					}
					else {
						System.out.println("Fehler eingetreten");
						Menu_Logged();
					}
					break;
				case ALLES:
					for(News n : NewsRecipientList) {
						if(stub.deleteNews(n)) {
							System.out.println("Termin Abgelehnt");
							Menu_Logged();
						}
						else
						{
							System.out.println("Fehler ist eingetreten");
							Menu_Logged();
						}
					}
					break;
				case ZURUECK:
					Menu_Logged();
					break;
				}
				break;
			case WEITER:
				Menu_Logged();
				break;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static boolean Login_Menu(){
		
		Console console = System.console();
		String eingabe = null;
		String userName = "";
		String password = "";
		char[] pwd = null;
		String vorName = "";
		String nachName = "";
		String email = "";
		Login_Create input = null;
		String confirm = null;
		Ja_Nein antwort = null;
		List<News> NewsRecipientList = null;
		
		try {
			
			System.out.print("\nWollen Sie:\n"
					+ "1. Login.\n"
					+ "2. Konto erstellen.\n"
					+ "0. Abbrechen.\n"
					+ "Wählen Sie: ");
			eingabe = s.next();
			
			
			while(!eingabe.equals("0") && 
					!eingabe.equals("1") && 
					!eingabe.equals("2")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = s.next();
			}
			input = Login_Create.values()[Integer.valueOf(eingabe)];
			
			switch(input) {
			case LOGIN:
				do {
					System.out.print("Geben Sie ihren Benutzernamen: ");
					userName = s.next();
					System.out.print("Geben Sie ihr Passwort: ");
					if(console == null) {
						password = s.next();
						user = stub.login(userName, password);
					}
					else {
						pwd = console.readPassword();
						user = stub.login(userName, String.valueOf(pwd));
					}
					
					if(user != null) {
						NewsRecipientList = stub.getNewsRecipientList(user.getUserName());
						System.out.println("\nWillkomen "+user.getNachname()+", Sie sind eingeloggen.");
						System.out.print("Sie haben "+NewsRecipientList.size() + " Notification(s).\n"
								+ "\nWollen Sie die zeigen?(ja/nein): ");
						confirm = s.next();
						while(!confirm.equalsIgnoreCase("ja") &&
								!confirm.equalsIgnoreCase("nein")){
							
							System.out.print("Falsche Eingabe! Wiederholen: ");
							confirm = s.next();
						}
						
						antwort = Ja_Nein.valueOf(confirm.toUpperCase());
						
						if(antwort == Ja_Nein.JA) {
							/*for(News n : stub.getNewsRecipientList(user.getUserName())) {
								System.out.println(stub.getNewsRecipientList(user.getUserName())+""+n.toString());
							}*/
							Menu_News(user.getUserName());
						}
						else
							Menu_Logged();
						return true;
					}
					else {
						System.out.print("Das Login ist schiefgegangen! \nWollen Sie wieder versuchen(ja) oder abbrechen(nein)? : ");
						confirm = s.next();
						while(!confirm.equalsIgnoreCase("ja") &&
								!confirm.equalsIgnoreCase("nein")){
							
							System.out.print("Falsche Eingabe! Wiederholen: ");
							confirm = s.next();
						}
						antwort = Ja_Nein.valueOf(confirm.toUpperCase());
					}
				}while(user == null && antwort != Ja_Nein.NEIN);
				
				if(antwort == Ja_Nein.NEIN) {
					System.out.println("Auf wiedersehen!");
					System.exit(0);	
				}
				
				break;
			case CREATE:
				boolean created = false;
				do {
					System.out.print("Geben Sie ihren Benutzernamen: ");
					userName = s.next();
					System.out.print("Geben Sie ihr Passwort: ");
					if(console != null) {
						pwd = console.readPassword();
						password = String.valueOf(pwd);
					}
					else {
						password = s.next();
					}
					System.out.print("Geben Sie ihren Nachname: ");
					nachName = s.next();
					System.out.print("Geben Sie ihren Vorname: ");
					vorName = s.next();
					System.out.print("Geben Sie ihr Email: ");
					email = s.next();
					user = new User(userName, email, nachName, vorName, password);
					created = stub.creatKonto(user);
					if(created) {
						System.out.println("Das Konto is Created!");
						System.out.println("\nWillkomen "+user.getNachname()+", Sie sind eingeloggen.");
						NewsRecipientList = stub.getNewsRecipientList(user.getUserName());
						System.out.println("\nWillkomen "+user.getNachname()+", Sie sind eingeloggen.");
						System.out.print("Sie haben "+NewsRecipientList.size() + " Notification(s).\n"
								+ "\nWollen Sie die zeigen?(ja/nein): ");
						confirm = s.next();
						while(!confirm.equalsIgnoreCase("ja") &&
								!confirm.equalsIgnoreCase("nein")){
							
							System.out.print("Falsche Eingabe! Wiederholen: ");
							confirm = s.next();
						}
						
						antwort = Ja_Nein.valueOf(confirm.toUpperCase());
						
						if(antwort == Ja_Nein.JA) {
							/*for(News n : stub.getNewsRecipientList(user.getUserName())) {
								System.out.println(stub.getNewsRecipientList(user.getUserName())+""+n.toString());
							}*/
							Menu_News(user.getUserName());
						}
						else
							Menu_Logged();
						return true;
					}
					else {
						System.out.print("Die Erstellung des ist schiefgegangen! \nWollen Sie wieder versuchen(ja) oder abbrechen(nein)? : ");
						confirm = s.next();
						while(!confirm.equalsIgnoreCase("ja") &&
								!confirm.equalsIgnoreCase("nein")){
							
							System.out.print("Falsche Eingabe! Wiederholen: ");
							confirm = s.next();
						}
						antwort = Ja_Nein.valueOf(confirm.toUpperCase());
					}
				
				}while(!created && antwort != Ja_Nein.NEIN);
				
				if(antwort == Ja_Nein.NEIN) {
					System.out.println("Auf wiedersehen!");
					System.exit(0);
				}
				
				break;
			case EXIT:
				System.out.println("Auf wiedersehen!");
				System.exit(0);
				break;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}

	public static void Konto_Menu() {
		
		Console console = System.console();
		char[] pwd = null;
		String passwort = "";
		String nachName = "";
		String vorName = "";
		String email = "";
		String eingabe = null;
		String confirm = null;
		Konto_Utilities input = null;
		Konto_Manage manage = null;
		Ja_Nein antwort = null;
		try {
			System.out.print("\nWollen Sie Ihr Konto: \n"
					+ "1. Bearbeiten.\n"
					+ "2. Löschen.\n"
					+ "0. Zurück.\n"
					+ "Wählen Sie: ");
			eingabe = s.next();
			
			while(!eingabe.equals("0") && 
					!eingabe.equals("1") && 
					!eingabe.equals("2")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = s.next();
			}
			input = Konto_Utilities.values()[Integer.valueOf(eingabe)];
			
			switch(input) {
			case UPDATE:
				System.out.print("\nWas Wollen Sie bearbeiten:\n"
						+ "1. Passwort.\n"
						+ "2. Email.\n"
						+ "3. Nachname.\n"
						+ "4. Vorname.\n"
						+ "5. Alles.\n"
						+ "0. Zuruck.\n"
						+ "Wählen Sie: ");
				eingabe = s.next();
				
				while(!eingabe.equals("0") && 
						!eingabe.equals("1") && 
						!eingabe.equals("2") &&						
						!eingabe.equals("3") && 
						!eingabe.equals("4") && 
						!eingabe.equals("5")){
					
					System.out.print("Falsche Eingabe! Wiederholen: ");
					eingabe = s.next();
				}
					manage = Konto_Manage.values()[Integer.valueOf(eingabe)];
					
					switch(manage) {
					case PASSWORT:
						System.out.print("Geben Sie ihr neues Passwort: ");
						
						if(console == null) {
							passwort = s.next();
						}
						else {
							pwd = console.readPassword();
							passwort = String.valueOf(pwd);
						}
						break;
					case EMAIL:
						System.out.print("Geben Sie ihr neues Email: ");
						email = s.next();
						break;
					case NACHNAME:
						System.out.print("Geben Sie ihren neuen Nachname: ");
						nachName = s.next();
						break;
					case VORNAME:
						System.out.print("Geben Sie ihren neuen Vorname: ");
						vorName = s.next();
						break;
					case ALLES:
						System.out.print("Geben Sie ihr neues Passwort: ");
						if(console != null) {
							pwd = console.readPassword();
							passwort = String.valueOf(pwd);
						}
						else {
							passwort = s.next();
						}
						System.out.print("Geben Sie ihren neuen Nachname: ");
						nachName = s.next();
						System.out.print("Geben Sie ihren neuen Vorname: ");
						vorName = s.next();
						System.out.print("Geben Sie ihr neues Email: ");
						email = s.next();
						break;
					case ZURUECK:
						Menu_Logged();
						break;
					}
					
					System.out.print("Sind Sie sicher, dass Sie Ihr Konto bearbeiten wollen?(ja/nein): ");
					confirm = s.next();
					while(!confirm.equalsIgnoreCase("ja") &&
							!confirm.equalsIgnoreCase("nein")){
						
						System.out.print("Falsche Eingabe! Wiederholen: ");
						confirm = s.next();
					}
					antwort = Ja_Nein.valueOf(confirm.toUpperCase());
					
					if(antwort == Ja_Nein.JA) {
						
						if(!passwort.isEmpty() && !email.isEmpty() && !nachName.isEmpty() && !vorName.isEmpty()) {
							user.setPasswort(passwort);
							user.setEmail(email);
							user.setNachname(nachName);
							user.setVorname(vorName);
							
							if(stub.updateKonto(user,"email") != null && stub.updateKonto(user,"passwort") != null && stub.updateKonto(user,"vorname") != null && stub.updateKonto(user,"nachname") != null) {
								System.out.println("User is updated!");
								passwort = "";
								email = "";
								nachName = "";
								vorName = "";
								Menu_Logged();
							}
							else{
								System.out.println("Update is failed");
								Konto_Menu();
							}
						}
						
						if(!passwort.isEmpty()){
							user.setPasswort(passwort);
							if(stub.updateKonto(user,"passwort") != null) {
								System.out.println("User is updated!");
								passwort = "";
								Menu_Logged();
							}
							else {
								System.out.println("Update is failed");
								Konto_Menu();
							}
						}
						
						if(!email.isEmpty()) {
							
							user.setEmail(email);
							
							if(stub.updateKonto(user,"email") != null) {
								System.out.println("User is updated!");
								email = "";
								Menu_Logged();
							}
							else{
								System.out.println("Update is failed");
								Konto_Menu();
							}
						}
						
						if(!nachName.isEmpty()) {
							user.setNachname(nachName);
							if(stub.updateKonto(user,"nachname") != null) {
								System.out.println("User is updated!");
								nachName = "";
								Menu_Logged();
							}
							else{
								System.out.println("Update is failed");
							Konto_Menu();
							}
						}
						
						if(!vorName.isEmpty()) {
							user.setVorname(vorName);
							if(stub.updateKonto(user,"vorname") != null) {
								System.out.println("User is updated!");
								vorName = "";
								Menu_Logged();
							}
							else{
								System.out.println("Update is failed");
								Konto_Menu();
							}
						}
					}
					else
						Konto_Menu();
					break;
				case DELETE:
					System.out.print("Sind Sie sicher, dass Sie Ihr Konto löschen wollen?(ja/nein): ");
					confirm = s.next();
					while(!confirm.equalsIgnoreCase("ja") &&
							!confirm.equalsIgnoreCase("nein")){
						
						System.out.print("Falsche Eingabe! Wiederholen: ");
						confirm = s.next();
					}
					antwort = Ja_Nein.valueOf(confirm.toUpperCase());
					
					if(antwort == Ja_Nein.JA) {
						if(stub.deleteKonto(user)) {
							System.out.println("Ihr Konto ist gelöscht");
							user = null;
							HauptMenu();
						}
						else
							System.out.println("Das Löschen ist schiefgegangen");
						}
					else
						Menu_Logged();
					break;
				case ZURUECK:
					Menu_Logged();
					break;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return;
	}
	
	public static void Termin_Menu() {
		
		return;
	}

	public static void Menu_Logged() {
		String eingabe = "";
		Logged_Utilities input = null;
		
		try {
			System.out.print("\nWollen Sie:\n"
					+ "1. Ihr Konto verwalten.\n"
					+ "2. Ihre Termine verwalten.\n"
					+ "0. Abbrechen.\n"
					+ "Wählen Sie: ");
			eingabe = s.next();
			
			while(!eingabe.equals("0") &&
					!eingabe.equals("1") &&
					!eingabe.equals("2")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = s.next();
			}
			input = Logged_Utilities.values()[Integer.valueOf(eingabe)];
		
			switch(input) {
			case KONTO:
				Konto_Menu();
				break;
			case TERMIN:
				break;
			case EXIT:
				System.out.println("Auf wiedersehen!");
				System.exit(0);
				break;
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
