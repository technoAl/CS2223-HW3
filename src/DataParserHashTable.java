import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataParserHashTable {
	private List<File> fileList;
	private int numDocs;
	public List<String> documentNames;
	public List<LinearProbingHashST<String, Term>> allDocumentStats;
	public LinearProbingHashST<String, Term> docFreq;

	private List<BST<String, Term>> allDocumentStatsBST;
	private BST<String, Term> docFreqBST;

	public DataParserHashTable(String pathName) throws FileNotFoundException {
		fileList = fillFilesToParse(pathName);
		documentNames = getFileNames(pathName);
		numDocs = documentNames.size();
		allDocumentStats = new ArrayList<>();
		docFreq = new LinearProbingHashST<>();
		fillScores();
	}

	private void fillScores() throws FileNotFoundException {
		for(File f:fileList){
			allDocumentStats.add(parseDocument(f));
		}

		for(LinearProbingHashST<String, Term> currentDocTable: allDocumentStats){
			Object[] keys = (Object[]) currentDocTable.getKeys();
			for(int i = 0; i < keys.length; i++){
				if(keys[i] == null) continue;
				String key = (String) keys[i];
				Term currentStat = currentDocTable.get(key);
				currentStat.TF = Math.log10(1 + currentStat.frequency);
				currentStat.IDF = Math.log10( (double) numDocs / docFreq.get(key).frequency);
				currentStat.TF_IDF_SCORE = currentStat.TF * currentStat.IDF; // Using alternate formula:  count-of-word-in-doc * log(numDocs / count-of-docs-containing-word)
			}
		}
	}

	public SearchResult search(String word){
		SearchResult result = new SearchResult(word);
		result.word = word;

		for(int i = 0; i < allDocumentStats.size(); i++){
			LinearProbingHashST<String, Term> currentDocTable = allDocumentStats.get(i);
			Term currentStat = currentDocTable.get(word);
			if(currentStat == null){
				continue;
			}
			result.addTerm(documentNames.get(i), currentStat);
		}
		return result;
	}

	public MinPQ<Term> top10(String document){
		int docNumber = 0;
		for(int i = 0; i < documentNames.size(); i++){
			if(document.equals(documentNames.get(i))){
				docNumber = i;
				break;
			}
		}

		MinPQ<Term> minPQTop10 = new MinPQ<>();

		LinearProbingHashST<String, Term> desiredDocTable = allDocumentStats.get(docNumber);
		Object[] keys = (Object[])desiredDocTable.getKeys();
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == null) continue;
			String key = (String) keys[i];
			Term currentStat = desiredDocTable.get(key);
			if(minPQTop10.size() < 10) {
				minPQTop10.insert(currentStat);
			} else if(minPQTop10.min().TF_IDF_SCORE < currentStat.TF_IDF_SCORE){
				minPQTop10.delMin();
				minPQTop10.insert(currentStat);
			}
		}
		return minPQTop10;

	}

	private List<File> fillFilesToParse(String pathName){
		File path = new File(pathName);
		return new ArrayList<File>(Arrays.asList(path.listFiles()));
	}

	private List<String> getFileNames(String pathName){
		File path = new File(pathName);
		return new ArrayList<String>(Arrays.asList(path.list()));
	}

	private LinearProbingHashST<String, Term> parseDocument(File file) throws FileNotFoundException {
		LinearProbingHashST<String, Term> frequencies = new LinearProbingHashST<>();
		Scanner in = new Scanner(file);
		while(in.hasNextLine()){
			String line = in.nextLine();
			for(String word: line.split("\\s+")){
				word = word.replaceAll("[^a-zA-Z ]", "");
				word = word.toLowerCase();
				if(word.equals("") || word == null){
					continue;
				}
				Term currentStats = frequencies.get(word);
				if(currentStats != null){
					currentStats.frequency++;
					//frequencies.put(word, currentStats);
				} else {
					frequencies.put(word, new Term(word, 1));
				}

				currentStats = docFreq.get(word);
				if(currentStats != null){
					currentStats.frequency++;
					//frequencies.put(word, currentStats);
				} else {
					docFreq.put(word, new Term(word, 1));
				}
			}
		}

		return frequencies;
	}
}
