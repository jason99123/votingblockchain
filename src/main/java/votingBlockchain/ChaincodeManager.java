package votingBlockchain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.BlockListener;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class ChaincodeManager {
	private static Logger log = Logger.getLogger(ChaincodeManager.class.getName());
	private VotingConfig config;
	private Orderers orderers;
	private Peers peers;
	private Chaincode chaincode;
	private HFClient client;
	private VotingOrg org;
	private Channel channel;
	private ChaincodeID chaincodeID;
	private final int maxInboundMessageSize = 1000000;
	
	public ChaincodeManager(VotingConfig _config) throws CryptoException, InvalidArgumentException {
		this.config = _config;
		orderers = this.config.getOrderers();
		peers = this.config.getpeers();
		chaincode =this.config.getchaincode();
		client = HFClient.createNewInstance();
		log.info("Creating HFClient instance");
		client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
		log.info("Setting up CryptoSuite");
		org = getVotingOrg();
		channel = getChannel();
		chaincodeID = getChaincodeID();
		client.setUserContext(org.getpeerAdmin());
	}

	private ChaincodeID getChaincodeID() {
		return ChaincodeID.newBuilder().setName(chaincode.getChaincodeName()).setVersion(chaincode.getChaincodeVersion()).setPath(chaincode.getChaincodePath()).build();
	}

	private Channel getChannel() throws InvalidArgumentException {
		client.setUserContext(org.getpeerAdmin());
		return getChannel(org,client);
	}

	private Channel getChannel(VotingOrg org2, HFClient client2) throws InvalidArgumentException {
		Channel channel = client.newChannel(chaincode.getchannelName());
		log.info("Setting up Channel");
		for (int i = 0;i<peers.getList().size();i++) {
			File cert = Paths.get(config.getcryptoPath(), "/peerOrganizations", peers.getorgDomainName(), "peers", peers.getList().get(i).getpeerName(), "tls/server.crt").toFile();
			
		
			Properties peerProperties = new Properties();
			peerProperties.setProperty("pemFile", cert.getAbsolutePath());
			peerProperties.setProperty("hostnameOverride", peers.getorgDomainName());
			peerProperties.setProperty("sslProvider", "openSSL");
			peerProperties.setProperty("negotiationType", "TLS");
			peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", maxInboundMessageSize);
			channel.addPeer(client.newPeer(peers.getList().get(i).getpeerName(), org.getpeerLocation(peers.getList().get(i).getpeerName()), peerProperties));
			if (peers.getList().get(i).isEventHub()) {
				channel.addEventHub(client.newEventHub(peers.getList().get(i).getpeerEventName(), org.geteventLocation(peers.getList().get(i).getpeerEventName()), peerProperties));
				
			}
		}
		for (int i = 0;i<orderers.getordererList().size();i++) {
			File cert = Paths.get(config.getcryptoPath(), "/ordererOrganizations", orderers.getdomainName(), "orderers", orderers.getordererList().get(i).getordererName(),"tls/server.cert").toFile();
			Properties ordererProperties = new Properties();
			ordererProperties.setProperty("pemFile", cert.getAbsolutePath());
			ordererProperties.setProperty("hostnameOverride", orderers.getdomainName());
			ordererProperties.setProperty("sslProvider", "openSSL");
			ordererProperties.setProperty("negotiationType", "TLS");
			ordererProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", maxInboundMessageSize);
			ordererProperties.setProperty("ordererWaitTimeMilliSecs", "300000");
			channel.addOrderer(client.newOrderer(orderers.getordererList().get(i).getordererName(), org.getordererLocation(orderers.getordererList().get(i).getordererName()), ordererProperties));
		}
		log.info("Initializing Channel");
		if (!channel.isInitialized()) {
			try {
				channel.initialize();
			} catch (TransactionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (config.isRegisterEvent()) {
			channel.registerBlockListener(new BlockListener() {
				@Override
				public void received(BlockEvent event) {
					log.info("Event Listener Start");
                    try {
						log.info("event.getChannelId() = " + event.getChannelId());
					} catch (InvalidProtocolBufferException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    log.info("event.getEvent().getChaincodeEvent().getPayload().toStringUtf8() = " + event.getEvent().getChaincodeEvent().getPayload().toStringUtf8());
                    log.info("event.getBlock().getData().getDataList().size() = " + event.getBlock().getData().getDataList().size());
                    ByteString byteString = event.getBlock().getData().getData(0);
                    String result = byteString.toStringUtf8();
                    log.info("byteString.toStringUtf8() = " + result);
                    String r1[] = result.split("END CERTIFICATE");
                    String rr = r1[2];
                    log.info("rr = " + rr);
                    log.info("Event Listener End");
				}
			});
		}
		return channel;
	}

	private VotingOrg getVotingOrg() {
		File storeFile = new File(System.getProperty("user.dir")+"/HFCSampletest.properties");
		VotingStore store = new VotingStore(storeFile);
		VotingOrg votingorg = new VotingOrg(peers, orderers, store, config.getcryptoPath());
		log.info("Getting Organization");
		return votingorg;
	}
	
    public Map<String, String> invoke(String fcn, String[] args)
            throws InvalidArgumentException, ProposalException, InterruptedException, ExecutionException, TimeoutException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, CryptoException, TransactionException, IOException {
        Map<String, String> resultMap = new HashMap<>();

        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();

        /// Send transaction proposal to all peers
        TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(args);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        transactionProposalRequest.setTransientMap(tm2);

        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(transactionPropResp);
        if (proposalConsistencySets.size() != 1) {
            log.warning("Expected only one set of consistent proposal responses but got " + proposalConsistencySets.size());
        }

        if (failed.size() > 0) {
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            log.warning("Not enough endorsers for inspect:" + failed.size() + " endorser error: " + firstTransactionProposalResponse.getMessage() + ". Was verified: "
                    + firstTransactionProposalResponse.isVerified());
            resultMap.put("code", "error");
            resultMap.put("data", firstTransactionProposalResponse.getMessage());
            return resultMap;
        } else {
            log.info("Successfully received transaction proposal responses.");
            ProposalResponse resp = transactionPropResp.iterator().next();
            byte[] x = resp.getChaincodeActionResponsePayload();
            String resultAsString = null;
            if (x != null) {
                resultAsString = new String(x, "UTF-8");
            }
            log.info("resultAsString = " + resultAsString);
            channel.sendTransaction(successful);
            resultMap.put("code", "success");
            resultMap.put("data", resultAsString);
            return resultMap;
        }
    }	
    public Map<String, String> query(String fcn, String[] args) throws InvalidArgumentException, ProposalException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, CryptoException, TransactionException, IOException {
        Map<String, String> resultMap = new HashMap<>();
        String payload = "";
        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(chaincodeID);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);

        Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
        for (ProposalResponse proposalResponse : queryProposals) {
            if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                log.info("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() + ". Messages: "
                        + proposalResponse.getMessage() + ". Was verified : " + proposalResponse.isVerified());
                resultMap.put("code", "error");
                resultMap.put("data", "Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() + ". Messages: "
                        + proposalResponse.getMessage() + ". Was verified : " + proposalResponse.isVerified());
            } else {
                payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                log.info("Query payload from peer: " + proposalResponse.getPeer().getName());
                log.info("" + payload);
                resultMap.put("code", "success");
                resultMap.put("data", payload);
            }
        }
        return resultMap;
    }

}
