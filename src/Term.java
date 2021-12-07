public class Term implements Comparable<Term>{
	String word;
	int frequency;
	double TF_IDF_SCORE = 0;
	double TF = 0;
	double IDF = 0;
	public Term(String word, int frequency){
		this.word = word;
		this.frequency = frequency;
	}

	@Override
	public int compareTo(Term competitor) {
		return Double.compare(this.TF_IDF_SCORE, competitor.TF_IDF_SCORE);
	}
}
