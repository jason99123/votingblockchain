package votingBlockchain;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

public class FabricLoginBridge {
	/* FabricLoginBridge is to provide connections to contact the blockchain framework
	   The main blockchain framework to communicate is to the user list framework
	*/
	// constant ipLoginPeer to determine the peer ip to connect to
	private final static String ipLoginPeer = "172.20.10.5";
/*
	private ChaincodeManager manager;
	private static FabricLoginBridge instance= null;
	public static FabricLoginBridge obtain() throws CryptoException, InvalidArgumentException {
		if (instance == null) {
			synchronized(FabricLoginBridge.class) {
				if (instance == null) {
					instance = new FabricLoginBridge();
				}
			}
		}
		return instance;
	}
	public FabricLoginBridge() throws CryptoException, InvalidArgumentException {
		
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
		chaincode.setChaincodeName(chaincodeName);
		chaincode.setChaincodePath(chaincodepath);
		chaincode.setChaincodeVersion(version);
		chaincode.setInvokeWaitTime(100000);
		chaincode.setDeployWaitTime(120000);
		return chaincode;
	}
	private Peers getpeers() {
		Peers peers = new Peers();
		peers.setorgName("Org2");
		peers.setorgMSPid("Org2MSP");
		peers.setorgDomainName("org2.example.com");
		peers.addPeer("peer0.org2.example.com", "peer0.org2.example.com", "grpc://192.168.1.199:7051", "grpc://192.168.1.199:7053", "http://192.168.1.207:7054");
		return peers;
	}
	private Orderers getorderers() {
		Orderers orderer = new Orderers();
		orderer.setdomainName("example.com");
		orderer.addOrderer("orderer.example.com", "grpc://192.168.1.199");
		return orderer;
	}
	
	*/
	/*
	 * querySingle is to return the data set for the input username
	 * String voter is the user name from input
	 */
	public String querySingle(String voter) throws Exception {
		
		HFCAClient caClient = getHfCaClient("http://"+ipLoginPeer+":7054", null);
		AppUser admin = getAdmin(caClient);
		AppUser appUser = getUser(caClient, admin, "hfuser");
		HFClient client = getHfClient();
		client.setUserContext(admin);
    	Channel channel = getChannel(client);
 //   	queryBlockChain(client);
    	return queryVoter(client, voter);
 //   	createnewVoter(client, channel);
   	

	}
	/*
	 * queryAll is to return the whole chain data set 
	 */
	public String queryAll() throws Exception {
		
		HFCAClient caClient = getHfCaClient("http://"+ipLoginPeer+":7054", null);
		AppUser admin = getAdmin(caClient);	
		AppUser appUser = getUser(caClient, admin, "hfuser");	   
		HFClient client = getHfClient(); 
		client.setUserContext(admin);
    	Channel channel = getChannel(client);
    	String result = queryBlockChain(client); 
    	return result;
    	
	}
	/*
	 * createnewVoter is to add a new user into the system
	 * args[] = {voterID, username, password, usergroup}
	 * default blockchain port 7054
	 * AppUser is to set the admin and user status of the chain connection, not relate to user of the data in blockchain
	 * HFClient is default environment of Fabric
	 */
	public void createnewVoter(String[] args) throws Exception {
		HFCAClient caClient = getHfCaClient("http://"+ipLoginPeer+":7054", null);
		AppUser admin = getAdmin(caClient);
		AppUser appUser = getUser(caClient, admin, "hfuser");
		HFClient client = getHfClient();
		client.setUserContext(admin);
    	Channel channel = getChannel(client);
		BlockEvent.TransactionEvent event = sendTransaction(client,channel,args).get(60, TimeUnit.SECONDS);
	}
	/*
	 * changevotergroup is to change the user group of a particular user
	 * args[] = {username, usergroup}
	 */
	public void changevotergroup(String[] args) throws Exception{
		HFCAClient caClient = getHfCaClient("http://"+ipLoginPeer+":7054", null);
		AppUser admin = getAdmin(caClient);
		AppUser appUser = getUser(caClient, admin, "hfuser");
		HFClient client = getHfClient();
		client.setUserContext(admin);
    	Channel channel = getChannel(client);
    	BlockEvent.TransactionEvent event = changeGroupTransaction(client,channel,args).get(60, TimeUnit.SECONDS);
	}
	/*
	 * function called by changevotergroup 
	 * necessary function by Fabric to call the transaction
	 * connect to the Go chaincode 
	 */
	static CompletableFuture<BlockEvent.TransactionEvent> changeGroupTransaction(HFClient client, Channel channel, String[] args)
            throws InvalidArgumentException, ProposalException {
        TransactionProposalRequest tpr = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();
        tpr.setChaincodeID(cid);
        tpr.setFcn("changeVoterGroup");
        tpr.setArgs(args);
        Collection<ProposalResponse> responses = channel.sendTransactionProposal(tpr);
        return channel.sendTransaction(responses);
    }
	/*
	 * function called by createnewVoter
	 */
    static CompletableFuture<BlockEvent.TransactionEvent> sendTransaction(HFClient client, Channel channel, String[] args)
            throws InvalidArgumentException, ProposalException {
        TransactionProposalRequest tpr = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();
        tpr.setChaincodeID(cid);
        tpr.setFcn("createVoter");
        tpr.setArgs(args);
        Collection<ProposalResponse> responses = channel.sendTransactionProposal(tpr);
        return channel.sendTransaction(responses);
    }
    /*
     * queryVoter is called by query function to connect to the blockchain to carry out querybychaincoderequest
     */
	private String queryVoter(HFClient client, String query) throws InvalidArgumentException, ProposalException {
		Channel channel = client.getChannel("mychannel");
        QueryByChaincodeRequest qpr = client.newQueryProposalRequest();
        ChaincodeID cId = ChaincodeID.newBuilder().setName("fabcar").build();
        qpr.setChaincodeID(cId);
        qpr.setFcn("queryVoter");
        qpr.setArgs(new String[] {query});
        Collection<ProposalResponse> res = channel.queryByChaincode(qpr);
        for (ProposalResponse pres : res) {
            String stringResponse = new String(pres.getChaincodeActionResponsePayload());
            return stringResponse;
        }
        return "";
		
	}
	/*
	 * to get Channel configuration of blockchain 
	 * include Peer,eventhub, orderer and channel
	 * default port peer 7051, eventhub 7053, orderer 7050 
	 */
	static Channel getChannel(HFClient client) throws InvalidArgumentException, TransactionException {
        Peer peer = client.newPeer("peer0.org1.example.com", "grpc://"+ipLoginPeer+":7051");
        EventHub eventHub = client.newEventHub("eventhub01", "grpc://"+ipLoginPeer+":7053");
        Orderer orderer = client.newOrderer("orderer.example.com", "grpc://"+ipLoginPeer+":7050");
        Channel channel = client.newChannel("mychannel");
        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();
        return channel;
    }
   
