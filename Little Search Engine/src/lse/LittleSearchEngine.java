package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		if(docFile == null) {
			throw new FileNotFoundException("Document file is not found on disk");
		}
		HashMap<String, Occurrence> keywords = new HashMap<String, Occurrence>();
		
		//scans document
		Scanner scan = new Scanner(new File(docFile));
		while(scan.hasNext()) {
			String keyword = getKeyword(scan.next());
			if(keyword != null) {
				//if keyword is first occurrence in hash table
				if(!keywords.containsKey(keyword)) {
					Occurrence newItem = new Occurrence(docFile, 1);
					keywords.put(keyword, newItem);
				}
				//if keyword already exists in hash table
				else {
					Occurrence existingItem =  keywords.get(keyword);
					existingItem.frequency++;
					keywords.put(keyword, existingItem);
				}
			}
			
		}
		scan.close(); 
		return keywords; 
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(String word: kws.keySet()) {
			
			//first occurrence of word 
			if(!keywordsIndex.containsKey(word)) {
				ArrayList<Occurrence> newDoc = new ArrayList<Occurrence>(); 
				newDoc.add(kws.get(word));
				insertLastOccurrence(newDoc);
				keywordsIndex.put(word, newDoc);
			}
			//master index already contains word 
			else {
				ArrayList<Occurrence> document = keywordsIndex.get(word);
				document.add(kws.get(word));
				insertLastOccurrence(document);
				keywordsIndex.put(word, document);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(word == null) {
			return null; 
		}
		String s = word.toLowerCase();
		//removes trailing punctuation
		for(int i = s.length()-1; i >= 0; i--) { 
			if(s.charAt(i) == '.' || s.charAt(i) == ',' || s.charAt(i) == '?' || s.charAt(i) == ':' || s.charAt(i) == ';' || s.charAt(i) == '!') {
				s = s.substring(0, i);
			}
			else {
				break; 
			}
		}
		//checks if characters are all alphabetical 
		for(int i = 0; i < s.length()-1; i++) { 
			if(!Character.isLetter(s.charAt(i))) {
				return null; 
			}
		}
		 //checks if word is a noise word
		if(noiseWords.contains(s)) {
			return null; 
		}
		
		//if string only includes trailing punctuation 
		if(s.length() == 0) {
			return null; 
		}
		
		return s;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		if(occs.size() == 1) {
			return null; 
		}
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		int lo = 0; 
		int hi = occs.size()-2; 
		int mid = (lo + hi)/2;
		Occurrence lastOccur = occs.get(occs.size()-1); // last occurrence in occs arraylist 
		//binary search 
		while(lo <= hi) {
			mid = (lo + hi)/2; 
			indices.add(mid);
	
			if(occs.get(mid).frequency < lastOccur.frequency) {
				hi = mid - 1; 
			}
			else if(occs.get(mid).frequency > lastOccur.frequency) {
				lo = mid + 1; 
			}
			else {
				break; 
			}
		}
		Occurrence insertVal = occs.remove(occs.size()-1);
		if(lo > hi) { 
			occs.add(lo, insertVal); 
		}
		else { //lo = hi 
			occs.add(mid+1, insertVal);
		}
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return indices;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> result = new ArrayList<String>(); 
		kw1 = getKeyword(kw1);
		kw2 = getKeyword(kw2); 
		
		ArrayList<Occurrence> occur1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> occur2 = keywordsIndex.get(kw2); 
		
		//no matches
		if(kw1 == null && kw2 == null) {
			return null; 
		}
		if(keywordsIndex.isEmpty()) {
			return null; 
		}
		if(!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			return null; 
		}
		
		ArrayList<Occurrence> allOccurrences = new ArrayList<Occurrence>(); 
		//the first keyword is added before the second keyword 
		if(occur1 != null) {
			allOccurrences.addAll(occur1);
		}
		if(occur2 != null) {
			allOccurrences.addAll(occur2); 
		}
		
		String doc = ""; 
		int index = 0; 
		int frequency = 0; 
		
		while(result.size() < 5) {
			
			//iterate through all occurrences 
			for(int i = 0; i < allOccurrences.size(); i++) {
				if(allOccurrences.get(i).frequency > frequency) {
					frequency = allOccurrences.get(i).frequency; 
					doc = allOccurrences.get(i).document; 
					index = i; 
				}
			}
			//removes most frequent occurrence 
			allOccurrences.remove(index);
			
			//prevents duplicates 
			if(!result.contains(doc)) {
				result.add(doc);
			}
			
			//no items left in list 
			if(allOccurrences.isEmpty()) {
				break; 
			}
			
			//reset for next item to list 
			frequency = 0; 
		}
		System.out.print(result);
		return result; 
	}
}
		
		

