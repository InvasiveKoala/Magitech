package com.github.invasivekoala.magitech.incantations;


import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.server.level.ServerPlayer;


// Passed to VerbWord#effect for context about the rest of the sentence
public class SentenceContext {

    public SentenceContext(ServerPlayer p){
        playerCaster = p;
    }
    public ServerPlayer playerCaster;
    public VerbWord verb;
    public NounInstance subject;
    public NounInstance object;



}
