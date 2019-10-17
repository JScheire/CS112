package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr   The expression
	 * @param vars   The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		StringTokenizer str = new StringTokenizer(expr, delims);
		while (str.hasMoreTokens()) {
			String temp = str.nextToken();
			int i = expr.indexOf(temp);
			if (Character.isLetter(expr.charAt(i))
					&& !(vars.contains(new Variable(temp)) && !(arrays.contains(new Array(temp))))) {
				try {
					if (expr.charAt(i + 1) == '[') {
						Array tempArr = new Array(temp);
						arrays.add(tempArr);
					} else {
						Variable tempVar = new Variable(temp);
						vars.add(tempVar);
					}
				} catch (Exception e) {

				}
			}
		}
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input
	 * @param vars   The variables array list, previously populated by
	 *               makeVariableLists
	 * @param arrays The arrays array list - previously populated by
	 *               makeVariableLists
	 */
	public static void

			loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}
	

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars   The variables array list, with values for all variables in the
	 *               expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		
				
		class Node {
			Character data;
			Node next;

			public Node(Character data, Node next) {

				this.data = data;
				this.next = next;

			}
		}
		class TreeNode {
			Character data;
			TreeNode l, r;

			TreeNode(Character data) {
				this.data = data;
				l = null;
				r = null;
			}
		}

		class Queue<T> {
			private Node rear;
			private int size;

			public Queue() {
				rear = null;
			}

			public void enqueue(Character item) {
				Node newItem = new Node(item, null);
				if (rear == null) {
					newItem.next = newItem;
				} else {
					newItem.next = rear.next;
					rear.next = newItem;
				}
				size++;
				rear = newItem;
			}

			public Character dequeue() throws NoSuchElementException{
				if(rear == null) {
					throw new NoSuchElementException("queue is empty");
				}
				Character data = rear.next.data;
				if (rear == rear.next) {
					rear = null;
				} else {
					rear.next = rear.next.next;
				}
				
				size--;
				return data;
			}
			
			public boolean isEmpty() {
				return size == 0;
			}
			
			public Character peek() throws NoSuchElementException{
				if(rear == null) {
					throw new NoSuchElementException("queue is empty");
				}
				return rear.next.data;
			}
				

		}
		
		class eval{
			public int evaluateTree(TreeNode root) {
				if(root == null) {
					return 0;
				}
				if(root.l == null && root.r == null ) {
					return Integer.parseInt(root.data);
				}
				
				int leftSub = evaluateTree(root.l);				
				int rightSub = evaluateTree(root.r);
				
				if(root.data.equals("+")) {
					return leftSub + rightSub;
				}
				
				else if(root.data.equals("-")) {
					return leftSub - rightSub;
				}
				
				else if(root.data.equals("*")) {
					return leftSub * rightSub;
				}
				else {				
					return leftSub / rightSub;
				}
			}
		}
		
		
		expr = expr.trim();

		char str[] = expr.toCharArray();
		Stack<Character> operator = new Stack<Character>();
		Queue<Character> output = new Queue<Character>();
		Stack<TreeNode> nodestack = new Stack<TreeNode>();
		for(int i = 0; i < str.length; i++ ) {
			char token = str[i];
			if (token == '+' || token == '-') {
				if (operator.peek() == '*' || operator.peek() == '/') {
					Character tempop = operator.pop();
					operator.push(token);
					operator.push(tempop);
				} else {
					operator.push(token);
				}

			} else if (token == '*' || token == '/') {
				operator.push(token);
			} else if (token == '(') {
				operator.push(token);
			}

			else if (token == ')') {
				while (!(operator.peek() == '(')) {
					output.enqueue(operator.pop());
				}
				operator.pop();
			}
			
			else {
				output.enqueue(token);
			}
		}
		
		if(!(operator.isEmpty())) {
			output.enqueue(operator.pop());
		}
		
		
		while (!(output.isEmpty())) {
			if (output.peek() == '+' || output.peek() == '-' || output.peek() == '*'
					|| output.peek() == '/') {
				TreeNode n1 = nodestack.pop();
				TreeNode n2 = nodestack.pop();
				TreeNode oproot = new TreeNode(output.dequeue());
				oproot.l = n1;
				oproot.r = n2;
				nodestack.push(oproot);
								
			}
			else {
				nodestack.push(new TreeNode(output.dequeue()));
			}
		}
		
		eval resultInstance = new eval();
		int result = resultInstance.evaluateTree(nodestack.pop());
		
		return result;
		
		
	}
}
