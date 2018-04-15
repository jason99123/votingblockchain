package votingBlockchain;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.Peer;

public class VotingOrg {
	private static Logger log = Logger.getLogger(VotingOrg.class.getName());
	private String orgName;
	private String mspId;
	private HFCAClient ca;
	
	Map<String, User> userMap = new HashMap<>();
	Map<String, String> peerLocation = new HashMap<>();
	Map<String, String> ordererLocation = new HashMap<>();
	Map<String, String> eventLocation = new HashMap<>();
	Set<Peer> peers = new HashSet<>();
	private AppUser admin;
	private String caLocation;
	private Properties caProperties = null;
	private String domain;
	private AppUser peerAdmin;
	public VotingOrg(Peers peers, Orderers orderers, VotingStore store, String path) {
		this.orgName = peers.getorgName();
		this.mspId = peers.getorgMSPid();
		for (int i=0;i<peers.getList().size(); i++) {
			addPeerLocation(peers.getList().get(i).getpeerName(), peers.getList().get(i).getpeerLocation());
			addEventLocation(peers.getList().get(i).getpeerEventName(), peers.getList().get(i).getpeerEventLocation());
			setCALocation(peers.getList().get(i).getCa());
		}
		for (int i=0; i<orderers.getordererList().size(); i++) {
			addOrdererLocation(orderers.getordererList().get(i).getordererName(), orderers.getordererList().get(i).getordererLocation());
		}
		setdomain(peers.getorgDomainName());
//		setAdmin(store.getMember("admin", peers.getorgName()));
		File keyFile = Paths.get(path, "/peerOrganizations/", peers.getorgDomainName(), String.format("/users/Admin@%s/map/keystore", peers.getorgDomainName())).toFile();
		File certFile = Paths.get(path, "/peerOrganizations", peers.getorgName(), String.format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem",  peers.getorgDomainName(), peers.getorgDomainName())).toFile();
		log.info("keyFile path = "+keyFile.getAbsolutePath());
		log.info("CertFile = "+certFile.getAbsolutePath());
//		setpeerAdmin(store.getMember(peers.getorgName()+"Admin", peers.getorgName(), peers.getorgMSPid(), findFileKey(keyFile), certFile));
	}
	
	private File findFileKey(File file) {
		File[] tmp = file.listFiles((dir, name) -> name.endsWith("_sk"));
		if (tmp == null) {
			log.warning("SK file no found");
		} else if(tmp.length !=1) 
		{
			log.warning("More than 1 found");
		} else {
			return tmp[0];
		}
		return null;
	}

	public String getorgName() {
		return this.orgName;
	}
	public AppUser getAdmin() {
		return this.admin;
	}
	public void setAdmin(AppUser _admin) {
		this.admin = _admin;
	}
	public String getmspId() {
		return this.mspId;
	}
	public void setCALocation(String location) {
		this.caLocation = location;
	}
	public String getCALocation() {
		return this.caLocation;
	}
	public void addPeerLocation(String name, String location) {
		peerLocation.put(name,location);
	}
	public void addOrdererLocation(String name, String location) {
		ordererLocation.put(name, location);
	}
	public void addEventLocation(String name, String location) {
		eventLocation.put(name, location);
	}
	public String getpeerLocation(String name) {
		return peerLocation.get(name);
	}
	public String getordererLocation(String name) {
		return ordererLocation.get(name);
	}
	public String geteventLocation(String name) {
		return eventLocation.get(name);
	}
	public Set<Peer> getpeers(){
		return Collections.unmodifiableSet(this.peers);
	}
	public Set<String> getorderername(){
		return Collections.unmodifiableSet(this.ordererLocation.keySet());
	}
	public Collection<String> getordererLocation(){
		return Collections.unmodifiableCollection(this.ordererLocation.values());
	}
	public Set<String> geteventNames(){
		return Collections.unmodifiableSet(this.eventLocation.keySet());
	}
	public Collection<String> geteventLocation(){
		return Collections.unmodifiableCollection(eventLocation.values());
	}
	public void seCAClient(HFCAClient client) {
		this.ca = client;
	}
	public HFCAClient getCAClient() {
		return this.ca;
	}
	public void addUser(AppUser user) {
		userMap.put(user.getName(), user);
	}
	public User getUser(String name) {
		return userMap.get(name);
	}
	public void addPeer(Peer peer) {
		peers.add(peer);
	}
	public void setCAProperties(Properties properties) {
		this.caProperties = properties;
	}
	public Properties getCAProperties() {
		return this.caProperties;
	}
	public void setpeerAdmin(AppUser _admin) {
		this.peerAdmin = _admin;
	}
	public AppUser getpeerAdmin() {
		return this.peerAdmin;
	}
	public String getdomain() {
		return this.domain;
	}
	public void setdomain(String _domain) {
		this.domain = _domain;
	}
	
}
