package votingBlockchain;



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.commons.codec.digest.DigestUtils;

public class Vote extends JFrame{
	int candidateCount = 0;
	String[] allCandidate = new String[10];
	JRadioButton option1 = new JRadioButton();
	JRadioButton option2 = new JRadioButton();
	JRadioButton option3 = new JRadioButton();
	JRadioButton option4 = new JRadioButton();
	JButton confirmButton = new JButton("Submit");
	JPanel midpanel = new JPanel();
	JPanel bottompanel = new JPanel();
	private String sha256hex[] = null;
	
	public Vote() {
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

	private void setCandidateList() {
		
		candidateCount = getCandidateNum();
		String opt[] = {"MRA", "MRSB", "MissC", "Abstain"};
		// load candidate name from source
		option1.setText("MRA");
		option2.setText("MRSB");
		option3.setText("MissC");
		option4.setText("Abstain");
	}
	private int getCandidateNum() {
		return 3;
	}
	private void submitVote() {
		confirmButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (option1.isSelected()) {
					JOptionPane.showMessageDialog(null, "Selected "+option1.getText()+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote();
					}else if (option2.isSelected()) {
					JOptionPane.showMessageDialog(null, "Selected "+option1.getText()+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote();
					}else if (option3.isSelected()) {
					JOptionPane.showMessageDialog(null, "Selected "+option1.getText()+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote();
					}else if (option4.isSelected()) {
					JOptionPane.showMessageDialog(null, "Selected "+option1.getText()+". Please exit the application.");
					setVisible(false);
					dispose();
					updateVote();
					}else {
						JOptionPane.showMessageDialog(null, "Invalid Choice!","ERROR" ,JOptionPane.ERROR_MESSAGE);
					}
				
				}

			});
	}
	private String getCandidate() {
		String name = null;
		return name;
	}
	private void updateVote() {
		// TODO Auto-generated method stub
		
	}

}