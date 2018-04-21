package votingBlockchain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;



public class Admin extends JFrame{
	
	JFrame adminFrame = new JFrame("Admin Operation");
	JButton adduser = new JButton("Add User");
	JButton adduserlist = new JButton("Add User from File");
	JButton removeuser = new JButton("Disable VOTERID");
	JButton resetuser = new JButton("Reset VOTERID");
	JButton addCandidate = new JButton("Add Candidate");
	JButton removeCandidate = new JButton("Disable CandidateID");
	JButton moduser = new JButton("Mod User");
	JButton reportresult = new JButton("Show Result");
	JButton openvote = new JButton("Enable Voting");
	JButton printallUser= new JButton("All User to File");
	JButton printallCandidate= new JButton("All Candidate to File");
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
//		adminFrame.add(openvote);
//		adminFrame.add(closevote);
		adminFrame.add(printallUser);
		adminFrame.add(printallCandidate);
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
		printallUser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				try {
					printallQuery();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			});
		printallCandidate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				try {
					printallCanQuery();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	private void printallQuery() throws Exception {
		  PrintWriter writer = new PrintWriter("allUser.txt","UTF-8");
		  FabricLoginBridge brigde = new FabricLoginBridge();
		  String all = brigde.queryAll();
		  writer.print(all);
		  writer.close();
	}
	private void printallCanQuery() throws Exception{
	       FabricVotingBridge bridge = new FabricVotingBridge();
	       PrintWriter writer = new PrintWriter("allCandidate.txt","UTF-8");
	       String all = bridge.queryAll();
	       writer.print(all);
	       writer.close();
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
	private void printResult() {
		JFrame result = new JFrame("Voting Result");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,200));
		try {
			String[] list = getCandidate();
		
		JLabel labelA = new JLabel(list[0]);
		JLabel labelB = new JLabel(list[1]);
		JLabel labelC = new JLabel(list[2]);
		JLabel labelD = new JLabel("Abstain");
		int a = getVoteNum(list[0]);
		int b = getVoteNum(list[1]);
		int c = getVoteNum(list[2]);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//read vote from server
	private int getVoteNum(String name) {
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
		    	if (canName.equals(name)) {
		    		int votenum = Integer.parseInt(list.get(4));
		    		return votenum;
		    	}
		}
		    } catch (CryptoException | InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private void modCan(String choice) {
		String labeltext;
		if (choice.equals("add")) {
			labeltext = "Enter Candidate Name";
		}else {
			labeltext = "Enter Candidate ID";
		}
		JFrame user = new JFrame("Modify Candidate");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,80));
		user.add(panel);
		JTextField name = new JTextField();
		JLabel instruction = new JLabel(labeltext);
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
				boolean check=false;
				try {
					check = checkCanexist(canname);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//pass username and pw to server
				if (choice.equals("add")) {
					if (check == true) {
						JOptionPane.showMessageDialog(null, canname+" already exist!", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						FabricVotingBridge vote;
						try {
							vote = new FabricVotingBridge();
							boolean empty = false;
							int i = 0;
							while(empty==false) {						 
								String get = vote.querySingle("CAN"+Integer.toString(i));
								if (get.equals("")) {
									empty = true;
									break;
								}
								i++;
							}
					
							vote.createnewCan(new String[]{"CAN"+Integer.toString(i),canname,"0", "ACTIVE"});
							 JOptionPane.showMessageDialog(null, canname+" added as "+"CAN"+Integer.toString(i));
						} catch (CryptoException | InvalidArgumentException e) {
							JOptionPane.showMessageDialog(null, "Error occurred!", "Error!", JOptionPane.ERROR_MESSAGE);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error occurred!", "Error!", JOptionPane.ERROR_MESSAGE);
						}
						JOptionPane.showMessageDialog(null, canname+" added as a candidate");
					}
				}else if (choice.equals("del")) {
					try {
						FabricVotingBridge vote = new FabricVotingBridge();
						vote.changeCanStatus(new String[] {canname,"DQ"});
					} catch (CryptoException | InvalidArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
	}
	private boolean checkCanexist(String canname) throws Exception {
		 FabricVotingBridge vote = new FabricVotingBridge();
		 String query = vote.queryAll();
		 String split[] = query.split("}}");
		 Pattern p = Pattern.compile("\"([^\"]*)\"");
		
			    for (int i= 0;i<split.length-1;i++) {
			    	ArrayList<String> checklist = new ArrayList<>();
			    	Matcher m = p.matcher(split[i]);
			    	while (m.find()) {
			    		String tmp = m.group(1);
			    		checklist.add(tmp);
			    	}
			    	String canID = checklist.get(6);
			    	if (canname.equals(canID)) {
			    		return true;
			    	}
			    }
		return false;
	}
	private void exitAction() {
		adminFrame.dispose();
		adminFrame.setVisible(false);
	}
	private void modUser(String choice) {
		String labeltext;
		if (choice.equals("add")){
			labeltext = "Enter User ID";
		}else {
			labeltext = "Enter Voter ID";
		}
		JFrame user = new JFrame("Modify User");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300,80));
		user.add(panel);
		JTextField name = new JTextField();
		JLabel instruction = new JLabel(labeltext);
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
				boolean check=false;
				try {
					check = checkuserexist(username);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
						FabricLoginBridge login;
						try {
							login = new FabricLoginBridge();
							boolean empty = false;
							int i = 0;
							while(empty==false) {						 
								String get = login.querySingle("VOTER"+Integer.toString(i));
								if (get.equals("")) {
									empty = true;
									break;
								}
								i++;
							}
					
							login.createnewVoter(new String[]{"VOTER"+Integer.toString(i), username, "1234", "voter"});
							 JOptionPane.showMessageDialog(null, username+" added as "+"VOTER"+Integer.toString(i)+". Passcode: "+"1234");
						} catch (CryptoException | InvalidArgumentException e) {
							JOptionPane.showMessageDialog(null, "Error occurred!", "Error!", JOptionPane.ERROR_MESSAGE);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error occurred!", "Error!", JOptionPane.ERROR_MESSAGE);
						}

						
					}
				}else if (choice.equals("del")) {
					FabricLoginBridge bridge;
					try {
						bridge = new FabricLoginBridge();
						bridge.changevotergroup(new String[] {username, "notmatch"});
					} catch (CryptoException | InvalidArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					}else if (choice.equals("reset")) {
						FabricLoginBridge bridge;
						try {
							bridge = new FabricLoginBridge();
							bridge.changevotergroup(new String[] {username, "voter"});
						} catch (CryptoException | InvalidArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if (choice.equals("mod")) {
						FabricLoginBridge bridge;
						try {
							bridge = new FabricLoginBridge();
							bridge.changevotergroup(new String[] {username, "admin"});
						} catch (CryptoException | InvalidArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
			}
		});
		
		
	}
	private boolean checkuserexist(String user) throws Exception {
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
			    		return true;
			    	}
			    }
		return false;
	}
}
