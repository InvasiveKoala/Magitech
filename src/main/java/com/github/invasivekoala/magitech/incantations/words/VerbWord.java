package com.github.invasivekoala.magitech.incantations.words;


import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;

import java.util.List;

public abstract class VerbWord extends Word{

    public VerbWord() {
        super();
    }

    public abstract boolean effect(SentenceContext context) throws IncantationException;

    public boolean hasSubject(){
        return false;
    }
    public boolean hasObject(){
        return true;
    }

    public List<Types> subjectTypes(){
        return List.of(Types.ENTITY);
    }
    public List<Types> objectTypes(){
        return List.of(Types.ENTITY);
    }

    public int subjectLimit(){ return 999;}
    public int objectLimit(){ return 999;}



}
