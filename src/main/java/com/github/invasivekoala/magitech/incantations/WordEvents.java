package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Magitech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WordEvents {



    @SubscribeEvent
    public static void onSpeak(ServerChatEvent event){
        String[] splitString = event.getMessage().split("\\s+");
        if (splitString.length <= 1) return;

        if (WordRegistry.VERBS.containsKey(splitString[0].toLowerCase())){
            // If the first word is a verb, begin parsing
            try {
                parseSpell(event.getPlayer(), List.of(splitString));
            }
            catch(IncantationException e){
                e.printStackTrace();
            }
        }
    }

    public static void parseSpell(ServerPlayer player, List<String> words) throws IncantationException {
        SentenceContext cxt = new SentenceContext(player);
        VerbWord verb = WordRegistry.VERBS.get(words.get(0).toLowerCase());
        cxt.verb = verb;
        boolean expectsSubject = verb.hasSubject();
        boolean expectsObject = verb.hasObject();

        int index = 0;

        Set<AdjectiveWord> tempAdjectiveList = new HashSet<>();
        while (expectsSubject || expectsObject){
            index++;
            Word currentWord = getWord(words, index); // TODO catch out of bounds error
            if (currentWord == null) break; // TODO Error! Not a word

            // If it's an adverb
            // If it's an adjective
            if (currentWord instanceof AdjectiveWord adj){
                //if (tempAdjectiveList.contains(adj)) continue; // We need repeated adjectives
                tempAdjectiveList.add(adj);
            }
            // If it's a noun
            else if (currentWord instanceof NounWord noun){
                NounInstance winst = new NounInstance(noun, index+1);
                winst.adjectives = new HashSet<>(tempAdjectiveList);
                tempAdjectiveList.clear();

                // If it's the subject
                if (expectsSubject){
                    if (!Word.compatible(verb.subjectTypes(), noun)) return; // TODO error! wrong noun type for subject
                    cxt.subject = winst;
                    winst.isSubject = true;
                    expectsSubject = false;
                } else if (expectsObject){
                    if (!Word.compatible(verb.objectTypes(), noun)) return; // TODO error! wrong noun type for object
                    cxt.object = winst;
                    winst.isSubject = false;
                    expectsObject = false;
                } else {
                    return;
                    // TODO Error! too many words.
                }
            }
        }
        verb.effect(cxt);

    }
    private static Word getWord(List<String> words, int index){
        return WordRegistry.WORDS.get(words.get(index));
    }
}
