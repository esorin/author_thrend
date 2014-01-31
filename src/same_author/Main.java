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
									
			//text 1
			file1 = "in.txt";
			text1Str = readFile(file1);
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
		    for(Sentence sen : sentenceList1){
		    	
		    }
		    for(Sentence sen : sentenceList1)
		    {
		    	//2-grame
		    	for(ArrayList n2grams : sen.get_2_grams())
		    	{
		    		for(Sentence sen2 : sentenceList1)
		    		{
		    			for(ArrayList n2grams_all : sen2.get_2_grams())
		    			{
		    				if(((TaggedWord)n2grams.get(0)).tag().equals(((TaggedWord)n2grams_all.get(0)).tag()) &&
		    				   ((TaggedWord)n2grams.get(1)).tag().equals(((TaggedWord)n2grams_all.get(1)).tag()))
		    				{
		    					
		    				}
		    			}
		    		}
		    	}
		    }
		    
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
