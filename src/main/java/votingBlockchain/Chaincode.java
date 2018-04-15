package votingBlockchain;

public class Chaincode {
	
	
	private String channelName;
	private String chaincodeName;
	private String chaincodePath;
	private String chaincodeVersion;
	// how long function invoke last
	private int invokeWaitTime = 100000;
	// how long function deploy last
	private int deployWaitTime = 100000;
	
	public String getchannelName() {
		return this.channelName;
	}
	
	public void setChannelName(String _channelName) {
		this.channelName = _channelName;
	}
	
	public String getChaincodeName() {
		return this.chaincodeName;
	}
	public void setChaincodeName(String name) {
		this.chaincodeName = name;
	}
	
	public String getChaincodePath() {
		return this.chaincodePath;
	}
	
	public void setChaincodePath(String _chaincodePath) {
		this.chaincodePath = _chaincodePath;
	}

	public String getChaincodeVersion() {
		return this.chaincodeVersion;
	}

	public void setChaincodeVersion(String _chaincodeVersion) {
		this.chaincodeVersion = _chaincodeVersion;
	}

	public int getInvokeWaitTime() {
		return this.invokeWaitTime;
	}

	public void setInvokeWaitTime(int _invokeWaitTime) {
		this.invokeWaitTime = _invokeWaitTime;
	}

	public int getDeployWaitTime() {
		return this.deployWaitTime;
	}

	public void setDeployWaitTime(int _deployWaitTime) {
		this.deployWaitTime = _deployWaitTime;
	}

}
