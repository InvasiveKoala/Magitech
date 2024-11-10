package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IBlockAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IEntityAdjective;
import com.github.invasivekoala.magitech.incantations.words.nouns.PlayerNoun;

import java.util.ArrayList;
import java.util.List;

public class NounInstance<T extends NounWord> {

    public final T wordSingleton;
    public NounInstance(T word){
        wordSingleton = word;
    }
    public List<AdjectiveWord> adjectives;


    public boolean isEntity() { return wordSingleton.classification.contains(WordRegistry.Types.ENTITY);}
    public boolean isBlock() { return wordSingleton.classification.contains(WordRegistry.Types.BLOCK);}
    // This is only run if the noun is a specific thing in the world -- otherwise, if it's a mob type, like (Pig) you would use #getType
    @SuppressWarnings("unchecked") // we know its safe
    public <G> List<G> getThing(SentenceContext cxt){
        // If the noun is just "me" assume they mean the player caster
        if (adjectives.isEmpty()){
            if (wordSingleton instanceof PlayerNoun) return (List<G>) List.of(cxt.playerCaster);
            return null; // TODO Error! Not specific enough.
        }

        boolean isSpecificEnough = false;

        List<G> toReturn = new ArrayList<>();

        List<AdjectiveWord> tempAdjectives = new ArrayList<>(adjectives);
        // First look through the adjective list for adjectives that should occur first (ie. primary adjectives) and remove them from the list.
        for (AdjectiveWord adjective : adjectives){
            if (!adjective.isPrimaryAdjective()) continue;
            isSpecificEnough = true;
            if (this.isEntity()){
                if (!(adjective instanceof IEntityAdjective ea)) return null; // TODO Error! Adjective incompatible
                toReturn = ea.narrowEntityDown(toReturn, cxt, wordSingleton);
            }
            else if (this.isBlock()){
                if (!(adjective instanceof IBlockAdjective ea)) return null; // TODO Error! Adjective incompatible
                toReturn = ea.narrowBlockDown(toReturn, cxt, wordSingleton);
            }
            tempAdjectives.remove(adjective);
        }
        if (!isSpecificEnough) return null; // TODO Error! Not specific enough


        // Then go through the rest.
        for (AdjectiveWord adjective : tempAdjectives){
            if (this.isEntity()){
                toReturn = ((IEntityAdjective) adjective).narrowEntityDown(toReturn, cxt, wordSingleton);
            } else if (this.isBlock()){
                toReturn = ((IBlockAdjective) adjective).narrowBlockDown(toReturn, cxt, wordSingleton);
            }
        }
        return toReturn;
    }



}
