package same_author;

import java.util.ArrayList;

import edu.stanford.nlp.ling.TaggedWord;

public class Ngrams 
{
	
	ArrayList<TaggedWord> words;
	int count;
	float freq;

	public Ngrams()
	{
		words = new ArrayList<TaggedWord>();
		count = 0;
		freq = 0;
	}
	
	public Ngrams(String[] ngrams)
	{
		words = new ArrayList<TaggedWord>();
		TaggedWord tw;
		
		for(String s : ngrams)
		{
			tw = new TaggedWord("");
			tw.setTag(s);
			words.add(tw);
		}
		
		count = 0;
		freq = 0.0f;
	}
	
	void addElement(TaggedWord w)
	{
		words.add(w);
	}
	
	int getSize()
	{
		return words.size();
	}
	
	void incrementCount()
	{
		count++;
	}

	@Override
	public String toString() 
	{
		String s = "";
		
		for(TaggedWord pos : words){
			 s += pos.tag() + " ";
		}
		
		s += count;
		return s;
	}

	@Override
	public boolean equals(Object other) 
	{
		
		for(int i = 0; i < words.size(); i++){
			if(!words.get(i).tag().equals(((Ngrams)other).words.get(i).tag())){
				return false;
			}
		}
		
		return true;
	}
	
	

}
