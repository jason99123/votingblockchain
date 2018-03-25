package votingBlockchain;

import java.io.File;

import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

public class FabricBridge {
	// to connect Main to Fabric backbones
	
	private ChaincodeManager manager;
	private static FabricBridge instance= null;
	public static FabricBridge obtain() throws CryptoException, InvalidArgumentException {
		if (instance == null) {
			synchronized(FabricBridge.class) {
				if (instance == null) {
					instance = new FabricBridge();
				}
			}
		}
		return instance;
	}
	private FabricBridge() throws CryptoException, InvalidArgumentException {
		manager = new ChaincodeManager(getConfig());
	}
	public ChaincodeManager getManager() {
		return manager;
	}
	private VotingConfig getConfig() {
		VotingConfig config = new VotingConfig();
		config.setorderers(getorderers());
		config.setpeers(getpeers());
		config.setchaincode(getchaincode("orgname","VotingBlockchain", "github.com/hyperledger/fabric/chaincode/go/release/", "1.0"));
		config.setchannelPath(getchannelArtifactsPath());
		config.setcryptoPath(getcryptoConfigPath());
		return config;
	}
	private String getcryptoConfigPath() {
		String dir = System.getProperty("user.dir");
		//String dir = FabricBridge.class.getClassLoader().getResource("fabric").getFile();
		File file = new File(dir);
		return file.getPath() + "/crypto-config";
	}
	private String getchannelArtifactsPath() {
		String dir = System.getProperty("user.dir");
		//String dir = FabricBridge.class.getClassLoader().getResource("fabric").getFile();
		File file = new File(dir);
		return file.getPath() + "/channel-artifacts/";
	}
	private Chaincode getchaincode(String channelName, String chaincodeName, String chaincodepath, String version) {
		Chaincode chaincode = new Chaincode();
		chaincode.setChannelName(channelName);
		chaincode.setChaincodePath(chaincodeName);
		chaincode.setChaincodePath(chaincodepath);
		chaincode.setChaincodeVersion(version);
		chaincode.setInvokeWaitTime(100000);
		chaincode.setDeployWaitTime(120000);
		return chaincode;
	}
	private Peers getpeers() {
		Peers peers = new Peers();
		peers.setorgName("Org1");
		peers.setorgMSPid("Org1MSP");
		peers.setorgDomainName("org1.example.com");
		peers.addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://192.168.1.207:7051", "grpc://192.168.1.207:7053", "http://192.168.1.207:7054");
		return peers;
	}
	private Orderers getorderers() {
		Orderers orderer = new Orderers();
		orderer.setdomainName("example.com");
		orderer.addOrderer("orderer.example.com", "grpc://192.168.1.148");
		return orderer;
	}
	
}
