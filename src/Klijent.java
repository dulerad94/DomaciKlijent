import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Klijent extends JFrame {

	private JPanel contentPane;
	private JComboBox comboBox;
	private JButton btnIzaberi;
	private JTextField txtIzraz;
	private JButton btnIzvrsi;
	private JTextField txtRezultat;
	Socket soket;
	BufferedReader ulazniTok;
	PrintStream izlazniTok;
	private JButton btnZavrsi;
	KlijentNit klijent;
	private JButton btnDodajOperand;
	private JTextField txtOperand;
	private JButton btnObrisiPoslednji;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Klijent frame = new Klijent();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Klijent() {
		setDefaultCloseOperation(izadji());
		setBounds(100, 100, 509, 302);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getComboBox());
		contentPane.add(getBtnIzaberi());
		contentPane.add(getTxtIzraz());
		contentPane.add(getBtnIzvrsi());
		contentPane.add(getTxtRezultat());
		contentPane.add(getBtnZavrsi());
		contentPane.add(getBtnDodajOperand());
		contentPane.add(getTxtOperand());
		contentPane.add(getBtnObrisiPoslednji());
		try {
			soket = new Socket("localhost", 1908);
			uspostaviVeze();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(contentPane, "Greška! Server nije ukljucen", "GRESKA",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private int izadji() {
		if (soket != null) {
			izlazniTok.println("kraj");
			zatvoriVeze();
		}
		return JFrame.EXIT_ON_CLOSE;
	}

	public Klijent getKlijent() {
		return this;
	}

	public JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
			comboBox.setModel(
					new DefaultComboBoxModel(new String[] { "sabiranje", "oduzimanje", "mnozenje", "deljenje" }));
			comboBox.setBounds(29, 41, 119, 20);
		}
		return comboBox;
	}

	private JButton getBtnIzaberi() {
		if (btnIzaberi == null) {
			btnIzaberi = new JButton("Izaberi");
			btnIzaberi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					klijent = new KlijentNit(getKlijent());
					regulisiDugmice(true);
				}
			});
			btnIzaberi.setBounds(171, 40, 95, 23);
		}
		return btnIzaberi;
	}

	public JTextField getTxtIzraz() {
		if (txtIzraz == null) {
			txtIzraz = new JTextField();
			txtIzraz.setBounds(29, 151, 179, 20);
			txtIzraz.setColumns(10);
		}
		return txtIzraz;
	}

	private JButton getBtnIzvrsi() {
		if (btnIzvrsi == null) {
			btnIzvrsi = new JButton("Izvrsi");
			btnIzvrsi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (txtIzraz.getText().equals(""))
						return;
					synchronized (klijent) {
						klijent.notify();
					}
				}

			});
			btnIzvrsi.setEnabled(false);
			btnIzvrsi.setBounds(282, 150, 179, 23);
		}
		return btnIzvrsi;
	}

	public JTextField getTxtRezultat() {
		if (txtRezultat == null) {
			txtRezultat = new JTextField();
			txtRezultat.setBounds(29, 193, 432, 44);
			txtRezultat.setColumns(10);
		}
		return txtRezultat;
	}

	public JButton getBtnZavrsi() {
		if (btnZavrsi == null) {
			btnZavrsi = new JButton("Zavrsi");
			btnZavrsi.setEnabled(false);
			btnZavrsi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					regulisiDugmice(false);
					klijent = null;
				}
			});
			btnZavrsi.setBounds(366, 40, 95, 23);
		}
		return btnZavrsi;
	}

	public JButton getBtnDodajOperand() {
		if (btnDodajOperand == null) {
			btnDodajOperand = new JButton("Dodaj operand");
			btnDodajOperand.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dodajOperand();
				}
			});
			btnDodajOperand.setEnabled(false);
			btnDodajOperand.setBounds(171, 101, 131, 23);
		}
		return btnDodajOperand;
	}

	public JTextField getTxtOperand() {
		if (txtOperand == null) {
			txtOperand = new JTextField();
			txtOperand.setBounds(29, 102, 95, 20);
			txtOperand.setColumns(10);
		}
		return txtOperand;
	}

	public JButton getBtnObrisiPoslednji() {
		if (btnObrisiPoslednji == null) {
			btnObrisiPoslednji = new JButton("Obrisi poslednji");
			btnObrisiPoslednji.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					obrisiPoslednjiOperand();
				}
			});
			btnObrisiPoslednji.setEnabled(false);
			btnObrisiPoslednji.setBounds(330, 101, 131, 23);
		}
		return btnObrisiPoslednji;
	}

	private void uspostaviVeze() {
		try {
			ulazniTok = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlazniTok = new PrintStream(soket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void regulisiDugmice(boolean b) {
		btnIzaberi.setEnabled(!b);
		btnZavrsi.setEnabled(b);
		btnIzvrsi.setEnabled(b);
		btnDodajOperand.setEnabled(b);
		btnObrisiPoslednji.setEnabled(b);
	}

	private void dodajOperand() {
		String izraz = txtIzraz.getText();
		if (izraz.equals(""))
			izraz += txtOperand.getText();
		else
			izraz += klijent.operacija + txtOperand.getText();
		txtIzraz.setText(izraz);
	}

	private void obrisiPoslednjiOperand() {
		String izraz = txtIzraz.getText();
		if (izraz.contains(klijent.operacija))
			izraz = izraz.substring(0, izraz.lastIndexOf(klijent.operacija));
		else
			izraz = "";
		txtIzraz.setText(izraz);
	}

	private void zatvoriVeze() {
		try {
			ulazniTok.close();
			izlazniTok.close();
			soket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
