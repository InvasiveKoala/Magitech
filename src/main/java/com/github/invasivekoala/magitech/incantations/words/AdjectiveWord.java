package com.github.invasivekoala.magitech.incantations.words;



public abstract class AdjectiveWord extends Word {

    public AdjectiveWord() {
        super(Types.ANY);
    }

    public int adjectivePriority() {return 1;}

}
