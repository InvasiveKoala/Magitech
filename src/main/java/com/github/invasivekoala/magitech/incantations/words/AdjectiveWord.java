package com.github.invasivekoala.magitech.incantations.words;



public abstract class AdjectiveWord extends Word {

    public AdjectiveWord(String id) {
        super(id, Types.ANY);
    }

    public int adjectivePriority() {return 0;}

}
