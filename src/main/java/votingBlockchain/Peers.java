package votingBlockchain;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.sdk.Peer;

public class Peers {
	private String orgName;
	private String orgMSPid;
	private String orgDomainName;
	private List<Peer> peers;
	
	public Peers() {
		peers = new ArrayList<>();
	}
	public String getorgName() {
		return orgName;
	}
	public void setorgName(String name) {
		this.orgName = name;
	}
	public String getorgMSPid() {
		return orgMSPid;
	}
	public void setorgMSPid(String id) {
		this.orgMSPid = id;
	}
	public String getorgDomainName() {
		return orgDomainName;
	}
	public void setorgDomainName(String name) {
		this.orgDomainName = name;
	}
	public void addPeer(String peerName, String peerEventName, String peerLocation, String peerEventLocation, String ca) {
		peers.add(new Peer(peerName, peerEventName, peerLocation, peerEventLocation, ca));
	}
	public List<Peer> getList(){
		return peers;
	}
	
	public class Peer{
		public Peer(String peerName2, String peerEventName2, String peerLocation2, String peerEventLocation2,
				String ca2) {
			this.peerName = peerName2;
			this.peerEventName = peerEventName2;
			this.peerLocation = peerLocation2;
			this.peerEventLocation = peerEventLocation2;
			this.ca = ca2;
		}
		private String peerName;
		private String peerEventName;
		private String peerLocation;
		private String peerEventLocation;
		private String ca;
		private boolean eventHub = false;
		
		public String getpeerName() {
			return this.peerName;
		}
		public void setpeerName(String name) {
			this.peerName = name;
		}
		public void setpeerEventName(String name) {
			this.peerEventName = name;
		}
		public String getpeerEventName() {
			return this.peerEventName;
		}
		public String getpeerLocation() {
			return this.peerLocation;
		}
		public void setpeerLocation(String location) {
			this.peerLocation = location;
		}
		public String getpeerEventLocation() {
			return this.peerEventLocation;
		}
		public void setpeerEventLocation(String location) {
			this.peerEventLocation = location;
		}
		public String getCa() {
			return this.ca;
		}
		public void setCa(String _ca) {
			this.ca = _ca;
		}
		public boolean isEventHub() {
			return this.eventHub;
		}
		public void setEventHub(boolean check) {
			this.eventHub = check;
		}
		
	}
}
