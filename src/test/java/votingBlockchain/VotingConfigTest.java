package votingBlockchain;

import static org.junit.Assert.*;

import org.junit.Test;

public class VotingConfigTest {

	@Test
	public void testgetpeers() {
		Peers peer = new Peers();
		VotingConfig config = new VotingConfig();
		config.setpeers(peer);
		Peers test = config.getpeers();
		assertEquals(peer, test);
	}
	
	@Test
	public void testgetorderers() {
		Orderers ord = new Orderers();
		VotingConfig config = new VotingConfig();
		config.setorderers(ord);
		Orderers test = config.getOrderers();
		assertEquals(ord, test);
	}

	@Test
	public void testgetchaincode() {
		Chaincode code = new Chaincode();
		VotingConfig config = new VotingConfig();
		config.setchaincode(code);
		Chaincode test = config.getchaincode();
		assertEquals(code, test);
	}

	@Test
	public void testgetchannelPath() {
		VotingConfig config = new VotingConfig();
		config.setchannelPath("Path");
		String test = config.getchannelPath();
		assertEquals("Path", test);
	}

	@Test
	public void testgetcryptoPath() {
		VotingConfig config = new VotingConfig();
		config.setcryptoPath("Path");
		String test = config.getcryptoPath();
		assertEquals("Path", test);
	}

	@Test
	public void testisRegisterEvent() {
		VotingConfig config = new VotingConfig();
		config.setRegisterEvent(true);
		Boolean test = config.isRegisterEvent();
		assertEquals(true, test);
	}

}
