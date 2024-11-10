package com.github.invasivekoala.magitech.incantations.words;


import com.github.invasivekoala.magitech.incantations.SentenceContext;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class VerbWord extends Word{

    public VerbWord(String id) {
        super(id);
    }

    public abstract boolean effect(SentenceContext context);

    public boolean hasSubject(){
        return false;
    }
    public boolean hasObject(){
        return true;
    }

    public boolean subjectIsValid(SentenceContext cxt){
        return false;
    }
    public boolean objectIsValid(SentenceContext cxt){
        return false;
    }

    public TranslatableComponent errorSubjectInvalid(SentenceContext cxt){
        return new TranslatableComponent("error.magitech."+translated()+"_subject");
    }
    public TranslatableComponent errorObjectInvalid(SentenceContext cxt){
        return new TranslatableComponent("error.magitech."+translated()+"_object");
    }

    // Whether the object of the sentence is a specific thing, or an abstract noun
    // Ex. "Attack Nearby pigs" is referring to specific things already in the world. "Summon Pig" is not something already in the world.
    public boolean specificObject(){
        return true;
    }
}
