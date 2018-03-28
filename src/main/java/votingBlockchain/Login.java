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
		panel2.add(headtext);
		
		headtext2.setBounds(10,40,300,25);
		panel2.add(headtext2);
		
		usertext.setBounds(10, 80 , 80 ,25);
		panel2.add(usertext);
		
		userfield.setBounds(100,80,160,25);
		panel2.add(userfield);
		
		pwtext.setBounds(10,120,80,25);
		panel2.add(pwtext);
		
		pwfield.setBounds(100,120,160,25);
		panel2.add(pwfield);
		
		loginbutton.setBounds(95, 160,80,25);
		panel2.add(loginbutton);
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
					initVote();
				}
				if (check.equals("admin")) {
					dispose();
					setVisible(false);
					JOptionPane.showMessageDialog(panel, "Welcome "+user+".\nProceeding to Configuration Panel");
					Admin admin = new Admin();
				}
				if (check.equals("voted")) {
					dispose();
					setVisible(false);
					JOptionPane.showMessageDialog(null, "This user has already voted.\nIf you have not voted, please contact the adminstrator.","ERROR" ,JOptionPane.ERROR_MESSAGE);
				}
				if (check.equals("not match"))
					JOptionPane.showMessageDialog(null, "Wrong UserID or Password!","ERROR" ,JOptionPane.ERROR_MESSAGE);
					userfield.setText("");
					pwfield.setText("");
					userfield.requestFocus();
				}


	});
	}
// function to connect blockchain
/*
 * 4 user state
 * user = the login is correct, allow entry to vote function
 * admin = the login is correct and the user type is admin, proceed to the admin page
 * voted = the user is detected to have voted, block the access
 * not match = the user and pw not matching, turn back to login page
 */
	private String VerifyAccount(String user, String pw) {
		if (user.equals("user") && pw.equals("pw")) {
			return "user";
		}
		if (user.equals("admin") && pw.equals("admin")) {
			return "admin";
		}
		if (user.equals("user") && pw.equals("0")) {
			return "voted";
		}
		return "not match";
		
	}
	private void initVote() {
		Vote vote = new Vote();
		
	}
	
}
	