package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class HexiconTabRegistry {
    // This is done like this so addon developers have an easier time adding tabs (if this ever gets addons)

    public static Map<String, HexiconTabInfo> TABS = new HashMap<>();

    public static void init()
    {
        int i = 24; // Equivalent to one button's space
        register(
                HexiconTabInfo.Builder.neww("introduction")
                        .addEntry("intro_witchcraft", 0,0, ItemRegistry.HEXICON.get().getDefaultInstance())
                        .addEntry("intro_shadow", i*3, 0, ItemRegistry.RAW_SHADOW.get().getDefaultInstance())
                        .addEntry("intro_crystallization", i*3, i*2, ItemRegistry.CRYSTALLIZED_SHADOW.get().getDefaultInstance())
                        .build());
        register(
                HexiconTabInfo.Builder.neww("incantations")
                        .addEntry("inc_basics", 0,0, Items.DIAMOND.getDefaultInstance())
                        .addEntry("inc_shadow", i*3, 0, Items.CONDUIT.getDefaultInstance())
                        .build());

    }

    private static void register(HexiconTabInfo info){
        TABS.put(info.id, info);
    }
}
