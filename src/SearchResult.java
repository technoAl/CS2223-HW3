import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	String word;
	List<String> documentNames;
	List<Term> termScores;
	public SearchResult(String word){
		documentNames = new ArrayList<>();
		termScores = new ArrayList<>();
	}

	public void addTerm(String doc, Term term){
		documentNames.add(doc);
		termScores.add(term);
	}
}
