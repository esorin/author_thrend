package same_author;

import java.util.ArrayList;

import edu.stanford.nlp.ling.TaggedWord;

public class Ngrams {
	
	ArrayList<TaggedWord> words;

	public Ngrams(){
		words = new ArrayList<TaggedWord>();
	}
	
	void addWord(TaggedWord w){
		words.add(w);
	}
	
	int getSize(){
		return words.size();
	}

	@Override
	public String toString() {
		String s = "";
		
		for(TaggedWord pos : words){
			 s += pos.tag() + " ";
		}
		return s;
	}

	@Override
	public boolean equals(Object other) {
		
		for(int i = 0; i < words.size(); i++){
			if(!words.get(i).tag().equals(((Ngrams)other).words.get(i).tag())){
				return false;
			}
		}
		
		return true;
	}
	
	

}
