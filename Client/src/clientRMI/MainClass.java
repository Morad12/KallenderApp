package clientRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import kalenderApp.KalenderApp;

public class MainClass {

	public static void main(String[] args) {
		verbindungsfunktion();
	}
	
	public static KalenderApp verbindungsfunktion() {
		KalenderApp stub = null;
		try {
			stub = (KalenderApp) Naming.lookup("rmi://localhost:1099/KalenderApp");
			System.out.println("verbunden...\n");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println(e.toString());
		}
		return stub;
	}

}
