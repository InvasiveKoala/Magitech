package com.github.invasivekoala.magitech.incantations.words;


import com.github.invasivekoala.magitech.incantations.WordRegistry;

public abstract class AdjectiveWord extends Word {

    public AdjectiveWord(String id, WordRegistry.Types... t) {
        super(id, t);
    }

    public boolean isPrimaryAdjective() {return false;}

}
