package std_deviation;

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
		    String file1 = "input/1.txt";
		    ArrayList<Ngrams> grams_DB2 = new ArrayList<Ngrams>();
		    String file2 = "input/w1.txt";
		    ArrayList<Ngrams> grams_DB3 = new ArrayList<Ngrams>();
		    String file3 = "in.txt";
		    int size = 2;
		    
			// -- Extract all ngrams possibilities from all texts and write to file --//
		    //Functions.putNgramsinFiles();
		    
		    //if(true)
		    	//return;
		    
		    //get ngrams from files
		    grams_DB1 = Functions.getNgramsFromFiles(size);
		    grams_DB2 = Functions.getNgramsFromFiles(size);
		    
		    Functions.getFreqFromText(grams_DB1, file1, size);
		    Functions.getFreqFromText(grams_DB2, file2, size);

		    //we write the output file of the ngrams with the freq attached
		    //Functions.outputNgramsFreq(grams_DB, size);
		    Functions.outputNgramsFreqMultipleFiles(size, grams_DB1.size(),grams_DB1, grams_DB2);
		    
		    /*
			// -- get the freq of ngrams -- //
			//text
			file1 = "input/w1.txt";
			//file1 = "in.txt";
			text1Str = Functions.readFile(file1);
		    rawWords1 = tokenizerFactory.getTokenizer(new StringReader(text1Str)).tokenize();
		    wordsList1 = tagger.apply(rawWords1);
		    	    
		    //impartim in propozitii
		    s = new Sentence();
		    for(TaggedWord w : wordsList1)
		    {
		    	s.addWord(w);
		    	if(w.tag().equals("."))
		    	{
		    		sentenceList1.add(s);
		    		s = new Sentence();
		    	}
		    }
		    
		    //calculam frecventele POS n-gramelelor
		    for(Sentence sen1 : sentenceList1)
		    {
		    	//2- grams
		    	for(Ngrams ngram1 : sen1.get_ngrams(2))
		    	{
		    		if(grams2.contains(ngram1))
		    		{
		    			continue;
		    		}
		    		grams2.add(ngram1);
		    		for(Sentence sen2 : sentenceList1)
		    		{
		    			for(Ngrams ngram2 : sen2.get_ngrams(2))
		    			{
		    				if(ngram1.equals(ngram2))
		    				{
		    					ngram1.incrementCount();
		    				}
		    			}
		    		}
		    	}
		    }
		    
		    		    
		    for(Ngrams ngram : grams2)
		    {
		    	countNgrams2 += ngram.count;
		    }
		    System.out.println("total count= " + countNgrams2);
		    
		    for(Ngrams ngram : grams2)
		    {
		    	ngram.freq = (float)ngram.count/countNgrams2;
		    	//System.out.printf(ngram + " %.8f\n", ngram.freq);
		    }
		    
		    //we assign for every ngram in the DB the freq of it that we calculate in the current text
		    for(Ngrams n_db : grams2_DB)
		    {
		    	//System.out.println(n_db);
		    	//System.out.println("-------------");
		    	for(Ngrams n : grams2)
		    	{
		    		//System.out.println(n);
		    		if(n.equals(n_db))
		    		{
		    			n_db.freq = n.freq;			
		    		}
		    	}
		    }
		    */
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
