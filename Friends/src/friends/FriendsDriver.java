package friends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendsDriver {
	
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(new File("test.txt"));
		Scanner sc2 = new Scanner(System.in);
		
		Graph g = new Graph(sc);
		
			System.out.print("Enter the first friend: ");
			String friend1 = sc2.nextLine();
			System.out.print("Enter the second friend: ");
			String friend2 = sc2.nextLine();
			ArrayList<String> shortest = Friends.shortestChain(g, friend1, friend2);
			
			if(shortest == null) {
				System.out.println("No link");
			}
			else {
				for(String person : shortest) {
					System.out.println(person);
				}
			}
			
			
			
			System.out.print("Enter a school name: ");
			String school = sc2.nextLine();
			ArrayList<ArrayList<String>> cliques = Friends.cliques(g, school);
	        if(cliques != null) {
	            for(ArrayList<String> clique : cliques) {
	                System.out.println(clique.toString());
	            }
	        }
	        
	        
	        
			System.out.println("Connectors: ");
			ArrayList<String> connectors = Friends.connectors(g);
			for(String connector : connectors) {
				System.out.println(connector);
			}
		sc.close();
		
	}

}
