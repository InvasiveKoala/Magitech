package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IBlockAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IEntityAdjective;
import com.github.invasivekoala.magitech.incantations.words.nouns.StackNoun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

public class NounInstance {

    public final NounWord<?> wordSingleton;
    public final int wordNumber;
    public NounInstance(NounWord<?> word, int number){
        wordSingleton = word;
        wordNumber = number;
    }
    public List<AdjectiveWord> adjectives;


    public boolean isEntity() { return wordSingleton.classification == Word.Types.ENTITY;}
    public boolean isBlock() { return wordSingleton.classification == Word.Types.BLOCK;}


    // This is only run if the noun is a specific thing in the world -- otherwise, if it's a mob type, like (Pig) you would use #getType
    @SuppressWarnings("unchecked") // we know its safe
    public <G> List<G> getThing(SentenceContext cxt) throws IncantationException {
        if (wordSingleton instanceof StackNoun sn){
            Deque<Object> deq = MemoryUtil.getOrCreateMemory(cxt.playerCaster.getUUID());
            return (List<G>) getThingGeneral(cxt, sn.get(deq));
        } else return getThingNonMemory(cxt);

    }

    private <G> List <G> getThingNonMemory(SentenceContext cxt) throws IncantationException{
        // If the noun is just "me" assume they mean the player caster
        if (adjectives.isEmpty()){
            if (wordSingleton.registryName.equals("player")) return (List<G>) List.of(cxt.playerCaster);
            return null; // TODO Error! Not specific enough.
        }

        return getThingGeneral(cxt, new ArrayList<>());
    }

    private <G> List<G> getThingGeneral(SentenceContext cxt, List<G> toReturn) throws IncantationException {
        if (adjectives.isEmpty()) return toReturn;

        HashMap<Integer, List<AdjectiveWord>> adjMap = new HashMap<>();
        // First sort the adjectives based on priority.
        for (AdjectiveWord adjective : adjectives){
            int p = adjective.adjectivePriority();
            if (adjMap.get(p) == null){
                List<AdjectiveWord> newList = new ArrayList<>();
                adjMap.put(p, newList);
            }
            adjMap.get(p).add(adjective);
        }

        // Priority
        for(Integer i : adjMap.keySet()){
            // Adjectives within that priority
            for (AdjectiveWord adjective : adjMap.get(i)){
                if (this.isEntity()){
                    if (!(adjective instanceof IEntityAdjective ea)) return toReturn; // TODO Error! Adjective incompatible
                    toReturn = (List<G>) ea.narrowEntityDown((List<Entity>) toReturn, cxt, (NounWord<Entity>) wordSingleton);
                }
                else if (this.isBlock()){
                    if (!(adjective instanceof IBlockAdjective ea)) return toReturn; // TODO Error! Adjective incompatible
                    toReturn = (List<G>) ea.narrowBlockDown((List<BlockPos>) toReturn, cxt, (NounWord<BlockPos>)wordSingleton);
                }
            }
        }
        // if (toReturn == null) TODO Error! not specific enough
        return toReturn;
    }



}
