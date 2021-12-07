import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataParserBST {
	private List<File> fileList;
	private int numDocs;
	public java.util.List<String> documentNames;

	private List<BST<String, Term>> allDocumentStatsBST;
	private BST<String, Term> docFreqBST;

	public DataParserBST(String pathName) throws FileNotFoundException {
		fileList = fillFilesToParse(pathName);
		documentNames = getFileNames(pathName);
		numDocs = documentNames.size();
		allDocumentStatsBST = new ArrayList<>();
		docFreqBST = new BST<>();
		fillScoresBST();
	}

	private void fillScoresBST() throws FileNotFoundException {
		for(File f:fileList){
			allDocumentStatsBST.add(parseDocumentBST(f));
		}

		for(BST<String, Term> currentDocTable: allDocumentStatsBST){
			Set<String> keys = currentDocTable.getKeys();
			for(String key: keys){
				Term currentStat = currentDocTable.get(key);
				currentStat.TF = Math.log10(1 + currentStat.frequency);
				currentStat.IDF = Math.log10( (double) numDocs / (double) docFreqBST.get(key).frequency);
				currentStat.TF_IDF_SCORE = currentStat.TF * currentStat.IDF;
			}
		}
	}

	public SearchResult searchBST(String word){
		SearchResult result = new SearchResult(word);
		result.word = word;
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

	private List<File> fillFilesToParse(String pathName){
		File path = new File(pathName);
		return new ArrayList<File>(Arrays.asList(path.listFiles()));
	}

	private List<String> getFileNames(String pathName){
		File path = new File(pathName);
		return new ArrayList<String>(Arrays.asList(path.list()));
	}

	private BST<String, Term> parseDocumentBST(File file) throws FileNotFoundException {
		BST<String, Term> frequencies = new BST<>();
		Scanner in = new Scanner(file);
		while (in.hasNextLine()) {
			String line = in.nextLine();
			for (String word : line.split("\\s+")) {
				word = word.replaceAll("[^a-zA-Z ]", "");
				word = word.toLowerCase();
				if (word.equals("") || word == null) {
					continue;
				}
				Term currentStats = frequencies.get(word);
				if (currentStats != null) {
					currentStats.frequency++;
					//frequencies.put(word, currentStats);
				} else {
					frequencies.put(word, new Term(word, 1));
				}

				currentStats = docFreqBST.get(word);
				if (currentStats != null) {
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
