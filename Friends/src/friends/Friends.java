package friends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		boolean[] visited = new boolean[g.members.length];
		
		Queue<String> queue = new Queue<String>();
		
		ArrayList<String> result = new ArrayList<String>();
		
		String[] previous = new String[g.members.length];
		
		ArrayList<String> members = new ArrayList<String>();
		
		String shortestname = p2;
		int index = g.map.get(p1);
		visited[index] = true;
		queue.enqueue(p1);
		
		while(!(queue.isEmpty())) {
			
			String next = queue.dequeue();
			index = g.map.get(next);
			if(next.equals(p2)) {
                result.add(p2);
                for(Person p : g.members) {
                    members.add(p.name);
                }
                while(previous[members.indexOf(shortestname)] != null) {
                    result.add(previous[members.indexOf(shortestname)]);
                    shortestname = previous[members.indexOf(shortestname)];
                }
                return result;
            }
			
			for(Friend ptr = g.members[index].first; ptr != null; ptr = ptr.next) {
			    int nextIndex = ptr.fnum;
			    
			    if(!visited[nextIndex]) {
			    	visited[nextIndex] = true;
			    	previous[nextIndex] = next;
			    	queue.enqueue(g.members[nextIndex].name);       
			    }		    
			}
		}
		
		return null;
		
	}
	
	private static void DFS1(Graph g, String school, boolean[] visited, int index, ArrayList<String> clique) {
        visited[index] = true;
        if(g.members[index].student && g.members[index].school.equals(school))
            clique.add(g.members[index].name);
      
        for(Friend ptr = g.members[index].first; ptr != null; ptr = ptr.next) {
            int nextIndex = ptr.fnum;
            
            if(!(visited[nextIndex]) && g.members[nextIndex].student && g.members[nextIndex].school.equals(school)) {
                DFS1(g, school, visited, nextIndex, clique);
            }
        }
        
        
    }
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
        
	    boolean[] visited = new boolean[g.members.length];
	    
	    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
	    
	    for(int i = 0; i < g.members.length; i++) {
	        
	        if(visited[i])
	            continue;
	        
	        ArrayList<String> clique = new ArrayList<String>();
	        
	        DFS1(g, school, visited, i, clique);
	        
	        if(!clique.isEmpty()) {
	            result.add(clique);
	        }
	        
	    }
		if(result.isEmpty()) {
		    return null;
		}
		return result;
		
	}
	
	
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return null;
		
	}
}

