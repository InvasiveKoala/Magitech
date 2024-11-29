package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = Magitech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WordEvents {


    public static HashMap<UUID, MultiLineSpell> onGoingSpells = new HashMap<>();

    @SubscribeEvent
    public static void onSpeak(ServerChatEvent event){
        String[] splitString = event.getMessage().split("\\s+");
        if (splitString.length <= 1) return;

        if (WordRegistry.VERBS.containsKey(splitString[0].toLowerCase())){
            // If the first word is a verb, begin parsing
            try {
                SentenceContext context = parseSpell(event.getPlayer(), List.of(splitString));
                context.effect();
            }
            catch(IncantationException e){
                // TODO tell the player the exception
                e.printStackTrace();
            }
        }
    }
    // Temporary
    @SubscribeEvent
    public static void onRightclickBook(PlayerInteractEvent.RightClickItem event){
        if (event.getSide() == LogicalSide.CLIENT) return;
        if (!event.getItemStack().is(Items.WRITTEN_BOOK)) return;
        if (event.getPlayer().isShiftKeyDown()) return;
        if (onGoingSpells.containsKey(event.getPlayer().getUUID())){
            event.setCanceled(true);
            return;
        }

        List<String> pages = new ArrayList<>();

        BookViewScreen.loadPages(event.getItemStack().getOrCreateTag(), pages::add);

        String page = pages.get(0);

        String strippedPage = Component.Serializer.fromJsonLenient(page).getContents();
        String[] lines = strippedPage.split("\\R+");

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        List<SentenceContext> spells = new ArrayList<>();


        for (String line : lines){

            line = line.replaceAll("#(.*)", ""); // comments, removes anything after a #

            String[] splitString = line.split("\\s+");
            try {
                SentenceContext context = parseSpell(player, List.of(splitString));
                spells.add(context);
            } catch (IncantationException e) {
                e.printStackTrace();
            }
        }
        MultiLineSpell spell = new MultiLineSpell(spells);
        onGoingSpells.put(player.getUUID(), spell);

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
    public static void castMultiLine(ServerPlayer p, MultiLineSpell spell){
        if (onGoingSpells.containsKey(p.getUUID())) return; // We don't want the player to have multiple spells going on at the same time
        onGoingSpells.put(p.getUUID(), spell);
    }

    @SubscribeEvent
    public static void handleMultilineSpells(TickEvent.PlayerTickEvent event){
        if (event.side == LogicalSide.CLIENT) return;

        UUID uuid = event.player.getUUID();
        if (!onGoingSpells.containsKey(uuid)) return;

        MultiLineSpell spell = onGoingSpells.get(uuid);
        if (spell.lines.isEmpty()){
            onGoingSpells.remove(uuid);
            return;
        }

        try {
            spell.tick();
        } catch (IncantationException e) {
            e.printStackTrace(); // TODO handle exception
        }
    }



    public static SentenceContext parseSpell(ServerPlayer player, List<String> words) throws IncantationException {
        SentenceContext cxt = new SentenceContext(player);
        VerbWord verb = WordRegistry.VERBS.get(words.get(0).toLowerCase());
        cxt.verb = verb;
        boolean expectsSubject = verb.hasSubject();
        boolean expectsObject = verb.hasObject();

        byte index = 0;

        Set<AdjectiveWord> tempAdjectiveList = new HashSet<>();
        while (expectsSubject || expectsObject){
            index++;
            Word currentWord = getWord(words, index); // TODO catch out of bounds error
            if (currentWord == null) throw new IncantationException(index, IncantationException.NOT_A_WORD);

            // If it's an adverb
            // If it's an adjective
            if (currentWord instanceof AdjectiveWord adj){
                //if (tempAdjectiveList.contains(adj)) continue; // We need repeated adjectives
                tempAdjectiveList.add(adj);
            }
            // If it's a noun
            else if (currentWord instanceof NounWord noun){
                NounInstance winst = new NounInstance(noun, (byte) (index+ 1));
                winst.adjectives = new HashSet<>(tempAdjectiveList);
                tempAdjectiveList.clear();

                // If it's the subject
                if (expectsSubject){
                    if (!Word.compatible(verb.subjectTypes(), noun)) throw new IncantationException(index, IncantationException.WRONG_NOUN);
                    cxt.subject = winst;
                    winst.isSubject = true;
                    expectsSubject = false;
                } else if (expectsObject){
                    if (!Word.compatible(verb.objectTypes(), noun)) throw new IncantationException(index, IncantationException.WRONG_NOUN);
                    cxt.object = winst;
                    expectsObject = false;
                } else { // This should theoretically never run!
                    throw new IncantationException(index, IncantationException.TOO_MANY_WORDS);
                }
            }
        }
        return cxt;

    }
    private static Word getWord(List<String> words, int index){
        return WordRegistry.WORDS.get(words.get(index));
    }
}
