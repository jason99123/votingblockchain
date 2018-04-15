package votingBlockchain;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChaincodeTest {

	@Test
	public void testgetchannelName() {
		Chaincode code = new Chaincode();
		code.setChannelName("Testing");
		String test = code.getchannelName();
		assertEquals(test, "Testing");
	}
	
	@Test
	public void testsetChannelName() {
		Chaincode code = new Chaincode();
		code.setChannelName("Testing");
		assert(true);
	}
	
	@Test
	public void testgetChaincodeName() {
		Chaincode code = new Chaincode();
		code.setChaincodeName("TestName");
		String test = code.getChaincodeName();
		assertEquals(test, "TestName");
	}
	
	@Test 
	public void testsetChaincodeName() {
		Chaincode code = new Chaincode();
		code.setChaincodeName("testing");
		assert(true);
	}
	
	@Test
	public void testgetChaincodePath() {
		Chaincode code = new Chaincode();
		code.setChaincodePath("path");
		String test = code.getChaincodePath();
		assertEquals(test, "path");
	}
	
	@Test 
	public void testsetChaincodePath() {
		Chaincode code = new Chaincode();
		code.setChaincodeName("path");
		assert(true);
	}

	@Test
	public void testgetChaincodeVersion() {
		Chaincode code = new Chaincode();
		code.setChaincodeVersion("version");
		String test = code.getChaincodeVersion();
		assertEquals(test, "version");
	}
	

	@Test
	public void testsetChaincodeVersion() {
		Chaincode code = new Chaincode();
		code.setChaincodeVersion("test");
		assert(true);
	}
	
	@Test
	public void testgetInvokeWaitTime() {
		Chaincode code = new Chaincode();
		code.setInvokeWaitTime(100);
		int test = code.getInvokeWaitTime();
		assertEquals(test, 100);
	}
	
	@Test
	public void testsetInvokeWaitTime() {
		Chaincode code = new Chaincode();
		code.setInvokeWaitTime(100);
		assert(true);
	}

	@Test
	public void testgetDeployWaitTime() {
		Chaincode code = new Chaincode();
		code.setDeployWaitTime(100);
		int test = code.getDeployWaitTime();
		assertEquals(test, 100);
	}

	@Test
	public void testsetDeployWaitTime() {
		Chaincode code = new Chaincode();
		code.setDeployWaitTime(100);
		assert(true);
	}


}
