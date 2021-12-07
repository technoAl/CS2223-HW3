import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Question1 {
	public static void main(String[] args) throws FileNotFoundException {
		DataParserHashTable dataParserHashTable = new DataParserHashTable("./cats/data");
		DataParserBST dataParserBST = new DataParserBST("./cats/data");

		// Demonstration of Search & Top 10

		// Test of top 10
		MinPQ<Term> top10 = dataParserHashTable.top10(dataParserHashTable.documentNames.get(0));
		System.out.println("Top 10 for Document: " + dataParserHashTable.documentNames.get(0));
		while(!top10.isEmpty()){
			Term term = top10.delMin();
			System.out.println(term.word + " : " + term.TF_IDF_SCORE);
		}

		top10 = dataParserHashTable.top10(dataParserHashTable.documentNames.get(1));
		System.out.println("Top 10 for Document: " + dataParserHashTable.documentNames.get(1));
		while(!top10.isEmpty()){
			Term term = top10.delMin();
			System.out.println(term.word + " : " + term.TF_IDF_SCORE);
		}

		top10 = dataParserHashTable.top10(dataParserHashTable.documentNames.get(2));
		System.out.println("Top 10 for Document: " + dataParserHashTable.documentNames.get(2));
		while(!top10.isEmpty()){
			Term term = top10.delMin();
			System.out.println(term.word + " : " + term.TF_IDF_SCORE);
		}

		// So on & so forth for the other documents

		// Test Search for Hash & BST

		System.out.println();

		// Search for "st"
		SearchResult resultST = dataParserHashTable.search("st");
		System.out.println("Search Results for the" + "\"" + resultST.word + "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		resultST = dataParserBST.searchBST("st");
		System.out.println("Search Results for " + "\"" + resultST.word + "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		// Search for "the"
		resultST = dataParserHashTable.search("the");
		System.out.println("Search Results for " + "\"" + resultST.word+ "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		resultST = dataParserBST.searchBST("the");
		System.out.println("Search Results for " + "\"" + resultST.word + "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		// Search for "cat"
		resultST = dataParserHashTable.search("cat");
		System.out.println("Search Results for " + "\"" + resultST.word+ "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		resultST = dataParserBST.searchBST("cat");
		System.out.println("Search Results for " + "\"" + resultST.word + "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		// Search for "cat"
		resultST = dataParserHashTable.search("hi");
		System.out.println("Search Results for " + "\"" + resultST.word+ "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();

		resultST = dataParserBST.searchBST("hi");
		System.out.println("Search Results for " + "\"" + resultST.word + "\"");
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}
		System.out.println();


		// Measuring Time for HashTable vs. BST

		System.out.format("Search on 1/10th size test set averages %fms over 100 iterations for HashTable.\n", testHashTableSearch(dataParserHashTable, 100));
		System.out.format("Search on 1/10th size test set averages %fms over 100 iterations for BST.\n",testBSTSearch(dataParserBST, dataParserHashTable, 100));

		System.out.format("Creation averages 2%fms for HashTable over 100 iterations.\n", testHashTableCreation(100));
		System.out.format("Creation averages 2%fms for BST over 100 iterations.\n",testBSTCreation(100));
	}

	// Creates a test set with the fraction amount of the whole document frequencies
	public static List<String> createTestSet(DataParserHashTable dataParser, int fraction, int remainder){
		List<String> testSet = new ArrayList<>();
		Object[] keys = dataParser.docFreq.getKeys();
		int count = 0;
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == null) continue;
			String key = (String) keys[i];
			if(count % fraction == remainder){ // Every 10th or fraction, add a key to the test set
				testSet.add(key);
			}
			count++;
		}
		return testSet;
	}


	// Test Hashtable Search w/ test set & # of times to run the test
	public static double testHashTableSearch(DataParserHashTable dataParser, int iterations){
		double sum = 0;
		for(int i = 0; i < iterations; i++) {
			List<String> testSet = createTestSet(dataParser, 10, i % 10);
			long start = System.currentTimeMillis();
			for (String key : testSet) {
				SearchResult searchResult = dataParser.search(key);
			}
			long end = System.currentTimeMillis();
			sum += end-start;
		}

		return sum / (double)iterations;
	}

	// Test BST Search w/ test set & # of times to run the test
	public static double testBSTSearch(DataParserBST dataParser, DataParserHashTable dataParserHashTable, int iterations){
		double sum = 0;
		for(int i = 0; i < iterations; i++) {
			List<String> testSet = createTestSet(dataParserHashTable, 10,i % 10);
			long start = System.currentTimeMillis();
			for (String key : testSet) {
				SearchResult searchResult = dataParser.searchBST(key);
			}
			long end = System.currentTimeMillis();
			sum += end-start;
		}

		return sum / (double)iterations;
	}

	// Test Hashtable Creation w/  of times to run the test
	public static double testHashTableCreation(int iterations) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			DataParserHashTable testDataParser = new DataParserHashTable("./cats/data");
		}
		long end = System.currentTimeMillis();
		return (end - start) / (double) iterations;
	}

	// Test BST Creation w/  of times to run the test
	public static double testBSTCreation(int iterations) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
				DataParserBST testDataParser = new DataParserBST("./cats/data");
		}
		long end = System.currentTimeMillis();
		return (end - start) / (double) iterations;
	}


}
