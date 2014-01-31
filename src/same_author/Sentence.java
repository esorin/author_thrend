package same_author;

import java.util.ArrayList;

import edu.stanford.nlp.ling.TaggedWord;

public class Sentence {
	
	ArrayList<TaggedWord> words;

	public Sentence()
	{
		words = new ArrayList<TaggedWord>();
	}
	
	void addWord(TaggedWord w){
		words.add(w);
	}
	
	int getSize(){
		return words.size();
	}
	
	ArrayList<ArrayList<TaggedWord>> get_2_grams()
	{
		ArrayList<ArrayList<TaggedWord>> grams = new ArrayList<ArrayList<TaggedWord>>();
		ArrayList<TaggedWord> temp = new ArrayList<TaggedWord>();
		int i;
		
		if(getSize() <= 1)
			return grams;
		
		i = 0;
		while(true)
		{
			temp.add(words.get(i));
			temp.add(words.get(i+1));
			grams.add(temp);
			
			if(i == getSize()-2)
				break;
			i++;
		}
		
		return grams;
	}

}
