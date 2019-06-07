package serverRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import kalenderApp.KalenderAppImp;

public class MainClass {

	public static void main(String[] args) {
		try {
			
			LocateRegistry.createRegistry(1099);
			KalenderAppImp od = new KalenderAppImp();
			System.out.println(od.toString());
			Naming.rebind("rmi://localhost:1099/KalenderApp", od);
			System.out.println("Server ready...\n");
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
