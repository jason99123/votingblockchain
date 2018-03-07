package votingBlockchain;

import java.io.File;

import org.apache.log4j.Logger;

public class VotingConfig {
	private static Logger log = Logger.getLogger(VotingConfig.class);
	private Peers peers;
	private Orderers orderers;
	private Chaincode chaincode;
	private String channelPath;
	private String cryptoPath;
	private boolean registerEvent = false;
	
	public VotingConfig() {
		this.channelPath = getChannelPath()+ "/channel-artifacts/";
		this.cryptoPath = getChannelPath() + "/crypto-config/";
	}

	private String getChannelPath() {
		String dir = ChaincodeManager.class.getClassLoader().getResource("fabric").getFile();
		File directory = new File(dir);
		log.info("Directory: "+directory.getPath());
		return directory.getPath();
	}
	public Peers getpeers() {
		return peers;
	}
	public void setpeers(Peers p) {
		this.peers = p;
	}
	public void getorderers(Orderers order) {
		this.orderers = order;
	}
	public Orderers getOrderers() {
		return orderers;
	}
	public Chaincode getchaincode() {
		return chaincode;
	}
	public void setchaincode(Chaincode code) {
		this.chaincode = code;
	}
	public String getchannelPath() {
		return channelPath;
	}
	public void setchannelPath(String path) {
		this.channelPath = path;
	}
	public void setcryptoPath(String path) {
		this.cryptoPath = path;
	}
	public String getcryptoPath(){
		return cryptoPath;
	}
	public boolean isRegisterEvent() {
		return registerEvent;
	}
	public void setRegisterEvent(boolean check) {
		this.registerEvent = check;
	}
}
