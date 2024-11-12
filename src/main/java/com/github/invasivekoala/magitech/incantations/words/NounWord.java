package com.github.invasivekoala.magitech.incantations.words;

import com.github.invasivekoala.magitech.incantations.SentenceContext;


public abstract class NounWord<T> extends Word {

    public NounWord(String id, Word.Types t) {
        super(id, t);
    }

    public Class<T> nounClass(SentenceContext context){
        return null;
    }


}
