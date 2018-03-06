package votingBlockchain;

import java.util.ArrayList;
import java.util.List;


public class Orderers {
	private String domainName;
	private List<Orderer> orderer;
	
	public Orderers() {
		orderer = new ArrayList<>();
	}
	
	public String getdomainName() {
		return domainName;
	}
	
	public void setdomainName(String _domainName) {
		this.domainName = _domainName;
	}
	
	// to add orderer to the list
	public void addOrderer(String name, String location) {
		orderer.add(new Orderer(name, location));
	}
	
	public List<Orderer> getordererList() {
		return orderer;
	}
	

	public class Orderer {
		private String ordererName;
		private String ordererLocation;
	
		public Orderer(String name, String location) {
			super();
			this.ordererName = name;
			this.ordererLocation = location;
		}
		
		public String getordererName() {
			return this.ordererName;
		}
		
		public void setordererName(String name) {
			this.ordererName = name;
		}
		
		public String getordererLocation() {
			return this.ordererLocation;
		}
		
		public void setordererLocation(String location) {
			this.ordererLocation = location;
		}
}
}
