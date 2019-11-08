package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		TrieNode root = new TrieNode(null, null, null);
		Indexes index = new Indexes(0, (short) 0, (short) (allWords[0].length() - 1));
		root.firstChild = new TrieNode(index, null, null);
		for (int i = 1; i < allWords.length; i++) {

			TrieNode ptr = root.firstChild;
			TrieNode prev = root;
			String currentWord = allWords[i];
			short startIndex = 0;
			short endIndex = (short) ((short) (currentWord.length()) - 1);
			index = new Indexes(i, startIndex, endIndex);

			while (ptr != null) {
				while (allWords[ptr.substr.wordIndex].charAt(startIndex) == currentWord.charAt(startIndex)
						&& startIndex - 1 < ptr.substr.endIndex) {
					startIndex++;
				}
				if (startIndex == 0 || startIndex == ptr.substr.startIndex) {
					prev = ptr;
					ptr = ptr.sibling;
				} else {
					if (startIndex - 1 == ptr.substr.endIndex) {
						prev = ptr;
						ptr = ptr.firstChild;
					} else {
						prev = ptr;
						break;
					}
				}
			}

			if (startIndex == 0 || startIndex == prev.substr.startIndex) {
				index.startIndex = startIndex;
				prev.sibling = new TrieNode(index, null, null);
			}

			else {
				Indexes firstIndex = new Indexes(prev.substr.wordIndex, startIndex, prev.substr.endIndex);
				prev.substr.endIndex = (short) (startIndex - 1);
				Indexes nextIndex = new Indexes(i, startIndex, endIndex);
				TrieNode saveFirstChild = prev.firstChild;
				prev.firstChild = new TrieNode(firstIndex, null, null);
				prev.firstChild.sibling = new TrieNode(nextIndex, null, null);
				prev.firstChild.firstChild = saveFirstChild;

			}

		}
		return root;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf
	 * nodes in the trie whose words start with this prefix. For instance, if the
	 * trie had the words "bear", "bull", "stock", and "bell", the completion list
	 * for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and
	 * "bell", and for prefix "bell", completion would be the leaf node that holds
	 * "bell". (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be", the
	 * returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root     Root of Trie that stores all words to search on for
	 *                 completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix   Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		ArrayList<TrieNode> result = completionListHelper(root, allWords, prefix);
		
		if(result.isEmpty()) {
			return null;
		}
		else {
			return result;
		}
	}

	private static ArrayList<TrieNode> completionListHelper(TrieNode root, String[] allWords, String prefix) {

		ArrayList<TrieNode> nodeList = new ArrayList<TrieNode>();
		TrieNode ptr = root;
		if (root.substr == null) {
			ptr = root.firstChild;
		}

		while (ptr != null) {
			if ((allWords[ptr.substr.wordIndex].startsWith(prefix))) {
				if (ptr.firstChild != null) {
					nodeList.addAll(completionListHelper(ptr.firstChild, allWords, prefix));
				} else {
					nodeList.add(ptr);
				}
			} else if ((prefix.startsWith(
					allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1)))) {
				if (ptr.firstChild != null) {
					nodeList.addAll(completionListHelper(ptr.firstChild, allWords, prefix));
				} else {
					nodeList.add(ptr);
				}
			}
			ptr = ptr.sibling;
		}

		return nodeList;

	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}