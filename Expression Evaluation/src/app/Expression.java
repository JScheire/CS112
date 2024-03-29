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
		int counter = 0;
		while (str.hasMoreTokens()) {
			String temp = str.nextToken();
			int i = expr.indexOf(temp, counter);
			counter = i;
			if (Character.isLetter(expr.charAt(i))
					&& !(vars.contains(new Variable(temp))) && !(arrays.contains((new Array(temp))))) {				
				try {
					if (expr.charAt(i + temp.length()) == '[') {
						Array tempArr = new Array(temp);
						arrays.add(tempArr);
					} else {
						Variable tempVar = new Variable(temp);
						vars.add(tempVar);
					}
				} catch (Exception e) {
					Variable tempVar = new Variable(temp);
					vars.add(tempVar);

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
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) throws IOException {
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
	
	//Replaces all variables with literals/numbers
	

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
			String data;
			Node next;

			public Node(String data, Node next) {

				this.data = data;
				this.next = next;

			}
		}
		class TreeNode {
			String data;
			TreeNode l, r;

			TreeNode(String data) {
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

			public void enqueue(String item) {
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

			public String dequeue() throws NoSuchElementException{
				if(rear == null) {
					throw new NoSuchElementException("queue is empty");
				}
				String data = rear.next.data;
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
			
			public String peek() throws NoSuchElementException{
				if(rear == null) {
					throw new NoSuchElementException("queue is empty");
				}
				return rear.next.data;
			}
				

		}
		
		class eval{
			public float evaluateTree(TreeNode root) {
				if(root == null) {
					return 0;
				}
				if(root.l == null && root.r == null ) {
					return Integer.parseInt(root.data);
				}
				
				float leftSub = evaluateTree(root.l);				
				float rightSub = evaluateTree(root.r);
				
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
		expr = expr.replace(" ","");

		char str[] = expr.toCharArray();
		String token;
		int parencounter = 0;
		int digitCounter = 0;
		Stack<String> operator = new Stack<String>();
		Queue<String> output = new Queue<String>();
		Stack<TreeNode> nodestack = new Stack<TreeNode>();
		for(int i = 0; i < str.length; i++ ) {
			token = Character.toString(str[i]);
			if (token.equals("+") || token.equals("-")) {
				if(operator.isEmpty()){
					operator.push(token);
				}
				else if(operator.peek().equals("-")) {
					output.enqueue(operator.pop());
					operator.push(token);
				}
				else if (operator.peek().equals("*") || operator.peek().equals("/")) {
					output.enqueue(operator.pop());
					if(operator.isEmpty()) {
						operator.push(token);
					}
					else if(operator.peek().equals("-")){
						output.enqueue(operator.pop());
						operator.push(token);
					}
					else {
						operator.push(token);
					}
				} 
				else {
					operator.push(token);
				}

			} else if (token.equals("*")|| token.equals("/")) {
				if(operator.isEmpty()) {
					operator.push(token);
				}
				else if(operator.peek().equals("/") || operator.peek().equals("*")) {
					output.enqueue(operator.pop());
					operator.push(token);
				}
				else {
					operator.push(token);
				}
			} else if (token.equals("(")) {
				operator.push(token);
				parencounter++;
			}

			else if (token.equals(")")) {
				while (!(operator.peek().equals("("))) {
					output.enqueue(operator.pop());
				}
				operator.pop();
			}
			
			
			else if(Character.isLetter(expr.charAt(i))) {
				int lettercounter = 0;
				String var = "";
				while(Character.isLetter(expr.charAt(lettercounter + i))){
					var = var + expr.charAt(lettercounter + i);
					lettercounter++;
					if(lettercounter + i >= expr.length()) {
						break;
					}
				}
				i+=lettercounter - 1;
				if(!(i + 2 > expr.length())){
					if(expr.charAt(i + 1) == '[') {
						int lastbracket = 0;
						int counter = 0;
						for(int p = i; p < expr.length(); p++) {
							if(expr.charAt(p) == '[') {
								counter++;
							}
							if(expr.charAt(p) == ']') {
								counter--;
							}
							if(counter == 0) {
								lastbracket = p;
								parencounter = 0;
								if(i - p != 0) {
									break;
								}
							}
						}
						
						
						String innerexpr = expr.substring(i + 2, lastbracket);
						int innerexprval = (int) evaluate(innerexpr, vars, arrays); 
						for(int k = 0; k < arrays.size(); k++) {
							if(arrays.get(k).name.equals(var)) {
								output.enqueue(Integer.toString(arrays.get(k).values[innerexprval]));
								break;
							}
						}
						i=lastbracket;
					}
					
					
					else {			
						for(int j = 0; j < vars.size(); j++) {
							if(vars.get(j).name.equals(var)) {
								output.enqueue(Integer.toString(vars.get(j).value));
								break;
							}
						}		
					}
				}
				else {
					for(int j = 0; j < vars.size(); j++) {
						if(vars.get(j).name.equals(var)) {
							output.enqueue(Integer.toString(vars.get(j).value));
							break;
						}
					}
				}
						
			}
			
			
			
			
			else {
				try {
					while(digitCounter < expr.length() && Character.isDigit(expr.charAt(digitCounter + i))) {
						digitCounter++;
					}
				}
				catch(Exception e) {
				}
				if(digitCounter == 1) {
					output.enqueue(token);
					parencounter = 0;
				}
				else {
					token = expr.substring(i, i + digitCounter);
					output.enqueue(token);
				}
				i += digitCounter - 1;
				digitCounter = 0;
			}
			
		}
		
		while(!(operator.isEmpty())) {
			output.enqueue(operator.pop());
		}
		
		
		while (!(output.isEmpty())) {
			if (output.peek().equals("+") || output.peek().equals("-") || output.peek().equals("*")
					|| output.peek().equals("/")) {
				TreeNode n1 = nodestack.pop();
				TreeNode n2 = nodestack.pop();
				TreeNode oproot = new TreeNode(output.dequeue());
				oproot.r = n1;
				oproot.l = n2;
				nodestack.push(oproot);
								
			}
			else {
				nodestack.push(new TreeNode(output.dequeue()));
			}
		}
		
		eval resultInstance = new eval();
		float result = resultInstance.evaluateTree(nodestack.pop());
		
		return result;
		
		
	}
}
