package create_db_freq_nrgams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Main {
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
		    //ngrams
		    ArrayList<Ngrams> grams_DB1 = new ArrayList<Ngrams>();
		    String file1 = "input/w1.txt";
		    int size = 2;
		    
			// -- Extract all ngrams possibilities from all texts and write to file --//
		    //Functions.putNgramsinFiles();
		    
		    //if(true)
		    	//return;
		    
		    //get ngrams from files
		    grams_DB1 = Functions.getNgramsFromFiles(size);
		    //grams_DB2 = Functions.getNgramsFromFiles(size);
		    
		    Functions.getFreqFromText(grams_DB1, file1, size);
		    //Functions.getFreqFromText(grams_DB2, file2, size);

		    //we write the output file of the ngrams with the freq attached
		    //Functions.outputNgramsFreqMultipleFiles(size, grams_DB1.size(),grams_DB1, grams_DB2);
		    
		    Functions.putFreqsinFile(file1.substring(6), size, grams_DB1);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
