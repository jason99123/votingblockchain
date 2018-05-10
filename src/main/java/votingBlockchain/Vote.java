package votingBlockchain;



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.commons.codec.digest.DigestUtils;

public class Vote extends JFrame{
	/*
	 * Vote class provide function to vote
	 * mainly relate to candidate list modification
	 */
	int candidateCount = 0;
	String[] allCandidate = new String[10];
	JRadioButton option1 = new JRadioButton();
	JRadioButton option2 = new JRadioButton();
	JRadioButton option3 = new JRadioButton();
	JRadioButton option4 = new JRadioButton();
	JButton confirmButton = new JButton("Submit");
	JPanel midpanel = new JPanel();
	JPanel bottompanel = new JPanel();

	
	public Vote() throws Exception {
		super("Please Choose one of the following candidate");

		setCandidateList();
		setLayout(new BorderLayout());
		ButtonGroup group = new ButtonGroup();
		group.add(option1);
		group.add(option2);
		group.add(option3);
		group.add(option4);
		midpanel.setLayout(new FlowLayout());
		midpanel.add(option1);
		midpanel.add(option2);
		midpanel.add(option3);
		midpanel.add(option4);
		bottompanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottompanel.add(confirmButton);
		add(midpanel, BorderLayout.CENTER);
		add(bottompanel,BorderLayout.SOUTH);
		pack();
		setVisible(true);
		submitVote();
		
	}
/*
 * the current setting only show first three active candidate + abstain choice
 * Add JRadioButton to the jframe for more choice
 */
	private void setCandidateList() throws Exception {
		
		String[] list = getCandidate();
		option1.setText(list[0]);
		option2.setText(list[1]);
		option3.setText(list[2]);
		option4.setText("Abstain");
	}
/*
 * submit choice to the blockchain and close the program
 * 
 */
	private void submitVote() {
		confirmButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (option1.isSelected()) {
					String choice = option1.getText();
					JOptionPane.showMessageDialog(null, "Selected "+choice+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote(choice);
					}else if (option2.isSelected()) {
						String choice = option2.getText();
					JOptionPane.showMessageDialog(null, "Selected "+choice+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote(choice);
					}else if (option3.isSelected()) {
						String choice = option3.getText();
					JOptionPane.showMessageDialog(null, "Selected "+choice+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote(choice);
					}else if (option4.isSelected()) {
						String choice = option4.getText();
					JOptionPane.showMessageDialog(null, "Selected "+choice+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote(choice);
					}else {
						JOptionPane.showMessageDialog(null, "Invalid Choice!","ERROR" ,JOptionPane.ERROR_MESSAGE);
					}
				
				}

			});
	}
	/*
	 * read the candidate chain
	 * only status Active can be considered as a choice
	 */
	public String[] getCandidate() throws Exception {
		FabricVotingBridge vote = new FabricVotingBridge();
	    String query = vote.queryAll();
	    String split[] = query.split("}}");
	    Pattern p = Pattern.compile("\"([^\"]*)\"");
	    String ret[] = new String[3];
	    int count = 0;
	    for (int i= 0;i<split.length-1;i++) {
	    	ArrayList<String> list = new ArrayList<String>();
	    	Matcher m = p.matcher(split[i]);
	    	while (m.find()) {
	    		String tmp = m.group(1);
	    		list.add(tmp);
	    	}
	    	String canName = list.get(6);
	    	String status = list.get(8);
	    	if (status.equals("ACTIVE")&&!canName.equals("Abstain")) {
	    		ret[count] = canName;
	    		count++;
	    	}
	    }
	    return ret;
	}
	/*
	 * function to connect to FabricVotingBridge
	 */
	private void updateVote(String choice) {
		
		try {
			FabricVotingBridge vote = new FabricVotingBridge();
	
		    String query = vote.queryAll();
		    String split[] = query.split("}}");
		    Pattern p = Pattern.compile("\"([^\"]*)\"");

	
		    for (int i= 0;i<split.length-1;i++) {
		    	ArrayList<String> list = new ArrayList<String>();
		    	Matcher m = p.matcher(split[i]);
		    	while (m.find()) {
		    		String tmp = m.group(1);
		    		list.add(tmp);
		    	}
		    	String canName = list.get(6);
		    	if (canName.equals(choice)) {
		    		int votenum = Integer.parseInt(list.get(4));
		    		String canNo = list.get(1);
					vote.createnewCan(new String[]{canNo,canName,Integer.toString(votenum+1), "ACTIVE"});
		    	}
		    }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}