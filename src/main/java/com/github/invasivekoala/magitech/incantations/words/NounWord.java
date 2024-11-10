package com.github.invasivekoala.magitech.incantations.words;

import com.github.invasivekoala.magitech.incantations.WordRegistry;

public abstract class NounWord<T> extends Word {

    public NounWord(String id, WordRegistry.Types... t) {
        super(id, t);
    }

    public abstract Class<T> nounClass();

}
