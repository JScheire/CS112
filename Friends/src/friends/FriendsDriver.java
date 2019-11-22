package friends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendsDriver {
	
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(new File("test.txt"));
		
		Graph g = new Graph(sc);
		
		ArrayList<String> shortest = Friends.shortestChain(g, "sam", "aparna");
		
		if(shortest == null) {
			System.out.println("No link");
		}
		else {
			for(String person : shortest) {
				System.out.println(person);
			}
		}
		
	}

}
