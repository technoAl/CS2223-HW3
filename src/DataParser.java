import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataParser {
	public static void main(String[] args) throws FileNotFoundException {
		List<File> fileList = fillFilesToParse();
		int numDocs = fileList.size();
		List<String> documentNames = getFileNames();
		List<LinearProbingHashST<String, Term>> allDocumentStats = new ArrayList<>();
		LinearProbingHashST<String, Term> docFreq = new LinearProbingHashST<>();
		for(File f:fileList){
			allDocumentStats.add(parseDocument(f, docFreq));
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

	public static SearchResult Search(String word, List<LinearProbingHashST<String, Term>> allDocumentStats, List<String> documentNames){
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

	public static PriorityQueue<Term> top10(String document, List<String> documentNames, List<LinearProbingHashST<String, Term>> allDocumentStats){
		int docNumber = 0;
		for(int i = 0; i < documentNames.size(); i++){
			if(document.equals(documentNames.get(i))){
				docNumber = i;
				break;
			}
		}
		Comparator<Term> minTermComparator = (Term a, Term b) -> (int) (a.TF_IDF_SCORE - b.TF_IDF_SCORE);
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



	public static List<File> fillFilesToParse(){
		File path = new File("./cats/data");
		return new ArrayList<File>(Arrays.asList(path.listFiles()));
	}

	public static List<String> getFileNames(){
		File path = new File("./cats/data");
		return new ArrayList<String>(Arrays.asList(path.list()));
	}

	public static LinearProbingHashST<String, Term> parseDocument(File file, LinearProbingHashST<String, Term> docFreq) throws FileNotFoundException {
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
