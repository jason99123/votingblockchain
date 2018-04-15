package votingBlockchain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;



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
	JButton closevote = new JButton("Close Voting");
	
	
	public Admin() {
		adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		adminFrame.setLayout(new GridLayout(0,2));
		adminFrame.add(adduser);
		//adminFrame.add(adduserlist);
		adminFrame.add(removeuser);
		adminFrame.add(resetuser);
		adminFrame.add(addCandidate);
		adminFrame.add(removeCandidate);
		adminFrame.add(moduser);
		adminFrame.add(reportresult);
		adminFrame.add(openvote);
		adminFrame.add(closevote);
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
				modUser("mod");
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
		closevote.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				closeDocker();
				}
			});
	}
	private void closeDocker() {
		//ssh to start the script
				JFrame frame = new JFrame("Stop Voting");
				JPanel panel = new JPanel();
				panel.setLayout(null);
				panel.setPreferredSize(new Dimension(300,110));
				frame.add(panel);
				JLabel orderer = new JLabel("User@OrdererIP");
				JLabel peer = new JLabel("User@PeerIP");
				JTextField ordererIP = new JTextField();
				JTextField peerIP = new JTextField();
				JButton button = new JButton("Execute");
				orderer.setBounds(10,10,120,25);
				ordererIP.setBounds(150, 10 ,120 ,25);
				peer.setBounds(10, 40 , 120, 25);
				peerIP.setBounds(150,40,120,25);
				button.setBounds(100,70,100,25);
				panel.add(orderer);
				panel.add(ordererIP);
				panel.add(peer);
				panel.add(peerIP);
				panel.add(button);
				frame.pack();
				frame.setVisible(true);
				
				
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String orderIP = ordererIP.getText();
						String pIP = peerIP.getText();
						if (!orderIP.equals("")) {
							JSch jsch = new JSch();
							String s[] = orderIP.split("@");
							try {
								String cmd = "docker stop $(docker ps -a -q)"; 		
								Session session = jsch.getSession(s[0],s[1],22);
								java.util.Properties config = new java.util.Properties();
								config.put("StrictHostKeyChecking", "no");
								session.setConfig(config);
								session.setPassword("1234");
								session.connect();
								Channel channel = session.openChannel("exec");
								((ChannelExec)channel).setCommand(cmd);
								channel.connect();
								channel.disconnect();
								session.disconnect();
								JOptionPane.showMessageDialog(null, "Orderer docker service stopped");
							} catch (JSchException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						if (!pIP.equals("")) {
							JSch jsch = new JSch();
							String s[] = pIP.split("@");
							try {
								String cmd = "docker stop $(docker ps -a -q)"; 		
								Session session = jsch.getSession(s[0],s[1],22);
								java.util.Properties config = new java.util.Properties();
								config.put("StrictHostKeyChecking", "no");
								session.setConfig(config);
								session.setPassword("1234");
								session.connect();
								Channel channel = session.openChannel("exec");
								((ChannelExec)channel).setCommand(cmd);
								channel.connect();
								channel.disconnect();
								session.disconnect();
								JOptionPane.showMessageDialog(null, "Peer docker service stopped");
							} catch (JSchException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}

					}
				});
			}
	
	private void initDocker() {
		//ssh to start the script
		JFrame frame = new JFrame("start Voting");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,110));
		frame.add(panel);
		JLabel orderer = new JLabel("User@OrdererIP");
		JLabel peer = new JLabel("User@PeerIP");
		JTextField ordererIP = new JTextField();
		JTextField peerIP = new JTextField();
		JButton button = new JButton("Execute");
		orderer.setBounds(10,10,120,25);
		ordererIP.setBounds(150, 10 ,120 ,25);
		peer.setBounds(10, 40 , 120, 25);
		peerIP.setBounds(150,40,120,25);
		button.setBounds(100,70,100,25);
		panel.add(orderer);
		panel.add(ordererIP);
		panel.add(peer);
		panel.add(peerIP);
		panel.add(button);
		frame.pack();
		frame.setVisible(true);
		
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				String orderIP = ordererIP.getText();
				String pIP = peerIP.getText();
				if (!orderIP.equals("")) {
					JSch jsch = new JSch();
					String s[] = orderIP.split("@");
					try {
						String cmd = "docker-compose -f /home/"+s[0]+"/fabric/examples/e2e_cli/docker-compose-orderer.yaml up -d"; 		;
						Session session = jsch.getSession(s[0],s[1],22);
						java.util.Properties config = new java.util.Properties();
						config.put("StrictHostKeyChecking", "no");
						session.setConfig(config);
						session.setPassword("1234");
						session.connect();
						Channel channel = session.openChannel("exec");
						((ChannelExec)channel).setCommand(cmd);
						channel.connect();
						channel.disconnect();
						session.disconnect();
						JOptionPane.showMessageDialog(null, "Orderer docker service started");
					} catch (JSchException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				if (!pIP.equals("")) {
					JSch jsch = new JSch();
					String s[] = pIP.split("@");
					try {
						String cmd = "docker-compose -f /home/"+s[0]+"/fabric/examples/e2e_cli/docker-compose-peer.yaml up -d"; 		;
						Session session = jsch.getSession(s[0],s[1],22);
						java.util.Properties config = new java.util.Properties();
						config.put("StrictHostKeyChecking", "no");
						session.setConfig(config);
						session.setPassword("1234");
						session.connect();
						Channel channel = session.openChannel("exec");
						((ChannelExec)channel).setCommand(cmd);
						channel.connect();
						channel.disconnect();
						session.disconnect();
						JOptionPane.showMessageDialog(null, "Peer docker service started");
					} catch (JSchException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
		});
	}
	private void printResult() {
		JFrame result = new JFrame("Voting Result");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,200));
		JLabel labelA = new JLabel("MRA");
		JLabel labelB = new JLabel("MRSB");
		JLabel labelC = new JLabel("MissC");
		JLabel labelD = new JLabel("Abstain");
		int a = getVoteNum("MRA");
		int b = getVoteNum("MRSB");
		int c = getVoteNum("MissC");
		int d = getVoteNum("Abstain");
		int total = a+b+c+d;
		if (total==0) {
			total = 1;
		}
		labelA.setBounds(10,10,100,25);
		JLabel resultA = new JLabel(a+"   "+Integer.toString(a*100/total)+"%");
		resultA.setBounds(125, 10 ,60,25);
		labelB.setBounds(10,40,100,25);
		JLabel resultB = new JLabel(b+"   "+Integer.toString(b*100/total)+"%");
		resultB.setBounds(125, 40 ,60,25);
		labelC.setBounds(10,70,100,25);
		JLabel resultC = new JLabel(c+"   "+Integer.toString(c*100/total)+"%");
		resultC.setBounds(125, 70 ,60,25);
		labelD.setBounds(10,110,100,25);
		JLabel resultD = new JLabel(d+"   "+Integer.toString(d*100/total)+"%");
		resultD.setBounds(125, 110 ,60,25);		
		result.add(panel);
		panel.add(labelA);
		panel.add(resultA);
		panel.add(labelB);
		panel.add(resultB);
		panel.add(labelC);
		panel.add(resultC);
		panel.add(labelD);
		panel.add(resultD);
	
		result.pack();
		result.setVisible(true);
	}
	//read vote from server
	private int getVoteNum(String name) {
		if (name.equals("MRA"))
			return 10;
		if (name.equals("MRSB"))
			return 2;
		if (name.equals("MissC"))
			return 7;
		if (name.equals("Abstain"))
			return 5;
			
		return 0;
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
				boolean check = checkCanexist();
				//pass username and pw to server
				if (choice.equals("add")) {
					if (check == true) {
						JOptionPane.showMessageDialog(null, canname+" already exist!", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, canname+" added as a candidate");
					}
				}else if (choice.equals("del")) {
					
				}
			}
		});
		
	}
	private boolean checkCanexist() {
		return false;
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
				String username = name.getText();
				String pw;
				boolean check = checkuserexist();
				//pass username and pw to server
				if (choice.equals("add")) {
					if (check==true) {
						JOptionPane.showMessageDialog(null, username+" already exist!", "Error!", JOptionPane.ERROR_MESSAGE);
					}else {
						Random rm = new Random();
						StringBuilder sb = new StringBuilder();
						for (int i = 0 ;i<4;i++) {
							sb.append(rm.nextInt(10));
						}
						JOptionPane.showMessageDialog(null, username+" added. Passcode: "+sb.toString());
					}
				}else if (choice.equals("del")) {
					
				}else if (choice.equals("reset")) {
					
				}else if (choice.equals("mod")) {
					if (check==true) {
						JOptionPane.showMessageDialog(null, username+" group changed.");
					} else {
						JOptionPane.showMessageDialog(null, username + "does not exist", "Error!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
	}
	private boolean checkuserexist() {
		return false;
	}
}
