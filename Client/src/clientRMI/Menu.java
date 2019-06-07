package clientRMI;



import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.Console;
import java.text.SimpleDateFormat;

import kalenderApp.KalenderApp;
import utilities.*;

public class Menu {


	private static enum Login_Create{EXIT, LOGIN, CREATE};
	private static enum News_Choice{WEITER, ANNEHMEN, ABLEHNEN};
	private static enum News_Utilities{ZURUECK, TERMIN, ALLES};
	private static enum Konto_Utilities{ZURUECK, UPDATE, DELETE}
	private static enum Konto_Manage{ZURUECK, PASSWORT, EMAIL, VORNAME, NACHNAME, ALLES};
	private static enum Termin_Utilities{ZURUECK, ADD, UPDATE, DELETE, SHOWALL, SHOWTIME, INVITE, INVITATION}
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
	
	public static int isNumber() {
		boolean isNumber = true;
		int number = 0;
		do {
			if(s.hasNextInt()) {
				number = s.nextInt();
				isNumber = true;
			}
			else
			{
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				isNumber = false;
				s.next();
			}
		}while(!isNumber);
		
		return number;
	}
	
	public static void HauptMenu() {
		user = new User();
		
		try {
			Login_Menu();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void Menu_News(String username) {
		
		String eingabe = "";
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
					System.out.print("Geben Sie Bitte die Zahl des News ein: ");
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
					System.out.print("Geben Sie Bitte die Zahl des News ein: ");
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

	public static User Login() {
		
		List<News> NewsRecipientList = null;
		Console console = System.console();
		String userName = "";
		String password = "";
		char[] pwd = null;
		String confirm = null;
		Ja_Nein antwort = null;
		try {
			do {
				System.out.print("Geben Sie ihren Benutzernamen ein: ");
				userName = s.next();
				System.out.print("Geben Sie ihr Passwort ein: ");
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
					System.out.println("Sie haben "+NewsRecipientList.size() + " Notification(s).");
					
					if(NewsRecipientList.size() > 0) {
						System.out.print("Wollen Sie die zeigen?(ja/nein): ");
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
					}
					else
						Menu_Logged();
					return user;
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
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static User Create() {
		
		Console console = System.console();
		String userName = "";
		String password = "";
		char[] pwd = null;
		String vorName = "";
		String nachName = "";
		String email = "";
		String confirm = null;
		Ja_Nein antwort = null;
		List<News> NewsRecipientList = null;
		boolean created = false;
		
		try {
			do {
				System.out.print("Geben Sie ihren Benutzernamen ein: ");
				userName = s.next(); // Exception
				System.out.print("Geben Sie ihr Passwort ein: ");
				if(console != null) {
					pwd = console.readPassword();
					password = String.valueOf(pwd);
				}
				else {
					password = s.next();
				}
				System.out.print("Geben Sie ihren Nachname ein: ");
				nachName = s.next();
				System.out.print("Geben Sie ihren Vorname ein: ");
				vorName = s.next();
				System.out.print("Geben Sie ihr Email ein: ");
				email = s.next();
				user = new User(userName, email, nachName, vorName, password);
				created = stub.creatKonto(user);
				if(created) {
					System.out.println("Das Konto is Created!");
					System.out.println("\nWillkomen "+user.getNachname()+", Sie sind eingeloggen.");
					NewsRecipientList = stub.getNewsRecipientList(user.getUserName());
					System.out.println("\nWillkomen "+user.getNachname()+", Sie sind eingeloggen.");
					System.out.print("Sie haben "+NewsRecipientList.size() + " Notification(s).\n");
					
					if(NewsRecipientList.size() > 0) {
						System.out.print("Wollen Sie die zeigen?(ja/nein): ");
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
					}
					else
						Menu_Logged();
					return user;
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
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	public static boolean Login_Menu(){
		
		String eingabe = "";
		Login_Create input = null;
		
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
				Login();
				break;
			case CREATE:
				Create();
				break;
			case EXIT:
				System.out.println("Auf wiedersehen!");
				System.exit(0);
				break;
			}
		
		return false;
	}

	public static boolean Konto_Update() {
		
		Console console = System.console();
		String eingabe = "";
		char[] pwd = null;
		String passwort = "";
		String nachName = "";
		String vorName = "";
		String email = "";
		String confirm = "";
		Konto_Manage manage = null;
		Ja_Nein antwort = null;
		
		try {
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
					System.out.print("Geben Sie ihr neues Passwort ein: ");
					
					if(console == null) {
						passwort = s.next();
					}
					else {
						pwd = console.readPassword();
						passwort = String.valueOf(pwd);
					}
					break;
				case EMAIL:
					System.out.print("Geben Sie ihr neues Email ein: ");
					email = s.next();
					break;
				case NACHNAME:
					System.out.print("Geben Sie ihren neuen Nachname ein: ");
					nachName = s.next();
					break;
				case VORNAME:
					System.out.print("Geben Sie ihren neuen Vorname ein: ");
					vorName = s.next();
					break;
				case ALLES:
					System.out.print("Geben Sie ihr neues Passwort ein: ");
					if(console != null) {
						pwd = console.readPassword();
						passwort = String.valueOf(pwd);
					}
					else {
						passwort = s.next();
					}
					System.out.print("Geben Sie ihren neuen Nachname ein: ");
					nachName = s.next();
					System.out.print("Geben Sie ihren neuen Vorname ein: ");
					vorName = s.next();
					System.out.print("Geben Sie ihr neues Email ein: ");
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
							return true;
						}
						else{
							System.out.println("Update is failed");
							Konto_Menu();
							return false;
						}
					}
					
					if(!passwort.isEmpty()){
						user.setPasswort(passwort);
						if(stub.updateKonto(user,"passwort") != null) {
							System.out.println("User is updated!");
							passwort = "";
							Menu_Logged();
							return true;
						}
						else {
							System.out.println("Update is failed");
							Konto_Menu();
							return false;
						}
					}
					
					if(!email.isEmpty()) {
						
						user.setEmail(email);
						
						if(stub.updateKonto(user,"email") != null) {
							System.out.println("User is updated!");
							email = "";
							Menu_Logged();
							return true;
						}
						else{
							System.out.println("Update is failed");
							Konto_Menu();
							return false;
						}
					}
					
					if(!nachName.isEmpty()) {
						user.setNachname(nachName);
						if(stub.updateKonto(user,"nachname") != null) {
							System.out.println("User is updated!");
							nachName = "";
							Menu_Logged();
							return true;
						}
						else{
							System.out.println("Update is failed");
							Konto_Menu();
							return false;
						}
					}
					
					if(!vorName.isEmpty()) {
						user.setVorname(vorName);
						if(stub.updateKonto(user,"vorname") != null) {
							System.out.println("User is updated!");
							vorName = "";
							Menu_Logged();
							return true;
						}
						else{
							System.out.println("Update is failed");
							Konto_Menu();
							return false;
						}
					}
				}
				else {
					Konto_Menu();
					return false;
				}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	public static boolean Konto_Delete() {
		
		String confirm = "";
		Ja_Nein antwort = null;
		
		try {
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
					return true;
				}
				else {
					System.out.println("Das Löschen ist schiefgegangen");
					return false;
				}
			}
			else
				Menu_Logged();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	public static void Konto_Menu() {
		
		String eingabe = "";
		Konto_Utilities input = null;
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
				Konto_Update();
				break;
			case DELETE:
				Konto_Delete();
				break;
			case ZURUECK:
				Menu_Logged();
				break;
			}
		return;
	}
	
	public static int Termin_Add() {
		
		Termin termin = null;
		String titel = "";
		int termin_Id = 0;
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		String day_parse = "";
		String month_parse = "";
		String hour_parse = "";
		String minute_parse = "";
		String second_parse = "";
		String confirm = "";
		Ja_Nein antwort = null;
		Date t_datum = null;
		
		try {
			System.out.print("Geben Sie einen Titel des Ereignis ein: ");
			titel = s.next(); //Exception
			System.out.print("Geben Sie den Tag ein: ");
			day = isNumber();
			while(day <= 0 || day > 31) {
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				day = isNumber();
			}
			if(day < 10)
				day_parse = "0"+day;
			else
				day_parse = String.valueOf(day);
			
			System.out.print("Geben Sie den Monat ein: ");
			month = isNumber();
			while(month <= 0 || month > 12) {
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				month = isNumber();
			}
			if(month < 10)
				month_parse = "0"+month;
			else
				month_parse = String.valueOf(month);
			
			System.out.print("Geben Sie das Jahr ein: ");
			year = isNumber();
			while(year < Calendar.getInstance().get(Calendar.YEAR)) {
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				year = isNumber();
			}
			
			System.out.print("Geben Sie die Stunde ein: ");
			hour = isNumber();
			while(hour < 0 || hour > 23) {
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				hour = isNumber();
			}
			if(hour < 10)
				hour_parse = "0"+hour;
			else
				hour_parse = String.valueOf(hour);
			
			System.out.print("Geben Sie die Minuten ein: ");
			minute = isNumber();
			while(minute < 0 || minute > 59) {
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				minute = isNumber();
			}
			if(minute < 10)
				minute_parse = "0"+minute;
			else
				minute_parse = String.valueOf(minute);
			
			System.out.print("Geben Sie die Sekunden ein: ");
			second = isNumber();
			while(second < 0 || second > 59) {
				System.out.print("Geben Sie eine gültige Zahl ein: ");
				second = isNumber();
			}
			if(second < 10)
				second_parse = "0"+second;
			else
				second_parse = String.valueOf(second);
			
			t_datum = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(year + "-" 
					+ month_parse + "-"
					+ day_parse + " "
					+ hour_parse + ":"
					+ minute_parse + ":"
					+ second_parse);
			
			termin = new Termin(user.getUserName(),titel,t_datum);
			
			System.out.print("Sind Sie sicher, dass Sie den Termin hinzufügen wollen?(ja/nein): ");
			
			confirm = s.next();
			
			while(!confirm.equalsIgnoreCase("ja") &&
					!confirm.equalsIgnoreCase("nein")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				confirm = s.next();
			}
			
			antwort = Ja_Nein.valueOf(confirm.toUpperCase());
			
			if(antwort == Ja_Nein.JA) {
				termin_Id = stub.addTermin(termin);
				if(termin_Id < 0) {
					System.out.println("Termin nicht hinzugefügt!");
					Termin_Menu();
					return -1;
				}
				else {
					System.out.println("Termin ist hinzugefügt!");
					Termin_Menu();
					return termin_Id;
				}
			}
			else
				Menu_Logged();
				
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return -1;
	}
	
	public static void Termin_Menu() {
		
		Termin termin = null;
		int termin_Id = 0;
		String eingabe = "";
		String confirm = "";
		Ja_Nein antwort = null;
		Date t_datum = new Date();
		Termin_Utilities input = null;
		List<Termin> termine = null;
		
		try {
			System.out.print("\nWollen Sie: \n"
					+ "1. Termin erstellen.\n"
					+ "2. Termin bearbeiten.\n"
					+ "3. Termin löschen.\n"
					+ "4. Alle Termine anzeigen.\n"
					+ "5. Zeitspanne vom Kalender.\n"
					+ "6. Einladen.\n"
					+ "7. Gesendete Einladungen.\n"
					+ "0. Zurueck.\n"
					+ "Wählen Sie: ");
			eingabe = s.next();
			
			while(!eingabe.equals("0") && 
					!eingabe.equals("1") && 
					!eingabe.equals("2") &&
					!eingabe.equals("3") && 
					!eingabe.equals("4") && 
					!eingabe.equals("5") && 
					!eingabe.equals("6") && 
					!eingabe.equals("7")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = s.next();
			}
			
			input = Termin_Utilities.values()[Integer.valueOf(eingabe)];
			
			switch(input) {
			case ADD:
				termin_Id = Termin_Add();
				break;
			case UPDATE:
				break;
			case DELETE:
				System.out.print("");
				break;
			case SHOWALL:
				System.out.println(user.getUserName());
				termine = stub.getMyTermine(user.getUserName());
				//for(Termin t : termine)
					System.out.println(termine.size());
				break;
			case SHOWTIME:
				break;
			case INVITE:
				break;
			case INVITATION:
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
				Termin_Menu();
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
