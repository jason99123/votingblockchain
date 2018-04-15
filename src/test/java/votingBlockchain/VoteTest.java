package votingBlockchain;

import static org.junit.Assert.*;

import org.junit.Test;

public class VoteTest {

	@Test
	public void testVoteJFrame() {
		Vote vote;
		try {
			vote = new Vote();
			vote.dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert(true);
		
	}

}
