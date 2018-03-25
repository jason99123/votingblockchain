package votingBlockchain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Vote {

	HashMap<Integer, String> hmap = new HashMap<Integer, String>();
	String candidatepath = "./resources/main/candidate.txt";
	
	public int acceptVote() {
		getCandidate();
		printCandidate();
		int vote;
		vote = getVote();
		return vote;
	}

	public int getVote() {
		Scanner in = new Scanner(System.in);
		String line;
		line = in.nextLine();
		int vote;
		vote=Integer.parseInt(line);
		String name = hmap.get(vote);
		System.out.println("You have chosen "+name);
		return vote;
		
	}

	private void printCandidate() {
		System.out.println("Please choose one candidate");
		Set set = hmap.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			System.out.println("Candidate "+entry.getKey()+" : "+entry.getValue());
		}
		
	}

	private void getCandidate() {
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(candidatepath));
			while ((line = br.readLine())!=null) {
				String[] arr = line.split(",");
				hmap.put(Integer.parseInt(arr[0]), arr[1]);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}