   static HFClient getHfClient() throws Exception {
       CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
       HFClient client = HFClient.createNewInstance();
       client.setCryptoSuite(cryptoSuite);
       return client;
   }
   /*
    * get the user config of the blockchain framework with organization setting
    */
   static AppUser getUser(HFCAClient caClient, AppUser registrar, String userId) throws Exception {
       AppUser voteUser = tryDeserialize(userId);
       if (voteUser == null) {
           RegistrationRequest rr = new RegistrationRequest(userId, "org1");
           String enrollmentSecret = caClient.register(rr, registrar);
           Enrollment enrollment = caClient.enroll(userId, enrollmentSecret);
           voteUser = new AppUser(userId, "org1", "Org1MSP", enrollment);
           serialize(voteUser);
       }
       return voteUser;
   }

   /*
    * get the admin of the blockchain framework
    */
   static AppUser getAdmin(HFCAClient caClient) throws Exception {
       AppUser admin = tryDeserialize("admin");
       if (admin == null) {
           Enrollment adminEnrollment = caClient.enroll("admin", "adminpw");
           admin = new AppUser("admin", "org1", "Org1MSP", adminEnrollment);
           serialize(admin);
       }
       return admin;
   }

   static void serialize(AppUser voteuser) throws IOException {
       try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(
               Paths.get(voteuser.getName() + ".jso")))) {
           oos.writeObject(voteuser);
       }
   }


   static AppUser tryDeserialize(String name) throws Exception {
       if (Files.exists(Paths.get(name + ".jso"))) {
           return deserialize(name);
       }
       return null;
   }

   static AppUser deserialize(String name) throws Exception {
       try (ObjectInputStream decoder = new ObjectInputStream(
               Files.newInputStream(Paths.get(name + ".jso")))) {
           return (AppUser) decoder.readObject();
       }
   }
   static HFCAClient getHfCaClient(String caUrl, Properties caClientProperties) throws Exception {
       CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
       HFCAClient caClient = HFCAClient.createNewInstance(caUrl, caClientProperties);
       caClient.setCryptoSuite(cryptoSuite);
       return caClient;
   }
   /*
    * called by queryAll
    */
   static String queryBlockChain(HFClient client) throws ProposalException, InvalidArgumentException {
       Channel channel = client.getChannel("mychannel");
       QueryByChaincodeRequest qpr = client.newQueryProposalRequest();
       ChaincodeID cId = ChaincodeID.newBuilder().setName("fabcar").build();
       qpr.setChaincodeID(cId);
       qpr.setFcn("queryAllVoter");
       Collection<ProposalResponse> res = channel.queryByChaincode(qpr);
       for (ProposalResponse pres : res) {
           String stringResponse = new String(pres.getChaincodeActionResponsePayload());
           return stringResponse;
       }
       return "";
   }
	
}
