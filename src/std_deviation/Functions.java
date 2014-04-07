package std_deviation;

import java.io.BufferedReader;
import java.lang.Math;
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
	
	/* get freq of ngrams from file */
	public static ArrayList<Ngrams> getFreqFromFile(int n, String file)
	{
		ArrayList<Ngrams> ngrams = new ArrayList<Ngrams>();
		Ngrams ngram;
		BufferedReader br;
		String line;
		String[] tokens;
		String[] freqStr;
		int m;
		try
		{
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) 
			{
				// process the line.
				tokens = line.split(" ", n);
				ngram = new Ngrams(tokens);
				
				freqStr = line.split(" | ");
				m = freqStr.length;
				ngram.freq = Float.parseFloat(freqStr[m-1]);
				
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
	
	/* Put in file the freq of ngrams of a text */
	public static void putFreqsinFile(String filein, int ngrams_dimension, ArrayList<Ngrams> text)
	{
		try
		{
			String fileout;

			fileout = "db_authors_ngrams_freq/" + ngrams_dimension + "_" + filein;
			PrintWriter file_writer = new PrintWriter(fileout, "UTF-8");
			
			//write to file of 5 grams
			for(Ngrams ngram : text)
			{
				file_writer.printf(ngram + " | %.8f\n", ngram.freq);
			}
			
			file_writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/*Get standard deviation*/
	public static double[][] getStdDev(int line_number, ArrayList<Ngrams>... texts)
	{
		int text_number = texts.length;
		double[][] db = new double[line_number][text_number];
		double[] sum = new double[line_number];
		double[] med = new double[line_number];
		double[] st = new double[line_number];
		double[] sum_st = new double[line_number];
		double[][] interval = new double[line_number][2];
		int i=0, j=0;
		
		//extract values
		for(ArrayList<Ngrams> text : texts)
		{
			i = 0;
			for(Ngrams n : text)
			{
				db[i][j] = n.freq;
				i++;
			}
			j++;
		}
		
		//media pe rand
		for(i=0;i<line_number;i++)
		{
			sum[i] = 0;
			for(j=0;j<text_number;j++)
			{
				sum[i] += db[i][j];
			}
			med[i] = sum[i]/text_number;
		}
		
		//standard deviation
		for(i=0;i<line_number;i++)
		{
			sum_st[i] = 0;
			for(j=0;j<text_number;j++)
			{
				sum_st[i] += Math.pow((db[i][j] - med[i]), 2);
			}
			st[i] = Math.sqrt(sum_st[i]/text_number);
			
			interval[i][0] = med[i] - st[i];
			interval[i][1] = med[i] + st[i];
		}
		
		return interval;
	}
	
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
			
			f_4grams.close();
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
	
	public static void outputNgramsFreqMultipleFiles(int size, int line_number, ArrayList<Ngrams>... texts)
	{
		try
		{
			int i=0, j=0;
			int dim = texts.length;
			double[][] db = new double[line_number][dim];
			
			//write to file of size grams
			String output_file = "output/" + size + "Freq.txt";
			PrintWriter f_grams = new PrintWriter(output_file, "UTF-8");
			
			//write header o file
			f_grams.printf("##|##\n");
			f_grams.printf("Frequency0");
			for(i=1;i<texts.length;i++)
			{
				f_grams.printf("|Frequency" + i);
			}
			f_grams.printf("\n");
			
			
			//extract values
			i=0;
			j=0;
			for(ArrayList<Ngrams> text : texts)
			{
				i=0;
				for(Ngrams n : text)
				{
					db[i][j] = n.freq;
					i++;
				}
				j++;
			}
			
			for(i=0; i<line_number; i++)
			{
				f_grams.printf( "%.8f", db[i][0]);
				for(j=1; j<dim; j++)
				{
					f_grams.printf( "|%.8f", db[i][j]);
				}
				f_grams.printf("\n");
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
