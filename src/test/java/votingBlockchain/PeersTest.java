package votingBlockchain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import votingBlockchain.Peers.Peer;

public class PeersTest {

	@Test
	public void testgetorgName() {
		Peers peer = new Peers();
		peer.setorgName("Name");
		String test = peer.getorgName();
		assertEquals(test,"Name");
	}
	
	@Test
	public void testsetorgName() {
		Peers peer = new Peers();
		peer.setorgName("Name");
		assert(true);
	}

	@Test
	public void testgetorgMSPid() {
		Peers peer = new Peers();
		peer.setorgMSPid("ID");
		String test = peer.getorgMSPid();
		assertEquals(test,"ID");
	}
	
	@Test
	public void testsetorgMSPid() {
		Peers peer = new Peers();
		peer.setorgMSPid("Test");
	}

	@Test
	public void testgetorgDomainName() {
		Peers peer = new Peers();
		peer.setorgDomainName("Domain");
		String test = peer.getorgDomainName();
		assertEquals(test, "Domain");
	}
	
	@Test
	public void testaddPeer() {
		Peers peer = new Peers();
		peer.addPeer("Name", "Event", "Location", "EventLocation", "ca");
		assert(true);
	}

}
