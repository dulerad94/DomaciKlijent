import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class KlijentNit implements Runnable {
	Klijent klijent;
	Thread t;
	boolean pritisnuto=false;
	public KlijentNit(Klijent klijent) {
		this.klijent=klijent;
		t = new Thread(this);
		t.setDaemon(true);
		t.start();

	}

	
	@Override
	public void run() {		
		try {
			String odabrano=(String) klijent.getComboBox().getSelectedItem();
			klijent.izlazniTok.println(odabrano);
			
			String odgovor=klijent.ulazniTok.readLine();
			
			if(!odgovor.equals("moze")) return;	
			
			izvrsiOperaciju();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void izvrsiOperaciju(){
		
		try {
			Socket soket = new Socket("localhost",123);
			BufferedReader ulazniTok=new BufferedReader(new InputStreamReader(soket.getInputStream()));
			PrintStream izlazniTok=new PrintStream(soket.getOutputStream());
			String brojevi=klijent.getTxtBrojevi().getText()+"\n";
			izlazniTok.println(brojevi);
			klijent.getTxtRezultat().setText(ulazniTok.readLine());
			izlazniTok.close();
			ulazniTok.close();
			soket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
