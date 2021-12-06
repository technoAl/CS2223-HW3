import java.io.FileNotFoundException;
import java.util.PriorityQueue;

public class Question1 {
	public static void main(String[] args) throws FileNotFoundException {
		DataParser dataParser = new DataParser("./cats/data");

		PriorityQueue<Term> top10 = dataParser.top10(dataParser.documentNames.get(0));
		System.out.println("Document: " + dataParser.documentNames.get(0));
		while(!top10.isEmpty()){
			Term term = top10.poll();
			System.out.println(term.word + " " + term.TF_IDF_SCORE);
		}

		SearchResult resultST = dataParser.search("st");
		System.out.println("Search Results for " + resultST.word);
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  "with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
	}
}
