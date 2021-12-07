public class MinPQ<T extends Comparable<T>> {
	private T[] pq;
	private int n = 0;

	// Default max capacity of 1001
	public MinPQ(){
		pq = (T[]) new Comparable[1000+1];
	}

	public MinPQ(int maxN) {
		pq = (T[]) new Comparable[maxN+1];
	}

	// Takes an array an makes it into a minpq
	public MinPQ(T[] a){
		pq =  (T[]) new Comparable[a.length * 2+1];
		for (T t : a) {
			insert(t);
		}
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	// Adds a value to the end, then swims/checks if it's smaller
	public void insert(T v) {
		pq[++n] = v;
		swim(n);
	}

	// Returns min value
	public T min(){
		return pq[1];
	}

	// Deletes and returns the min value
	// Swaps it out of the effective space of the array, then sinks value swapped in to the correct place
	public T delMin() {
		T max = pq[1];
		swap(1, n--);
		pq[n+1] = null;
		sink(1);
		return max;
	}

	// Greater
	private boolean greater(int i, int j) {
		return pq[i].compareTo(pq[j]) > 0;
	}

	// Swap
	private void swap(int i, int j){
		T tmp = pq[i];
		pq[i] = pq[j];
		pq[j] = tmp;
	}

	// Swim takes a node placed at the leaf nodes and swims it up if it's less than parents
	// Does this by swapping w/ parents when the parents are greater than the child
	private void swim(int k) {
		while(k >= 2 && greater(k/2, k)){
			swap(k/2, k);
			k = k/2;
		}
	}

	// Swink takes a node placed at the root and sinks it down to the proper place
	// Does this by swapping w/ children when the children are less than the parent
	private void sink(int k) {
		while(2 * k < n+1 ){
			int child = 2 * k;
			if(child + 1 <= n && greater(child, child+1)) child++;
			if(greater(k, child)){
				swap(k, child);
			}
			k = child;
		}
	}

}
