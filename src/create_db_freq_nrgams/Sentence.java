package create_db_freq_nrgams;

import java.util.ArrayList;

import edu.stanford.nlp.ling.TaggedWord;

public class Sentence {
	
	ArrayList<TaggedWord> words;

	public Sentence()
	{
		words = new ArrayList<TaggedWord>();
	}
	
	void addWord(TaggedWord w)
	{
		words.add(w);
	}
	
	int getSize()
	{
		return words.size();
	}
	

	ArrayList<Ngrams> get_ngrams(int n)
	{
		ArrayList<Ngrams> grams = new ArrayList<Ngrams>();
		Ngrams temp = new Ngrams();
		int i;
		
		if(getSize() < n)
			return grams;
		
		i = 0;
		while(true)
		{
			for(int j=i; j<(n+i); j++)
			{
				temp.addElement(words.get(j));
			}
			grams.add(temp);
			temp = new Ngrams();
			
			if(i == getSize()-n)
				break;
			i++;
		}
		
		return grams;
	}
	
	int get_Count_ngrams(int n)
	{
		ArrayList<Ngrams> grams = this.get_ngrams(n);
		return grams.size();
	}
	
	@Override
	public String toString() 
	{
		String s = "";
		
		for(TaggedWord pos : words){
			 s += pos.word() + "(" + pos.tag() + ")" + " ";
		}
		return s;
	}

}
