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
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class NounInstance {

    public final NounWord<?> wordSingleton;
    public final byte wordNumber;
    public boolean isSubject;
    public int manaCost = 0;
    public NounInstance(NounWord<?> word, byte number){
        wordSingleton = word;
        wordNumber = number;
    }
    public static CompoundTag toNbt(NounInstance noun){
        CompoundTag tag = new CompoundTag();
        tag.put("noun", StringTag.valueOf(noun.wordSingleton.registryName));

        ListTag adjTag = new ListTag();
        for (AdjectiveWord adj : noun.adjectives){
            adjTag.add(StringTag.valueOf(adj.registryName));
        }
        tag.put("adjectives", adjTag);

        tag.put("wordIndex", ByteTag.valueOf(noun.wordNumber));
        return tag;
    }
    public static NounInstance fromNbt(CompoundTag tag){ // Im too lazy to add checks for this so if you use it wrong you're just stupid
        String nounId = tag.getString("noun");
        NounWord<?> nounWord = (NounWord<?>) WordRegistry.WORDS.get(nounId);

        byte index = tag.getByte("wordIndex");

        NounInstance noun = new NounInstance(nounWord,index);

        ListTag adjTag = (ListTag) tag.get("adjectives");
        if (adjTag == null) return noun;

        Set<AdjectiveWord> adjectives = new HashSet<>();
        for (Tag aTag : adjTag){
            adjectives.add((AdjectiveWord) WordRegistry.WORDS.get(aTag.getAsString()));
        }
        noun.adjectives = adjectives;
        return noun;
    }


    // Set b/c ordering doesn't matter here -- we use a priority system in the adjective class
    // (getThing() should prob return a set as well but I think .get() is used somewhere? idk.)
    public Set<AdjectiveWord> adjectives;




    public boolean isEntity() { return wordSingleton.classification == Word.Types.ENTITY;}
    public boolean isBlock() { return wordSingleton.classification == Word.Types.BLOCK;}
    public boolean isDirection() { return wordSingleton.classification == Word.Types.DIRECTION;}


    // This is only run if the noun is a specific thing in the world -- otherwise, if it's a mob type, like (Pig) you would use #getType
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
            if (wordSingleton.registryName.equals("me")) return (List<G>) List.of(cxt.playerCaster);
            else if (wordSingleton.registryName.equals("origo")) return (List<G>) List.of(getSpellOrigin(cxt.playerCaster));

            throw new IncantationException(wordNumber, IncantationException.NOT_SPECIFIC_ENOUGH);
        }

        return getThingGeneral(cxt, new ArrayList<>());
    }

    private <G> List<G> getThingGeneral(SentenceContext cxt, List<G> toReturn) throws IncantationException {

        HashMap<Integer, Set<AdjectiveWord>> adjMap = new HashMap<>();
        // First sort the adjectives based on priority.
        for (AdjectiveWord adjective : adjectives){
            int priority = adjective.adjectivePriority();
            if (adjMap.get(priority) == null){
                Set<AdjectiveWord> newList = new HashSet<>();
                adjMap.put(priority, newList);
            }
            adjMap.get(priority).add(adjective);
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
            if (!(adjective instanceof IEntityAdjective adj)) return toReturn;
            toReturn = (List<G>) adj.narrowEntityDown((List<Entity>) toReturn, cxt, (GenericEntityNoun<Entity>) wordSingleton);
        }
        else if (this.isBlock()){
            if (!(adjective instanceof IBlockAdjective adj)) return toReturn;
            toReturn = (List<G>) adj.narrowBlockDown((List<BlockPos>) toReturn, cxt, (NounWord<BlockPos>)wordSingleton);
        }
        else if (this.isDirection()){
            if (!(adjective instanceof IDirectionAdjective adj)) return toReturn;
            toReturn = (List<G>) adj.narrowItDown((List<Vec3>) toReturn, cxt, (NounWord<Vec3>)wordSingleton);
        }
        return toReturn;
    }

    public BlockPos getSpellOrigin(ServerPlayer player) throws IncantationException {
        if (!WordEvents.onGoingSpells.containsKey(player.getUUID())) throw new IncantationException(wordNumber, IncantationException.NO_SPELL_ORIGIN);
        return WordEvents.onGoingSpells.get(player.getUUID()).origin;
    }



}
