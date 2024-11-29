package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HexiconTabRegistry {
    // This is done like this so addon developers have an easier time adding tabs (if this ever gets addons)

    private static final String INTRO_TAB = "introduction";

    public static Map<String, HexiconTabInfo> TABS = new HashMap<>();

    public static Map<String, HexiconEntryInfo> ENTRIES = new HashMap<>();
    public static Map<String, List<HexiconEntryInfo>> ENTRIES_BY_TAB = new HashMap<>();

    public static void init()
    {
        // All of this should definitely be json, but idk how to do that

        registerTab(HexiconTabInfo.Builder.neww("introduction")
                        .build());
        registerTab(HexiconTabInfo.Builder.neww("incantations")
                        .build());

        // i shouldve just used modonomicon
        registerEntry(HexiconEntryInfo.Builder.named("intro_witchcraft")
                .addTextPage("intro_witchcraft_page1")
                .addCraftingPage(new ResourceLocation(Magitech.MOD_ID, "hexicon"))
                .addCraftingPage(new ResourceLocation("campfire"))
                .addConnection(ButtonConnection.Builder.dir("intro_shadow", ButtonConnection.RIGHT).addPiece(ButtonConnection.Piece.VERT_PIPE).addPiece(ButtonConnection.Piece.RIGHT_ARROW).build())
                .icon(ItemRegistry.HEXICON.get().getDefaultInstance())
                .isRoot(0,0)
                .build(INTRO_TAB));

        registerEntry(HexiconEntryInfo.Builder.named("intro_shadow")
                .addTextPage("intro_shadow_page1")
                .addCraftingPage(new ResourceLocation("torch"))
                .addConnection(ButtonConnection.Builder.dir("intro_crystallization", ButtonConnection.DOWN).addPiece(ButtonConnection.Piece.HOR_PIPE).addPiece(ButtonConnection.Piece.DOWN_ARROW).build())
                .icon(ItemRegistry.RAW_SHADOW.get().getDefaultInstance())
                .build(INTRO_TAB));
        registerEntry(HexiconEntryInfo.Builder.named("intro_crystallization")
                .addTextPage("intro_shadow_page1")
                .icon(ItemRegistry.CRYSTALLIZED_SHADOW.get().getDefaultInstance())
                .build(INTRO_TAB));


    }

    private static void registerTab(HexiconTabInfo info){
        TABS.put(info.id, info);
    }
    private static void registerEntry(HexiconEntryInfo info){
        ENTRIES.put(info.id, info);
        ENTRIES_BY_TAB.putIfAbsent(info.tab, new ArrayList<>());
        ENTRIES_BY_TAB.get(info.tab).add(info);
    }
}
