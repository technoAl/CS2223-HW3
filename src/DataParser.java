import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataParser {
	private List<File> fileList;
	private int numDocs;
	public List<String> documentNames;
	private List<LinearProbingHashST<String, Term>> allDocumentStats;
	private LinearProbingHashST<String, Term> docFreq;

	private List<BST<String, Term>> allDocumentStatsBST;
	private BST<String, Term> docFreqBST;

	public DataParser(String pathName) throws FileNotFoundException {
		fileList = fillFilesToParse(pathName);
		numDocs = fileList.size();
		documentNames = getFileNames(pathName);
		allDocumentStats = new ArrayList<>();
		docFreq = new LinearProbingHashST<>();
		fillScores();
	}

	public void initBST() throws FileNotFoundException {
		allDocumentStatsBST = new ArrayList<>();
		docFreqBST = new BST<>();
		fillScoresBST();
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
				currentStat.TF = Math.log(1 + currentStat.frequency);
				currentStat.IDF = Math.log( (double) numDocs / docFreq.get(key).frequency);
				currentStat.TF_IDF_SCORE = currentStat.TF * currentStat.IDF;
			}
		}
	}

	private void fillScoresBST() throws FileNotFoundException {
		for(File f:fileList){
			allDocumentStats.add(parseDocument(f));
		}

		for(BST<String, Term> currentDocTable: allDocumentStatsBST){
			Set<String> keys = currentDocTable.getKeys();
			for(String key: keys){
				Term currentStat = currentDocTable.get(key);
				currentStat.TF = Math.log(1 + currentStat.frequency);
				currentStat.IDF = Math.log( (double) numDocs / docFreq.get(key).frequency);
				currentStat.TF_IDF_SCORE = currentStat.TF * currentStat.IDF;
			}
		}
	}

	public SearchResult search(String word){
		SearchResult result = new SearchResult(word);

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

	public SearchResult searchBST(String word){
		SearchResult result = new SearchResult(word);
		for(int i = 0; i < allDocumentStatsBST.size(); i++){
			BST<String, Term> currentDocTable = allDocumentStatsBST.get(i);
			Term currentStat = currentDocTable.get(word);
			if(currentStat == null){
				continue;
			}
			result.addTerm(documentNames.get(i), currentStat);
		}
		return result;
	}

	public PriorityQueue<Term> top10(String document){
		int docNumber = 0;
		for(int i = 0; i < documentNames.size(); i++){
			if(document.equals(documentNames.get(i))){
				docNumber = i;
				break;
			}
		}

		Comparator<Term> minTermComparator = Comparator.comparingDouble((Term a) -> a.TF_IDF_SCORE);
		PriorityQueue<Term> minPQTop10 = new PriorityQueue<>(minTermComparator);

		LinearProbingHashST<String, Term> desiredDocTable = allDocumentStats.get(docNumber);
		Object[] keys = (Object[])desiredDocTable.getKeys();
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == null) continue;
			String key = (String) keys[i];
			Term currentStat = desiredDocTable.get(key);
			if(minPQTop10.size() < 10) {
				minPQTop10.add(currentStat);
			} else if(minPQTop10.peek().TF_IDF_SCORE < currentStat.TF_IDF_SCORE){
				minPQTop10.poll();
				minPQTop10.add(currentStat);
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

	private BST<String, Term> parseDocumentBST(File file) throws FileNotFoundException {
		BST<String, Term> frequencies = new BST<>();
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

				currentStats = docFreqBST.get(word);
				if(currentStats != null){
					currentStats.frequency++;
					//frequencies.put(word, currentStats);
				} else {
					docFreqBST.put(word, new Term(word, 1));
				}
			}
		}
		return frequencies;
	}
}
