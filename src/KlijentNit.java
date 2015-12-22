import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

public class KlijentNit implements Runnable {
	Klijent klijent;
	Thread t;
	String odabrano;
	String operacija;

	public KlijentNit(Klijent klijent) {
		this.klijent = klijent;
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		try {
			odabrano = (String) klijent.getComboBox().getSelectedItem();
			klijent.izlazniTok.println(odabrano);
			operacija = odrediOperaciju();
			String odgovor = klijent.ulazniTok.readLine();
			if (!odgovor.equals("moze"))
				return;
			synchronized (this) {
				while (true) {
					System.out.println("eo me");
					wait();
					System.out.println("prosa");
					KlijentSoket kl = new KlijentSoket(new Socket("localhost", 123), this);
					kl.izvrsiOperaciju();
					kl.zatvoriVeze();
					System.out.println("polomio vetar grane");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String odrediOperaciju() {
		if (odabrano.equals("sabiranje"))
			return "+";
		if (odabrano.equals("oduzimanje"))
			return "-";
		if (odabrano.equals("mnozenje"))
			return "*";
		if (odabrano.equals("deljenje"))
			return "/";
		throw new RuntimeException("Otkud ovde mrco?????");
	}

}
