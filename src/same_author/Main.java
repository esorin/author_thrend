package same_author;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			/*POS*/
			MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-caseless-left3words-distsim.tagger");
			TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
			
			String file1;
			String text1Str;
			List<CoreLabel> rawWords1;
			ArrayList<TaggedWord> wordsList1;
			ArrayList<Sentence> sentenceList1 = new ArrayList<Sentence>();
			Sentence s;
			
		    //ngrams
		    ArrayList<Ngrams> grams2 = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams3 = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams4 = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams5 = new ArrayList<Ngrams>();
		    
		    ArrayList<Ngrams> grams2_DB = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams3_DB = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams4_DB = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams5_DB = new ArrayList<Ngrams>();
		    
		    int countNgrams2 = 0;
		    int countNgrams3 = 0;
		    int countNgrams4 = 0;
		    int countNgrams5 = 0;
		    
			// -- Extract all ngrams possibilities from all texts and write to file --//
		    //Functions.putNgramsinFiles();
		    
		    //get ngrams from files
		    grams2_DB = Functions.getNgramsFromFiles(2);
		    
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
		    
		    /*
		    for(Sentence sen : sentenceList1)
		    {
		    	System.out.println(sen);
		    }
		    */
		    
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
		    
		    //we write the output file of the ngrams with the freq attached
		    Functions.outputNgramsFreq(grams2_DB);
		    
		    
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
