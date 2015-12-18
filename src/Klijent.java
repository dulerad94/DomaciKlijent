import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class Klijent extends JFrame {

	private JPanel contentPane;
	private JComboBox comboBox;
	private JButton btnIzaberi;
	private JTextField txtBrojevi;
	private JButton btnIzvrsi;
	private JTextField txtRezultat;
	Socket soket;
	BufferedReader ulazniTok;
	DataOutputStream izlazniTok;
	private JButton btnZavrsi;
	KlijentNit klijent;

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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getComboBox());
		contentPane.add(getBtnIzaberi());
		contentPane.add(getTxtBrojevi());
		contentPane.add(getBtnIzvrsi());
		contentPane.add(getTxtRezultat());
		contentPane.add(getBtnZavrsi());
	}

	private int izadji() {
		if (soket != null) {
			try {
				izlazniTok.writeBytes("kraj");
				ulazniTok.close();
				izlazniTok.close();
				soket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
						try {
							soket = new Socket("localhost", 1908);		
							uspostaviVeze();
							klijent = new KlijentNit(getKlijent());
							regulisiDugmice(true);
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}			
					
				}
			});
			btnIzaberi.setBounds(171, 40, 89, 23);
		}
		return btnIzaberi;
	}

	public JTextField getTxtBrojevi() {
		if (txtBrojevi == null) {
			txtBrojevi = new JTextField();
			txtBrojevi.setBounds(29, 122, 119, 20);
			txtBrojevi.setColumns(10);
		}
		return txtBrojevi;
	}

	private JButton getBtnIzvrsi() {
		if (btnIzvrsi == null) {
			btnIzvrsi = new JButton("Izvrsi");
			btnIzvrsi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
				
			});
			btnIzvrsi.setEnabled(false);
			btnIzvrsi.setBounds(171, 121, 89, 23);
		}
		return btnIzvrsi;
	}

	public JTextField getTxtRezultat() {
		if (txtRezultat == null) {
			txtRezultat = new JTextField();
			txtRezultat.setBounds(29, 175, 119, 20);
			txtRezultat.setColumns(10);
		}
		return txtRezultat;
	}

	private JButton getBtnZavrsi() {
		if (btnZavrsi == null) {
			btnZavrsi = new JButton("Zavrsi");
			btnZavrsi.setEnabled(false);
			btnZavrsi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					regulisiDugmice(false);
					klijent = null;
				}
			});
			btnZavrsi.setBounds(293, 40, 89, 23);
		}
		return btnZavrsi;
	}

	private void uspostaviVeze() {
		try {
			ulazniTok = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			izlazniTok = new DataOutputStream(soket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void regulisiDugmice(boolean b){
		btnIzaberi.setEnabled(!b);
		btnZavrsi.setEnabled(b);
		btnIzvrsi.setEnabled(b);
	}

}
