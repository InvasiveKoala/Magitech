package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Magitech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WordEvents {
    @SubscribeEvent
    public static void onSpeak(ServerChatEvent event){
        String[] splitString = event.getMessage().split("\\s+");
        if (splitString.length <= 1) return;

        if (WordRegistry.VERBS.containsKey(splitString[0].toLowerCase())){
            // If the first word is a verb, begin parsing
            parseSpell(event.getPlayer(), List.of(splitString));
        }
    }

    public static void parseSpell(ServerPlayer player, List<String> words){
        SentenceContext cxt = new SentenceContext(player);
        VerbWord verb = WordRegistry.VERBS.get(words.get(0).toLowerCase());
        cxt.verb = verb;
        boolean expectsSubject = verb.hasSubject();
        boolean expectsObject = verb.hasObject();

        int index = 0;

        List<AdjectiveWord> tempAdjectiveList = new ArrayList<>();
        while (expectsSubject || expectsObject){
            index++;
            Word currentWord = getWord(words, index);
            if (currentWord == null) break; // TODO Error! Not a word

            // If it's an adverb
            // If it's an adjective
            if (currentWord instanceof AdjectiveWord adj){
                if (tempAdjectiveList.contains(adj)) continue; // TODO error! repeated adjective
                tempAdjectiveList.add(adj);
            }
            // If it's a noun
            else if (currentWord instanceof NounWord noun){
                NounInstance<NounWord> winst = new NounInstance<>(noun);
                winst.adjectives = new ArrayList<>(tempAdjectiveList);
                tempAdjectiveList.clear();

                // If it's the subject
                if (expectsSubject){
                    cxt.subject = winst;
                    expectsSubject = false;
                } else if (expectsObject){
                    cxt.object = winst;
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
