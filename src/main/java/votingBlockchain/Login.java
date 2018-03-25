package votingBlockchain;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame{

	JButton loginbutton = new JButton("Login");
	JPanel panel = new JPanel(new GridBagLayout());
	GridBagConstraints gcs = new GridBagConstraints();
	JTextField userfield = new JTextField(15);
	JPasswordField pwfield = new JPasswordField(15);
	JLabel usertext = new JLabel("User ID: ");
	JLabel pwtext = new JLabel("Password");
	JLabel headtext = new JLabel("Welcome to the voting system.");
	JLabel headtext2 = new JLabel("Please enter your userID and password.");
	
	public Login(){
		super("Login Autentification");
		setSize(300,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(panel);
		placeComponents(panel);
		setVisible(true);
		readInfo();
	}
	private void placeComponents(JPanel panel2) {
		panel2.setLayout(null);
		
		headtext.setBounds(10,10, 300, 25);
		panel.add(headtext);
		
		headtext2.setBounds(10,40,300,25);
		panel.add(headtext2);
		
		usertext.setBounds(10, 80 , 80 ,25);
		panel.add(usertext);
		
		userfield.setBounds(100,80,160,25);
		panel.add(userfield);
		
		pwtext.setBounds(10,120,80,25);
		panel.add(pwtext);
		
		pwfield.setBounds(100,120,160,25);
		panel.add(pwfield);
		
		loginbutton.setBounds(95, 160,80,25);
		panel.add(loginbutton);
	}
	public void readInfo() {
		loginbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				String user = userfield.getText();
				String pw = pwfield.getText();
				String check = "not match";
				check = VerifyAccount(user, pw); 
				if (check.equals("user")) {
					JOptionPane.showMessageDialog(panel, "Login Successful.\nProceeding to Voting Interface");
					setVisible(false);
					dispose();
				}
				if (check.equals("admin")) {
					dispose();
					setVisible(false);
					JOptionPane.showMessageDialog(panel, "Welcome "+user+".\nProceeding to Configuration Panel");
				}
				if (check.equals("not match"))
					JOptionPane.showMessageDialog(null, "Wrong UserID or Password!","ERROR" ,JOptionPane.ERROR_MESSAGE);
					userfield.setText("");
					pwfield.setText("");
					userfield.requestFocus();
				}
			
	});
	}

	private String VerifyAccount(String user, String pw) {
		if (user.equals("user") && pw.equals("pw")) {
			return "user";
		}
		if (user.equals("admin") && pw.equals("admin")) {
			return "admin";
		}
		return "not match";
		
	}
}
	