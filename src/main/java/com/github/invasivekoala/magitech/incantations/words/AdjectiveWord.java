package com.github.invasivekoala.magitech.incantations.words;



public abstract class AdjectiveWord extends Word {

    public AdjectiveWord(String id, Word.Types t) {
        super(id, t);
    }

    public int adjectivePriority() {return 0;}

}
