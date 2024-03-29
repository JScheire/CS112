public class IntNode {
	public int data;
	public IntNode next;
	public IntNode(int data, IntNode next) {
		this.data = data; this.next = next;
	}
	public String toString() {
		return data + "";
	}
	
	public static IntNode addBefore(IntNode front, int target, int newItem)
	{
		IntNode prev=null, ptr=front;
        while (ptr != null && ptr.data != target) {
            prev = ptr;
		ptr = ptr.next; 
		}    
        if (ptr == null) { // target not found
            return front;
        }
        IntNode temp = new IntNode(newItem, ptr); // next of new node should point to target
        if (prev == null) { // target is first item, so new node will be new front
           return temp;
        }
        prev.next = temp;
        return front;  // front is unchanged
	}
	
	public static IntNode addBeforeLast(IntNode front, int item) {
		
		IntNode prev = null, ptr = front;
		
		while(ptr.next != null) {
			prev = ptr;
			ptr = ptr.next;
		}
		
		if(front == null) {
			return null;
		}
		
		IntNode temp = new IntNode(item, ptr);
		prev.next = temp;
		
		return front;
	}
	
	
	public static IntNode deleteEveryOther(IntNode front) {
		
		IntNode ptr = front.next; IntNode prev = front;
		boolean tbd = true;
		
		while(ptr != null) {
			if(tbd) {
				ptr = ptr.next;
				prev.next = ptr;
				tbd = false;
			}
			else {
				prev = ptr;
				ptr = ptr.next;
				tbd = true;
				
			}
			
		}
		return front;
		
	}
	
	public static IntNode commonElements(IntNode frontL1, IntNode frontL2) {
		IntNode result = null;
		for(IntNode ptr1 = frontL1; ptr1 != null; ptr1 = ptr1.next) {
			for(IntNode ptr2 = frontL2; ptr2 != null; ptr2 = ptr2.next) {
				if (ptr1.data == ptr2.data) {
					result = new IntNode(ptr1.data, null);
					result = result.next;
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		IntNode front = null; IntNode front2 = null;
		front = new IntNode(4, front);
		front=  new IntNode(3, front);
		front = new IntNode(2, front);
		front = new IntNode(1, front);
		front2 = new IntNode(6, front2);
		front2 =  new IntNode(5, front2);
		front2 = new IntNode(4, front2);
		front2 = new IntNode(1, front2);
		IntNode result = commonElements(front, front2);
		//IntNode result = addBeforeLast(front, 3);
		//IntNode result = deleteEveryOther(front);
		//System.out.println(result.data);
		
		
	}
}
