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
			String f4 = "input/4.txt";
			
			String text_train = "";
			List<CoreLabel> rawWords_train;
			ArrayList<TaggedWord> wordsList_train;
			ArrayList<Sentence> sentenceList_train = new ArrayList<Sentence>();
			
			text_train += readFile(f1);
			text_train += readFile(f2);
			text_train += readFile(f3);
			text_train += readFile(f4);
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
				for(Ngrams ngram : sen.get_ngrams(5))
				{
					if(grams5.contains(ngram))
					{
						continue;
					}
					grams5.add(ngram);
				}
			}
						
			//write to file of 5 grams
			PrintWriter f_5grams = new PrintWriter("db/5grams.txt", "UTF-8");
			
			//write to file of 5 grams
			for(Ngrams ngram : grams5)
			{
				f_5grams.println(ngram);
				//System.out.println(ngram);
			}
			
			f_5grams.close();
			return;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void outputNgramsFreq(ArrayList<Ngrams> ngrams)
	{
		try
		{
			//write to file of 2 grams
			PrintWriter f_2grams = new PrintWriter("output/2gramsFreq.txt", "UTF-8");
			
			//write to file of 2 grams
			for(Ngrams ngram : ngrams)
			{
				f_2grams.printf(ngram + " %.8f\n", ngram.freq);
			}
			
			f_2grams.close();
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
