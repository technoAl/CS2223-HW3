import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Question1 {
	public static void main(String[] args) throws FileNotFoundException {
		DataParserHashTable dataParserHashTable = new DataParserHashTable("./cats/data");

		PriorityQueue<Term> top10 = dataParserHashTable.top10(dataParserHashTable.documentNames.get(0));
		System.out.println("Document: " + dataParserHashTable.documentNames.get(0));
		while(!top10.isEmpty()){
			Term term = top10.poll();
			System.out.println(term.word + " " + term.TF_IDF_SCORE);
		}

		SearchResult resultST = dataParserHashTable.search("st");
		System.out.println("Search Results for " + resultST.word);
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}

		DataParserBST dataParserBST = new DataParserBST("./cats/data");
		resultST = dataParserBST.searchBST("st");
		System.out.println("Search Results for " + resultST.word);
		for(int i = 0; i < resultST.termScores.size(); i++){
			System.out.println("Found in " + resultST.documentNames.get(i) +  " with frequency "+ resultST.termScores.get(i).frequency + " and score " + resultST.termScores.get(i).TF_IDF_SCORE);
		}


		List<String> testSet = createTestSet(dataParserHashTable, 10);

		System.out.println(testHashTableSearch(dataParserHashTable, testSet, 10));
		System.out.println(testBSTSearch(dataParserBST, testSet, 10));

		System.out.println(testHashTableCreation(testSet, 1));
		System.out.println(testBSTCreation(testSet, 1));

	}

	public static List<String> createTestSet(DataParserHashTable dataParser, int fraction){
		List<String> testSet = new ArrayList<>();
		Object[] keys = dataParser.docFreq.getKeys();
		int count = 0;
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == null) continue;
			String key = (String) keys[i];
			if(count % fraction == 0){
				testSet.add(key);
			}
			count++;
		}
		return testSet;
	}

	public static long testHashTableSearch(DataParserHashTable dataParser, List<String> testSet, int iterations){
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			for (String key : testSet) {
				SearchResult searchResult = dataParser.search(key);
			}
		}
		long end = System.currentTimeMillis();
		return end - start;
	}

	public static long testBSTSearch(DataParserBST dataParser, List<String> testSet, int iterations){
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			for (String key : testSet) {
				SearchResult searchResult = dataParser.searchBST(key);
			}
		}
		long end = System.currentTimeMillis();
		return end - start;
	}

	public static long testHashTableCreation(List<String> testSet, int iterations) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			DataParserHashTable testDataParser = new DataParserHashTable("./cats/data");
		}
		long end = System.currentTimeMillis();
		return end - start;
	}

	public static long testBSTCreation(List<String> testSet, int iterations) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			for (String key : testSet) {
				DataParserBST testDataParser = new DataParserBST("./cats/data");
			}
		}
		long end = System.currentTimeMillis();
		return end - start;
	}


}
