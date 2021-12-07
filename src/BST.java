import java.util.HashSet;
import java.util.Set;

public class BST  <T extends Comparable<T>, U>{
	private Node root;
	private Set<T> keySet = new HashSet<>(); // Stores a set of all keys ever added to the BST
	// Node class
	// N stores number of child nodes
	private class Node {
		private T key;
		private U val;
		private Node left, right;
		private int N;
		public Node(T key, U val, int N){
			this.key = key;
			this.val = val;
			this.N = N;
		}
	}

	private int size(){
		return size(root);
	}

	// Root nodes contains full size, otherwise 0
	private int size(Node node){
		if(node == null){
			return 0;
		} else {
			return node.N;
		}
	}

	// Get
	public U get(T key){
		return get(root, key);
	}

	// Start at the root and then move down the sides until the value is found or null
	private U get(Node node, T key){
		if(node == null){
			return null;
		}

		int cmp = key.compareTo(node.key);
		if(cmp < 0){
			return get(node.left, key);
		} else if(cmp > 0){
			return get(node.right, key);
		} else {
			return node.val;
		}
	}

	// Add value to tree
	// Also add key to the key set for use in data parser
	public void put(T key, U val){
		keySet.add(key);
		root =  put(root, key, val);
	}

	//
	private Node put(Node node, T key, U val) {
		if (node == null){
			return new Node(key, val, 1);
		}
		int cmp = key.compareTo(node.key);
		if (cmp < 0){
			node.left = put(node.left, key, val);
		} else if (cmp > 0){
			node.right = put(node.right, key, val);
		} else {
			node.val = val;
		}
		node.N = size(node.left) + size(node.right) + 1;
		return node;
	}

	public Set<T> getKeys(){
		return keySet;
	}
}
