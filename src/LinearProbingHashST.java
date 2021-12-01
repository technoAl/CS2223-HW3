public class LinearProbingHashST<T, U> {
	private int N;
	private int M = 16;
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

	private int hash(T key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

	private void resize(int newSize) {
		LinearProbingHashST<T, U> t = new LinearProbingHashST<T, U>(newSize);
		for (int i = 0; i < M; i++)
			if (keys[i] != null)
				t.put(keys[i], vals[i]);
		keys = t.keys;
		vals = t.vals;
		M = t.M;
	}

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

	public U get(T key){
		for (int i = hash(key); keys[i] != null; i = (i + 1) % M)
			if (keys[i].equals(key)) return vals[i];
		return null;
	}

	public T[] getKeys(){
		return keys;
	}

}
