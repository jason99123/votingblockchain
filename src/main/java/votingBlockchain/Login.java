package votingBlockchain;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.json.*;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
public class Login extends JFrame{

	/*
	 * Login class contains most of the function to allow user to access the program
	 * The class is constructed on JFrame
	 */
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
	/*
	 * readInfo accepts input from user of the username and password
	 * it will distinguish user from different groups and direct to Admin or Vote class
	 */
	public void readInfo() {
		loginbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				String user = userfield.getText();
				String pw = pwfield.getText();
				String check = "notmatch";
				try {
					check = VerifyAccount(user, pw);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				// voter usergroup condition
				if (check.equals("user")) {
					JOptionPane.showMessageDialog(panel, "Login Successful.\nProceeding to Voting Interface");
					setVisible(false);
					dispose();
					try {			
						FabricLoginBridge bridge;
						bridge = new FabricLoginBridge();
						 FabricLoginBridge login = new FabricLoginBridge();
						 String query = login.queryAll();
						 String split[] = query.split("}}");
						 Pattern p = Pattern.compile("\"([^\"]*)\"");
						
							    for (int i= 0;i<split.length-1;i++) {
							    	ArrayList<String> checklist = new ArrayList<>();
							    	Matcher m = p.matcher(split[i]);
							    	while (m.find()) {
							    		String tmp = m.group(1);
							    		checklist.add(tmp);
							    	}
							    	String userID = checklist.get(6);
							    	if (user.equals(userID)) {
							    		String voteID = checklist.get(1);
							    		bridge.changevotergroup(new String[] {voteID, "voted"});
							    	}
							    	}
						initVote();
					} catch (CryptoException | InvalidArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
		
					}
				}
				// admin usergroup condition
				if (check.equals("admin")) {
					dispose();
					setVisible(false);
					JOptionPane.showMessageDialog(panel, "Welcome "+user+".\nProceeding to Configuration Panel");
					Admin admin = new Admin();
				}
				// voter who voted group condition
				if (check.equals("voted")) {
					dispose();
					setVisible(false);
					JOptionPane.showMessageDialog(null, "This user has already voted.\nIf you have not voted, please contact the adminstrator.","ERROR" ,JOptionPane.ERROR_MESSAGE);
				}
				// incorrect info condition
				if (check.equals("notmatch"))
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
	private String VerifyAccount(String user, String pw) throws Exception {
	    FabricLoginBridge login = new FabricLoginBridge();
	    String query = login.queryAll();
	    String split[] = query.split("}}");
	    Pattern p = Pattern.compile("\"([^\"]*)\"");

	    for (int i= 0;i<split.length-1;i++) {
	    	ArrayList<String> list = new ArrayList<String>();
	    	Matcher m = p.matcher(split[i]);
	    	while (m.find()) {
	    		String tmp = m.group(1);
	    		list.add(tmp);
	    	}
	    	String group = list.get(4);
	    	String userID = list.get(6);
	    	String passcode = list.get(8);
	    	if (user.equals(userID)) {
	    		if (pw.equals(passcode) && group.equals("voter")) {
	    			return "user";
	    		}
	    		if (pw.equals(passcode)&&group.equals("admin")) {
	    			return "admin";
	    		}
	    		if (pw.equals(passcode)&&group.equals("voted")) {
	    			return "voted";
	    		}
	    	}
	    }
	 
		return "notmatch";
		
	}
	// start a vote class instance
	private void initVote() throws Exception {
		Vote vote = new Vote();
		
	}
	
}
	