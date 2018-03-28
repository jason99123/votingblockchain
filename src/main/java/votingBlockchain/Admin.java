package votingBlockchain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;




public class Admin extends JFrame{
	
	JFrame adminFrame = new JFrame("Admin Operation");
	JButton adduser = new JButton("Add User");
	JButton adduserlist = new JButton("Add User from File");
	JButton removeuser = new JButton("Remove User");
	JButton resetuser = new JButton("Reset User");
	JButton addCandidate = new JButton("Add Candidate");
	JButton removeCandidate = new JButton("Remove Candidate");
	JButton moduser = new JButton("Mod User");
	JButton reportresult = new JButton("Show Result");
	JButton openvote = new JButton("Enable Voting");
	JButton exit = new JButton("Exit");
	
	
	public Admin() {
		adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		adminFrame.setLayout(new GridLayout(0,2));
		adminFrame.add(adduser);
		adminFrame.add(adduserlist);
		adminFrame.add(removeuser);
		adminFrame.add(resetuser);
		adminFrame.add(addCandidate);
		adminFrame.add(removeCandidate);
		adminFrame.add(moduser);
		adminFrame.add(reportresult);
		adminFrame.add(openvote);
		adminFrame.add(exit);
		adminFrame.pack();
		adminFrame.setVisible(true);
		buttonAction();
	}


	private void buttonAction() {
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				exitAction();
				}
			});
		adduser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modUser("add");
				}
			});
		adduserlist.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modUser("add");
				}
			});
		removeuser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modUser("del");
				}
			});
		resetuser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modUser("reset");
				}
			});
		addCandidate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modCan("add");
				}
			});
		removeCandidate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modCan("del");
				}
			});
		moduser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				modCan("mod");
				}
			});
		reportresult.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				printResult();
				}
			});
		openvote.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				initDocker();
				}
			});
		
	}
	private void initDocker() {
		
	}
	private void printResult() {
		JFrame result = new JFrame("Voting Result");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(300,150);
		
		result.add(panel);
		result.pack();
		result.setVisible(true);
	}
	private void modCan(String choice) {
		JFrame user = new JFrame("Modify Candidate");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,80));
		user.add(panel);
		JTextField name = new JTextField();
		JLabel instruction = new JLabel("Enter Candidate Name");
		JButton submit = new JButton("Submit");
		instruction.setBounds(10, 10 , 160 ,25);
		panel.add(instruction);
		
		name.setBounds(180,10,120,25);
		panel.add(name);
		
		submit.setBounds(95,40,110,25);
		panel.add(submit);
		
		user.pack();
		user.setVisible(true);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String canname;
				canname = name.getText();
				String pw;
				//pass username and pw to server
				if (choice.equals("add")) {
					
				}else if (choice.equals("del")) {
					
				}
			}
		});
		
	}
	
	private void exitAction() {
		adminFrame.dispose();
		adminFrame.setVisible(false);
	}
	private void modUser(String choice) {
		JFrame user = new JFrame("Modify User");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,80));
		user.add(panel);
		JTextField name = new JTextField();
		JLabel instruction = new JLabel("Enter User ID");
		JButton submit = new JButton("Submit");
		instruction.setBounds(10, 10 , 120 ,25);
		panel.add(instruction);
		
		name.setBounds(140,10,160,25);
		panel.add(name);
		
		submit.setBounds(95,40,110,25);
		panel.add(submit);
		
		user.pack();
		user.setVisible(true);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String username;
				username = name.getText();
				String pw;
				//pass username and pw to server
				if (choice.equals("add")) {
					
				}else if (choice.equals("del")) {
					
				}else if (choice.equals("reset")) {
					
				}else if (choice.equals("mod")) {
					
				}
			}
		});
		
	}
}
