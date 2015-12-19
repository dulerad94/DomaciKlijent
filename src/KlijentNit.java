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
	String operand;
	Stack<String> operandi;
	public KlijentNit(Klijent klijent) {
		this.klijent=klijent;
		operandi=new Stack<>();
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	
	@Override
	public void run() {		
		try {
			odabrano=(String) klijent.getComboBox().getSelectedItem();
			klijent.izlazniTok.println(odabrano);
			operand=odrediOperand();
			String odgovor=klijent.ulazniTok.readLine();
			if(!odgovor.equals("moze")) return;	
			KlijentSoket kl=new KlijentSoket(new Socket("localhost",123),this);
			kl.izvrsiOperaciju();
			kl.zatvoriVeze();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void dodaj(){
		if(klijent.getTxtIzraz().getText()=="") klijent.getTxtIzraz().setText(klijent.getTxtOperand().getText());
		else klijent.getTxtIzraz().setText(operand+klijent.getTxtOperand().getText());
	}
	private String odrediOperand(){
		if(odabrano.equals("sabiranje")) return"+";
		if(odabrano.equals("oduzimanje")) return"-";
		if(odabrano.equals("mnozenje")) return"*";
		if(odabrano.equals("deljenje")) return"/";
		throw new RuntimeException();
	}


}
