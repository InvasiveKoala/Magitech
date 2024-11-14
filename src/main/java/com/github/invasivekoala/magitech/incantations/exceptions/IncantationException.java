package com.github.invasivekoala.magitech.incantations.exceptions;

import net.minecraft.network.chat.TranslatableComponent;

public class IncantationException extends Exception {

    public static final String WRONG_NOUN = "wrong_noun_type";
    public static final String NO_TARGET_FOUND = "no_target_found";
    public static final String OVER_ENTITY_LIMIT = "over_entity_limit";
    public static final String NO_VALID_MEMORY = "no_valid_memory";
    public static final String NOT_SPECIFIC_ENOUGH = "not_specific_enough";
    public static final String NOT_CONJURABLE = "not_conjurable";

    int word;
    private final String str;
    public IncantationException(int word, String translation){
        this.word=word;
        this.str = translation;
    }
    public TranslatableComponent translationName(){ return new TranslatableComponent("exception.magitech."+str);}
}
