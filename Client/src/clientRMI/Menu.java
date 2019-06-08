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
	private static enum News_Utilities{ZURUECK, NEWS, ALLES};
	private static enum Konto_Utilities{ZURUECK, UPDATE, DELETE}
	private static enum Konto_Manage{ZURUECK, PASSWORT, EMAIL, VORNAME, NACHNAME, ALLES};
	private static enum Termin_Utilities{ZURUECK, ADD, UPDATE, DELETE, SHOWALL, SHOWTIME, INVITE, INVITATION};
	private static enum Termin_Manage{ZURUECK, TITEL, DATE, ALLES};
	private static enum Logged_Utilities{EXIT, KONTO, TERMIN};
	private static enum Ja_Nein{JA, NEIN};
	private static User user;
	private static Scanner s = new Scanner(System.in).useDelimiter("\n");
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
		
		int eingabe = 0;
		int news_id = 0;
		News_Choice choice = null;
		News_Utilities accepted = null;
		News_Utilities refused = null;
		
		try {
			List<News> NewsRecipientList = stub.getNewsRecipientList(username);
			
			for(int i = 0; i < NewsRecipientList.size(); i++) {
				System.out.print((i+1)+": "+ NewsRecipientList.get(i).toString());
			}
			System.out.print("\nWollen Sie:\n"
					+ "1. Annehmen.\n"
					+ "2. Ablehnen.\n"
					+ "0. Weiter.\n"
					+ "Wählen Sie: ");
			eingabe = isNumber();
			
			while(eingabe < 0 || eingabe > 2){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = isNumber();
			}
			
			choice = News_Choice.values()[eingabe];
			
			switch(choice) {
			case ANNEHMEN:
				System.out.print("\nWollen Sie:\n"
						+ "1. News wählen.\n"
						+ "2. Alles annehmen.\n"
						+ "0. Zurück.\n"
						+ "Wählen Sie: ");
				eingabe = isNumber();
				
				while(eingabe < 0 || eingabe > 2){
					
					System.out.print("Falsche Eingabe! Wiederholen: ");
					eingabe = isNumber();
				}
				
				accepted = News_Utilities.values()[eingabe];
				
				switch(accepted) {
				case NEWS:
					System.out.print("Geben Sie Bitte die Zahl des News ein: ");
					news_id = isNumber();
					
					while(news_id <= 0 || news_id > NewsRecipientList.size()) {
						System.out.print("Falsche Eingabe! Wiederholen: ");
						news_id = isNumber();
					}
					
					if(stub.acceptNews(NewsRecipientList.get(news_id - 1)) != -1){
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
				eingabe = isNumber();
				
				while(eingabe < 0 || eingabe > 2){
					
					System.out.print("Falsche Eingabe! Wiederholen: ");
					eingabe = isNumber();
				}
				
				refused = News_Utilities.values()[eingabe];
				
				switch(refused) {
				case NEWS:
					System.out.print("Geben Sie Bitte die Zahl des News ein: ");
					news_id = isNumber();
					
					while(news_id <= 0 || news_id > NewsRecipientList.size()) {
						System.out.print("Falsche Eingabe! Wiederholen: ");
						news_id = isNumber();
					}
					
					if(stub.deleteNews(NewsRecipientList.get(news_id - 1))){
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
					System.out.println("Sie haben "+ NewsRecipientList.size() + " Notification(s).");
					
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
		
		int eingabe = 0;
		Login_Create input = null;
		
		System.out.print("\nWollen Sie:\n"
				+ "1. Login.\n"
				+ "2. Konto erstellen.\n"
				+ "0. Abbrechen.\n"
				+ "Wählen Sie: ");
		eingabe = isNumber();
			
		while(eingabe < 0 || eingabe > 2){
				
			System.out.print("Falsche Eingabe! Wiederholen: ");
			eingabe = isNumber();
		}
		input = Login_Create.values()[eingabe];
			
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
		int eingabe = 0;
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
			eingabe = isNumber();
			
			while(eingabe < 0 || eingabe > 5){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = isNumber();
			}
				manage = Konto_Manage.values()[eingabe];
				
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
		
		int eingabe = 0;
		Konto_Utilities input = null;
			System.out.print("\nWollen Sie Ihr Konto: \n"
					+ "1. Bearbeiten.\n"
					+ "2. Löschen.\n"
					+ "0. Zurück.\n"
					+ "Wählen Sie: ");
			eingabe = isNumber();
			
			while(eingabe < 0 || eingabe > 2){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = isNumber();
			}
			
			input = Konto_Utilities.values()[eingabe];
			
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
	
	public static Termin Termin_Add() {
		
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
			
			System.out.print("Geben Sie die Stunden ein: ");
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
					return null;
				}
				else {
					System.out.println("Termin ist hinzugefügt!");
					Termin_Menu();
					return termin;
				}
			}
			else {
				Menu_Logged();
				return null;
			}
				
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	public static boolean Termin_Delete(List<Termin> termine) {
		
		int termin_Id = 0;
		String confirm = "";
		Ja_Nein antwort = null;
		try {
			System.out.print("Wählen Sie den Termin zu löschen: ");
			termin_Id = isNumber();
			
			while(termin_Id <= 0 || termin_Id > termine.size()){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				termin_Id = isNumber();
			}
			
			System.out.print("Sind Sie sicher, dass Sie den Termin löschen wollen?(ja/nein): ");
			
			confirm = s.next();
			
			while(!confirm.equalsIgnoreCase("ja") &&
					!confirm.equalsIgnoreCase("nein")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				confirm = s.next();
			}
			
			antwort = Ja_Nein.valueOf(confirm.toUpperCase());
			
			if(antwort == Ja_Nein.JA) {
				if(stub.deleteTermin(termin_Id - 1)) {
					System.out.println("Termin ist gelöscht");
					Termin_Menu();
					termin_Id = 0;
					return true;
				}
				else {
					System.out.println("Termin ist nicht gelöscht");
					Termin_Menu();
					return false;
				}
			}
			else {
				Termin_Menu();
				return false;
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	public static boolean Termin_Update(List<Termin> termine) {
		
		int eingabe = 0;
		String confirm = "";
		Ja_Nein antwort = null;
		Termin_Manage manage = null;
		String titel = "";
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
		Termin termin = null;
		int termin_Id = 0;
		Date t_datum = null;
		
		try {
			System.out.print("Wählen Sie den Termin zu bearbeiten: ");
			termin_Id = isNumber();
			
			while(termin_Id <= 0 || termin_Id > termine.size()){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				termin_Id = isNumber();
			}
			
			System.out.print("Was wollen ändern:\n"
					+ "1. Titel.\n"
					+ "2. Datum.\n"
					+ "3. Alles.\n"
					+ "0. Zurück.\n"
					+ "Wählen Sie: ");
			eingabe = isNumber();
			
			while(eingabe < 0 || eingabe > 3){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = isNumber();
			}
			
			manage = Termin_Manage.values()[eingabe];
			
			switch(manage) {
			case TITEL:
				System.out.print("Geben Sie einen neuen Titel des Ereignis ein: ");
				titel = s.next(); //Exception
				break;
			case DATE:
				System.out.print("Geben Sie den neuen Tag ein: ");
				day = isNumber();
				while(day <= 0 || day > 31) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					day = isNumber();
				}
				if(day < 10)
					day_parse = "0"+day;
				else
					day_parse = String.valueOf(day);
				
				System.out.print("Geben Sie den neuen Monat ein: ");
				month = isNumber();
				while(month <= 0 || month > 12) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					month = isNumber();
				}
				if(month < 10)
					month_parse = "0"+month;
				else
					month_parse = String.valueOf(month);
				
				System.out.print("Geben Sie das neue Jahr ein: ");
				year = isNumber();
				while(year < Calendar.getInstance().get(Calendar.YEAR)) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					year = isNumber();
				}
				
				System.out.print("Geben Sie die neuen Stunden ein: ");
				hour = isNumber();
				while(hour < 0 || hour > 23) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					hour = isNumber();
				}
				if(hour < 10)
					hour_parse = "0"+hour;
				else
					hour_parse = String.valueOf(hour);
				
				System.out.print("Geben Sie die neuen Minute ein: ");
				minute = isNumber();
				while(minute < 0 || minute > 59) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					minute = isNumber();
				}
				if(minute < 10)
					minute_parse = "0"+minute;
				else
					minute_parse = String.valueOf(minute);
				
				System.out.print("Geben Sie die neuen Sekunden ein: ");
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
				break;
			case ALLES:
				System.out.print("Geben Sie einen neuen Titel des Ereignis ein: ");
				titel = s.next(); //Exception
				
				System.out.print("Geben Sie den neuen Tag ein: ");
				day = isNumber();
				while(day <= 0 || day > 31) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					day = isNumber();
				}
				if(day < 10)
					day_parse = "0"+day;
				else
					day_parse = String.valueOf(day);
				
				System.out.print("Geben Sie den neuen Monat ein: ");
				month = isNumber();
				while(month <= 0 || month > 12) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					month = isNumber();
				}
				
				if(month < 10)
					month_parse = "0"+month;
				else
					month_parse = String.valueOf(month);
				
				System.out.print("Geben Sie das neue Jahr ein: ");
				year = isNumber();
				while(year < Calendar.getInstance().get(Calendar.YEAR)) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					year = isNumber();
				}
				
				System.out.print("Geben Sie die neuen Stunden ein: ");
				hour = isNumber();
				while(hour < 0 || hour > 23) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					hour = isNumber();
				}
				if(hour < 10)
					hour_parse = "0"+hour;
				else
					hour_parse = String.valueOf(hour);
				
				System.out.print("Geben Sie die neuen Minute ein: ");
				minute = isNumber();
				while(minute < 0 || minute > 59) {
					System.out.print("Geben Sie eine gültige Zahl ein: ");
					minute = isNumber();
				}
				if(minute < 10)
					minute_parse = "0"+minute;
				else
					minute_parse = String.valueOf(minute);
				
				System.out.print("Geben Sie die neuen Sekunden ein: ");
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
				break;
			case ZURUECK:
				Termin_Menu();
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
				
				if(t_datum != null && !titel.isEmpty()) {
					termin = termine.get(termin_Id - 1);
					termin.setDateTime(t_datum);
					termin.setTerminName(titel);
					if(stub.updateTermin(termin.getTerminId(), "terminName") != null) {
						if(stub.updateTermin(termin.getTerminId(), "terminDateTime") != null) {
							System.out.println("Termin is updated!");
							t_datum = null;
							termin = null;
							titel = "";
							Termin_Menu();
							return true;
						}
					}
					else {
						System.out.println("Termin is not updated!");
						Termin_Menu();
						return false;
					}
				}
				
				if(!titel.isEmpty()) {
					termin = termine.get(termin_Id - 1);
					termin.setTerminName(titel);
					if(stub.updateTermin(termin.getTerminId(), "terminName") != null) {
						System.out.println("Titel is updated!");
						titel = "";
						termin = null;
						Termin_Menu();
						return true;
					}
					else
					{
						System.out.println("Titel is not updated!");
						Termin_Menu();
						return false;
					}
				}
				
				if(t_datum != null) {
					termin = termine.get(termin_Id - 1);
					termin.setDateTime(t_datum);
					
					if(stub.updateTermin(termin.getTerminId(), "terminDateTime") != null) {
						System.out.println("Date is updated!");
						termin = null;
						t_datum = null;
						Termin_Menu();
						return true;
					}
					else {
						System.out.println("Date is not updated!");
						Termin_Menu();
						return false;
					}
				}
			}
			else {
				Termin_Menu();
				return false;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	public static boolean Einladen(List<Termin> termine) {
		
		int termin_Id = 0;
		News news = null;
		String recipient = "";
		String confirm = "";
		Ja_Nein antwort = null;
		
		try {
			System.out.println("Wen wollen Sie einladen!: ");
			recipient = s.next(); // Exception
			//System.out.println("Ihre Termine sind: ");
			//Termin_ShowAll(termine);
			System.out.print("An welchen Termin wollen Sie Sie einladen?: ");
			termin_Id = s.nextInt();
			
			System.out.print("Sind Sie sicher, dass Sie Sie einladen wollen?(ja/nein): ");
			confirm = s.next();
			
			while(!confirm.equalsIgnoreCase("ja") &&
					!confirm.equalsIgnoreCase("nein")){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				confirm = s.next();
			}
			antwort = Ja_Nein.valueOf(confirm.toUpperCase());
			
			if(antwort == Ja_Nein.JA) {
				
				news = new News(user.getUserName(),recipient , termine.get(termin_Id - 1).getTerminId());
				if(stub.userEinladen(news)) {
					System.out.println("User ist eingeladen.");
					Termin_Menu();
					return true;
				}
				else {
					System.out.println("User konnte nicht eingeladen sein.");
					Termin_Menu();
					return false;
				}
			}
			else
				Termin_Menu();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	public static void Termin_ShowAll(List<Termin> termine) {
		
		for(int i = 0; i < termine.size();i ++)
			System.out.println((i+1) + ":" + termine.get(i).toString());
	}
	
	public static void Termin_Span() {
		
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
		Date t_datumv = null;
		Date t_datumb = null;
		List<Termin> termine = null;
		
		try {
			System.out.println("Geben Sie die Datum von ein: ");
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
			
			System.out.print("Geben Sie die Stunden ein: ");
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
			
			t_datumv = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(year + "-" 
					+ month_parse + "-"
					+ day_parse + " "
					+ hour_parse + ":"
					+ minute_parse + ":"
					+ second_parse);
			
			System.out.println("Geben Sie die Datum bis ein: ");
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
			
			System.out.print("Geben Sie die Stunden ein: ");
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
			
			t_datumb = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(year + "-" 
					+ month_parse + "-"
					+ day_parse + " "
					+ hour_parse + ":"
					+ minute_parse + ":"
					+ second_parse);
			termine = stub.searchSpan(t_datumv, t_datumb, user.getUserName());
			
			for(Termin t : termine) {
				int i = 1;
				System.out.println(i + ": " + t.toString());
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void Invitation(List<News> NewsSenderList) {
		int i = 1;
		for(News n : NewsSenderList) {
			System.out.println(i + ": " + n.toString());
			i++;
		}
	}
	
	public static void Termin_Menu() {
		
		int eingabe = 0;
		Termin_Utilities input = null;
		List<Termin> termine = null;
		List<News> NewsSenderList = null;
		
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
			eingabe = isNumber();
			
			while(eingabe < 0 || eingabe > 7){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = isNumber();
			}
			
			input = Termin_Utilities.values()[eingabe];
			
			termine = stub.getMyTermine(user.getUserName());
			NewsSenderList = stub.getNewsSenderList(user.getUserName());
			
			switch(input) {
			case ADD:
				Termin_Add();
				break;
			case UPDATE:
				//System.out.println("Ihre Termine sind: ");
				//Termin_ShowAll(termine);
				Termin_Update(termine);
				break;
			case DELETE:
				//System.out.println("Ihre Termine sind: ");
				//Termin_ShowAll(termine);
				Termin_Delete(termine);
				break;
			case SHOWALL:
				Termin_ShowAll(termine);
				Termin_Menu();
				break;
			case SHOWTIME:
				Termin_Span();
				break;
			case INVITE:
				Einladen(termine);
				break;
			case INVITATION:
				Invitation(NewsSenderList);
				Termin_Menu();
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
		int eingabe = 0;
		Logged_Utilities input = null;
		
		try {
			System.out.print("\nWollen Sie:\n"
					+ "1. Ihr Konto verwalten.\n"
					+ "2. Ihre Termine verwalten.\n"
					+ "0. Abbrechen.\n"
					+ "Wählen Sie: ");
			eingabe = isNumber();
			
			while(eingabe < 0 || eingabe > 2){
				
				System.out.print("Falsche Eingabe! Wiederholen: ");
				eingabe = isNumber();
			}
			input = Logged_Utilities.values()[eingabe];
		
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
