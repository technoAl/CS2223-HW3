public class Term {
	String word;
	int frequency;
	double TF_IDF_SCORE = 0;
	double TF = 0;
	double IDF = 0;
	public Term(String word, int frequency){
		this.word = word;
		this.frequency = frequency;
	}
}
