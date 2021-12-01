public class BST  <T extends Comparable<T>, U>{
	private Node root;
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

	private int size(Node node){
		if(node == null){
			return 0;
		} else {
			return node.N;
		}
	}

	public U get(T key){
		return get(root, key);
	}

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

	public void put(T key, U val){
		root =  put(root, key, val);
	}

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
}
