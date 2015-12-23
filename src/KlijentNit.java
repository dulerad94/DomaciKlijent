import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

public class KlijentNit implements Runnable {
	private Klijent gui;
	private Thread t;
	private String odabrano;
	private String operacija;

	public KlijentNit(Klijent gui) {
		this.gui = gui;
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public String getOperacija() {
		return operacija;
	}

	public Klijent getGui() {
		return gui;
	}

	@Override
	public void run() {
		try {
			odabrano = (String) gui.getComboBox().getSelectedItem();
			gui.saljiPodatke(odabrano);
			operacija = odrediOperaciju();
			String odgovor = gui.citajPodatke();
			String ServerId=gui.citajPodatke();
			if (!odgovor.equals("moze"))
				return;
			synchronized (this) {
				wait();
				gui.saljiPodatke(ServerId);
				KlijentSoket soketZaPodatke = new KlijentSoket(new Socket("localhost", 123), this);
				soketZaPodatke.izvrsiOperaciju();
				soketZaPodatke.zatvoriVeze();
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
