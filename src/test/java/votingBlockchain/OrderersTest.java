package votingBlockchain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import votingBlockchain.Orderers.Orderer;

public class OrderersTest {

	@Test
	public void testgetdomainName() {
		Orderers ord = new Orderers();
		ord.setdomainName("Name");
		String test = ord.getdomainName();
		assertEquals(test, "Name");
	}
	
	@Test
	public void testsetdomainName() {
		Orderers ord = new Orderers();
		ord.setdomainName("test");
		assert(true);
	}

	@Test
	public void testaddOrderer() {
		Orderers ord = new Orderers();
		ord.addOrderer("Name", "Location");
		assert(true);
	}


}
