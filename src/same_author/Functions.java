package same_author;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
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

public class Functions {
	
	/* Get freq of n-grams from text file */
	public static void getFreqFromText(ArrayList<Ngrams> grams_DB, String file, int size)
	{
		try
		{
			MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-caseless-left3words-distsim.tagger");
			TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
			
			String text1Str;
			List<CoreLabel> rawWords1;
			ArrayList<TaggedWord> wordsList1;
			ArrayList<Sentence> sentenceList1 = new ArrayList<Sentence>();
			Sentence s;

			text1Str = Functions.readFile(file);
		    rawWords1 = tokenizerFactory.getTokenizer(new StringReader(text1Str)).tokenize();
		    wordsList1 = tagger.apply(rawWords1);
		    
		    ArrayList<Ngrams> grams_size = new ArrayList<Ngrams>();
		    int countNgrams_size = 0;
		    	    
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
		    	for(Ngrams ngram1 : sen1.get_ngrams(size))
		    	{
		    		if(grams_size.contains(ngram1))
		    		{
		    			continue;
		    		}
		    		grams_size.add(ngram1);
		    		for(Sentence sen2 : sentenceList1)
		    		{
		    			for(Ngrams ngram2 : sen2.get_ngrams(size))
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
		    
		    for(Ngrams ngram : grams_size)
		    {
		    	countNgrams_size += ngram.count;
		    }
		    System.out.println("total count= " + countNgrams_size);
		    
		    for(Ngrams ngram : grams_size)
		    {
		    	ngram.freq = (float)ngram.count/countNgrams_size;
		    	//System.out.printf(ngram + " %.8f\n", ngram.freq);
		    }
		    
		    //we assign for every ngram in the DB the freq of it that we calculate in the current text
		    for(Ngrams n_db : grams_DB)
		    {
		    	//System.out.println(n_db);
		    	//System.out.println("-------------");
		    	for(Ngrams n : grams_size)
		    	{
		    		//System.out.println(n);
		    		if(n.equals(n_db))
		    		{
		    			n_db.freq = n.freq;			
		    		}
		    	}
		    }
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/*Deschide si citeste fisier*/
	public static String readFile(String path) throws IOException 
	{
		  FileInputStream stream = new FileInputStream(new File(path));
		  try 
		  {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    return Charset.defaultCharset().decode(bb).toString();
		  }
		  finally 
		  {
		    stream.close();
		  }
	}
	
	/* Create files with ngrams from input data */
	public static void putNgramsinFiles()
	{
		try
		{
			/*POS*/
			MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-caseless-left3words-distsim.tagger");
			TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
			
			Sentence s;
			
		    //ngrams
		    ArrayList<Ngrams> grams2 = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams3 = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams4 = new ArrayList<Ngrams>();
		    ArrayList<Ngrams> grams5 = new ArrayList<Ngrams>();
			
			String f1 = "input/1.txt";
			String f2 = "input/2.txt";
			String f3 = "input/3.txt";
			String f4 = "input/hg4.txt";
			String f5 = "input/r1.txt";
			String f6 = "input/r2.txt";
			String f7 = "input/hg3.txt";
			String f8 = "input/r4.txt";
			
			String text_train = "";
			List<CoreLabel> rawWords_train;
			ArrayList<TaggedWord> wordsList_train;
			ArrayList<Sentence> sentenceList_train = new ArrayList<Sentence>();
			
			text_train += readFile(f1);
			text_train += readFile(f2);
			text_train += readFile(f3);
			text_train += readFile(f4);
			text_train += readFile(f5);
			text_train += readFile(f6);
			text_train += readFile(f7);
			//text_train += readFile(f8);
			rawWords_train = tokenizerFactory.getTokenizer(new StringReader(text_train)).tokenize();
			wordsList_train = tagger.apply(rawWords_train);			
			
			
			//impartim in propozitii
			s = new Sentence();
			for(TaggedWord w : wordsList_train)
			{
				s.addWord(w);
				if(w.tag().equals("."))
				{
					sentenceList_train.add(s);
					s = new Sentence();
				}
			}
			
			// Detectam ngrams si le scriem in fiser
			for(Sentence sen : sentenceList_train)
			{
				//5- grams
				for(Ngrams ngram : sen.get_ngrams(4))
				{
					if(grams4.contains(ngram))
					{
						continue;
					}
					grams4.add(ngram);
				}
			}
						
			//write to file of 5 grams
			PrintWriter f_4grams = new PrintWriter("db/4grams.txt", "UTF-8");
			
			//write to file of 5 grams
			for(Ngrams ngram : grams4)
			{
				f_4grams.println(ngram);
				//System.out.println(ngram);
			}
			
			f_3grams.close();
			return;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void outputNgramsFreq(ArrayList<Ngrams> ngrams, int size)
	{
		try
		{
			//write to file of size grams
			String output_file = "output/" + size + "Freq.txt";
			PrintWriter f_grams = new PrintWriter(output_file, "UTF-8");
			
			//write to file of size grams
			f_grams.printf("##|##\n");
			f_grams.printf("Ngrams|Frequency\n");
			for(int i=0; i<ngrams.size(); i++)
			{
				f_grams.printf(i + " " + ngrams.get(i).toString() + "|%.8f\n", ngrams.get(i).freq);
			}
			
			f_grams.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void outputNgramsFreqMultipleFiles(ArrayList<Ngrams> ngrams1, ArrayList<Ngrams> ngrams2, int size)
	{
		try
		{
			//write to file of size grams
			String output_file = "output/" + size + "Freq.txt";
			PrintWriter f_grams = new PrintWriter(output_file, "UTF-8");
			
			//write to file of size grams
			f_grams.printf("##|##\n");
			f_grams.printf("Frequency1|Frequency2\n");
			for(int i=0; i<ngrams1.size(); i++)
			{
				f_grams.printf( "%.8f|%.8f\n", ngrams1.get(i).freq, ngrams2.get(i).freq);
			}
			
			f_grams.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/* get ngrams from files */
	public static ArrayList<Ngrams> getNgramsFromFiles(int n)
	{
		ArrayList<Ngrams> ngrams = new ArrayList<Ngrams>();
		Ngrams ngram;
		String file = "db/" + n + "grams.txt";
		BufferedReader br;
		String line;
		String[] tokens;
		try
		{
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) 
			{
				// process the line.
				tokens = line.split(" ", n);
				ngram = new Ngrams(tokens);
				ngrams.add(ngram);
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
		return ngrams;
	}

}
