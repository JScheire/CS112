
public class StringNode {
        public String data;
        public StringNode next;
        public StringNode(String data, StringNode next) {
            this.data = data; this.next = next;
        }
        public String toString() {
            return data;
        }
        
        
        public static int numberOfOccurences(StringNode front, String target) {
        	
        	StringNode ptr = front;
        	int counter = 0;
        	
        	while(ptr != null) {
        		if(ptr.data.equals(target)) {
        			counter++;
        		}
        		ptr = ptr.next;
        	}
        	
        	return counter;
        }
        
        public static void main(String[] args) {
        	StringNode front = null;
        	front = new StringNode("hello", front);
        	front = new StringNode("world", front);
        	front = new StringNode("my", front);
        	front = new StringNode("name", front);
        	front = new StringNode("is", front);
        	front = new StringNode("name", front);
        	int result = numberOfOccurences(front, "name");
        	
        	System.out.println(result);
        }

}
