package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IBlockAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IDirectionAdjective;
import com.github.invasivekoala.magitech.incantations.words.adjectives.IEntityAdjective;
import com.github.invasivekoala.magitech.incantations.words.nouns.DirectionNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.StackNoun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class NounInstance {

    public final NounWord<?> wordSingleton;
    public final int wordNumber;
    public boolean isSubject;
    public int manaCost = 0;
    public NounInstance(NounWord<?> word, int number){
        wordSingleton = word;
        wordNumber = number;
    }

    public Set<AdjectiveWord> adjectives;




    public boolean isEntity() { return wordSingleton.classification == Word.Types.ENTITY;}
    public boolean isBlock() { return wordSingleton.classification == Word.Types.BLOCK;}
    public boolean isDirection() { return wordSingleton.classification == Word.Types.DIRECTION;}


    // This is only run if the noun is a specific thing in the world -- otherwise, if it's a mob type, like (Pig) you would use #getType
    @SuppressWarnings("unchecked") // we know its safe
    public <G> List<G> getThing(SentenceContext cxt) throws IncantationException {
        return getThing(cxt, null);
    }
    @SuppressWarnings("unchecked")
    public <G> List<G> getThing(SentenceContext cxt, @Nullable Entity target) throws IncantationException {
        if (wordSingleton instanceof StackNoun sn){
            Deque<Object> deq = MemoryUtil.getOrCreateMemory(cxt.playerCaster.getUUID());
            return (List<G>) getThingGeneral(cxt, sn.get(deq));
        } else if (wordSingleton instanceof DirectionNoun n){
            return (List<G>) getThingDirection(cxt, target);
        }
        else return getThingNonMemory(cxt);

    }

    // Directions are special in that they need access to the object for parsing.
    // (b/c of words like meceps which are dependent on target direction)
    public List<Vec3> getThingDirection(SentenceContext cxt, Entity target) throws IncantationException{
        return getThingGeneral(cxt, List.of(((DirectionNoun) wordSingleton).getDirection(cxt, target)));
    }

    @SuppressWarnings("unchecked")
    private <G> List <G> getThingNonMemory(SentenceContext cxt) throws IncantationException{
        // If the noun is just "me" assume they mean the player caster
        if (adjectives.isEmpty()){
            if (wordSingleton.registryName.equals("player")) return (List<G>) List.of(cxt.playerCaster);
            throw new IncantationException(wordNumber, IncantationException.NOT_SPECIFIC_ENOUGH);
        }

        return getThingGeneral(cxt, new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    private <G> List<G> getThingGeneral(SentenceContext cxt, List<G> toReturn) throws IncantationException {

        HashMap<Integer, Set<AdjectiveWord>> adjMap = new HashMap<>();
        // First sort the adjectives based on priority.
        for (AdjectiveWord adjective : adjectives){
            int p = adjective.adjectivePriority();
            if (adjMap.get(p) == null){
                Set<AdjectiveWord> newList = new HashSet<>();
                adjMap.put(p, newList);
            }
            adjMap.get(p).add(adjective);
        }

        // Priority
        for(Integer i : adjMap.keySet()){
            // Adjectives within that priority
            for (AdjectiveWord adjective : adjMap.get(i)){
                toReturn = narrowListGivenAdjective(cxt, toReturn, adjective);
            }
        }
        if (toReturn == null) throw new IncantationException(wordNumber, IncantationException.NO_TARGET_FOUND); // Error! not specific enough
        return toReturn;
    }


    @SuppressWarnings("unchecked")
    private <G> List<G> narrowListGivenAdjective(SentenceContext cxt, List<G> toReturn, AdjectiveWord adjective) throws IncantationException {
        if (this.isEntity()){
            if (!(adjective instanceof IEntityAdjective ea)) return toReturn;
            toReturn = (List<G>) ea.narrowEntityDown((List<Entity>) toReturn, cxt, (GenericEntityNoun<Entity>) wordSingleton);
        }
        else if (this.isBlock()){
            if (!(adjective instanceof IBlockAdjective ea)) return toReturn;
            toReturn = (List<G>) ea.narrowBlockDown((List<BlockPos>) toReturn, cxt, (NounWord<BlockPos>)wordSingleton);
        }
        else if (this.isDirection()){
            if (!(adjective instanceof IDirectionAdjective ea)) return toReturn;
            toReturn = (List<G>) ea.narrowItDown((List<Vec3>) toReturn, cxt, (NounWord<Vec3>)wordSingleton);
        }
        return toReturn;
    }



}
