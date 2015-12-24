import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class KlijentSoket {
	private Socket soket;
	private BufferedReader ulazniTok;
	private PrintStream izlazniTok;
	private KlijentNit klijent;
	
	public KlijentSoket(Socket soket, KlijentNit klijent) {
		this.soket = soket;
		this.klijent=klijent;
		uspostaviVeze();
	}
	public void uspostaviVeze() {
		try {
			ulazniTok = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlazniTok = new PrintStream(soket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void izvrsiOperaciju(){
		
		try {
			String izraz=klijent.getGui().getTxtIzraz().getText();
			izlazniTok.println(izraz);
			String rezultat=ulazniTok.readLine();
			klijent.getGui().getTxtRezultat().setText(rezultat);
			klijent.getGui().getTxtIzraz().setText("");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void zatvoriVeze(){
		try {
			izlazniTok.close();
			ulazniTok.close();
			soket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
