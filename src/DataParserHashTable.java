import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataParserHashTable {
	private List<File> fileList; // List of all files to parse
	private int numDocs; // Number of documents
	public List<String> documentNames; // List of document Names
	public List<LinearProbingHashST<String, Term>> allDocumentStats; // List of each documents individual stats
	public LinearProbingHashST<String, Term> docFreq; // Cumulative list of all documents

	// Constructor fills all tables in a given path
	public DataParserHashTable(String pathName) throws FileNotFoundException {
		fileList = fillFilesToParse(pathName);
		documentNames = getFileNames(pathName);
		numDocs = documentNames.size();
		allDocumentStats = new ArrayList<>();
		docFreq = new LinearProbingHashST<>();
		fillScores();
	}

	// Go through each document & fill scores
	private void fillScores() throws FileNotFoundException {
		// Loops through each doc & fills tables
		// Also fills large doc at the same time
		for(File f:fileList){
			allDocumentStats.add(parseDocument(f));
		}

		// Loops through each table & update TF-IDF scores
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

	// Search Function
	public SearchResult search(String word){
		//Creates a Search Result for the class
		SearchResult result = new SearchResult(word);
		result.word = word;

		// Loop through each document & check for the terms presence
		for(int i = 0; i < allDocumentStats.size(); i++){
			LinearProbingHashST<String, Term> currentDocTable = allDocumentStats.get(i);
			Term currentStat = currentDocTable.get(word);
			if(currentStat == null){
				continue;
			}
			result.addTerm(documentNames.get(i), currentStat); // Add term to search result
		}
		return result;
	}


	// Return a MinPQ containing top 10 search results for a document
	public MinPQ<Term> top10(String document){
		int docNumber = 0;
		for(int i = 0; i < documentNames.size(); i++){
			if(document.equals(documentNames.get(i))){
				docNumber = i;
				break;
			}
		}

		MinPQ<Term> minPQTop10 = new MinPQ<>();

		// Get the doctable for the right document
		LinearProbingHashST<String, Term> desiredDocTable = allDocumentStats.get(docNumber);
		Object[] keys = (Object[])desiredDocTable.getKeys();
		// Loop through the doc table
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == null) continue;
			String key = (String) keys[i];
			Term currentStat = desiredDocTable.get(key);
			// Fill PQ to size 10
			if(minPQTop10.size() < 10) {
				minPQTop10.insert(currentStat);

			// If the PQ is at size 10 and the current Stat is greater than the 10th place term, move it in and kick out the 10th place term
			} else if(minPQTop10.min().TF_IDF_SCORE < currentStat.TF_IDF_SCORE){
				minPQTop10.delMin();
				minPQTop10.insert(currentStat);
			}
		}

		return minPQTop10;
	}

	// Fills files to parse
	private List<File> fillFilesToParse(String pathName){
		File path = new File(pathName);
		return new ArrayList<File>(Arrays.asList(path.listFiles()));
	}

	// Fills file names
	private List<String> getFileNames(String pathName){
		File path = new File(pathName);
		return new ArrayList<String>(Arrays.asList(path.list()));
	}

	// Parse each individual document
	private LinearProbingHashST<String, Term> parseDocument(File file) throws FileNotFoundException {
		LinearProbingHashST<String, Term> frequencies = new LinearProbingHashST<>();
		Scanner in = new Scanner(file);
		while(in.hasNextLine()){
			String line = in.nextLine();
			// Splits by spaces
			for(String word: line.split("\\s+")){
				// Replaces all values that aren't alphabetical
				word = word.replaceAll("[^a-zA-Z ]", "");
				word = word.toLowerCase();
				if(word.equals("") || word == null){
					continue;
				}

				Term currentStats = frequencies.get(word);
				// If the word is found to already exist, increment frequency
				if(currentStats != null){
					currentStats.frequency++;
					//frequencies.put(word, currentStats);

				// If the word isn't found make a new key
				} else {
					frequencies.put(word, new Term(word, 1));
				}

				// Repeat the same update process for the LargeTable w/ all documents
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
