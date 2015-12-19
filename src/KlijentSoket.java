import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class KlijentSoket {
	Socket soket;
	BufferedReader ulazniTok;
	PrintStream izlazniTok;
	KlijentNit klijentskaNit;
	
	public KlijentSoket(Socket soket, KlijentNit klijentskaNit) {
		this.soket = soket;
		this.klijentskaNit=klijentskaNit;
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
			String izraz=klijentskaNit.klijent.getTxtIzraz().getText()+"\n";
			izlazniTok.println(izraz);
			klijentskaNit.klijent.getTxtRezultat().setText(ulazniTok.readLine());
			
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
