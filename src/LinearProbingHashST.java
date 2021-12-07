public class LinearProbingHashST<T, U> {
	private int N;
	private int M = 16; // Starting Size
	private T[] keys;
	private U[] vals;

	public LinearProbingHashST() {
		keys = (T[]) new Object[M];
		vals = (U[]) new Object[M];
	}

	public LinearProbingHashST(int cap) {
		keys = (T[]) new Object[cap];
		vals = (U[]) new Object[cap];
		M = cap;
	}

	// Hashing function
	// Bitwise & w/ 0x7fffffff which removes sign bit form integer
	// Mods M to remain in the symbol table indices
	private int hash(T key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

	// Resize copies all values over w/ newSize
	private void resize(int newSize) {
		LinearProbingHashST<T, U> t = new LinearProbingHashST<T, U>(newSize);
		for (int i = 0; i < M; i++)
			if (keys[i] != null)
				t.put(keys[i], vals[i]);
		keys = t.keys;
		vals = t.vals;
		M = t.M;
	}

	// Put will resize if necessary
	// Then it adds a new entry to each array at the hash code value
	public void put(T key, U val) {
		if (N >= M/2) resize(2*M);

		int i;
		for (i = hash(key); keys[i] != null; i = (i + 1) % M)
			if (keys[i].equals(key)) {
				vals[i] = val;
				return;
			}

		keys[i] = key;
		vals[i] = val;
		N++;
	}

	// Get will hash and then linear probe for the right key
	public U get(T key){
		for (int i = hash(key); keys[i] != null; i = (i + 1) % M)
			if (keys[i].equals(key)) return vals[i];
		return null;
	}

	public T[] getKeys(){
		return keys;
	}

}
