package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import com.github.invasivekoala.magitech.incantations.words.adjectives.NearbyAdjective;
import com.github.invasivekoala.magitech.incantations.words.nouns.PlayerNoun;
import com.github.invasivekoala.magitech.incantations.words.verbs.WiltVerb;
import java.util.HashMap;

public class WordRegistry {

    // Word classification (for nouns and adjectives)
    public enum Types{
        ENTITY,
        BLOCK,
    }

    public static final HashMap<String, Word> WORDS = new HashMap<>();
    public static final HashMap<String, VerbWord> VERBS = new HashMap<>();

    private static void register(String wordId, Word word){
        WORDS.put(wordId, word);
    }
    private static void registerVerb(String wordId, VerbWord word){
        register(wordId, word);
        VERBS.put(wordId, word);
    }

    static{
        registerVerb("mori", new WiltVerb("wilt"));
        register("me", new PlayerNoun("player", Types.ENTITY));
        register("prope", new NearbyAdjective("nearby", Types.ENTITY, Types.BLOCK));
    }


}
