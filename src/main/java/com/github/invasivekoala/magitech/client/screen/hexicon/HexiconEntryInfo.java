package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.client.screen.hexicon.entries.CraftingRecipePage;
import com.github.invasivekoala.magitech.client.screen.hexicon.entries.HexiconPage;
import com.github.invasivekoala.magitech.client.screen.hexicon.entries.TextPage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HexiconEntryInfo extends Screen {


    public String id;
    public String tab;
    public List<HexiconPage> pages;
    public Set<ButtonConnection> connectsTo;
    public ItemStack icon;
    @Nullable
    public Vec2 pos;

    protected HexiconEntryInfo() {
        super(TextComponent.EMPTY);
    }

    public static class Builder{
        private final String entryId;
        private final List<HexiconPage> pages = new ArrayList<>();
        private Vec2 pos;
        private ItemStack icon;
        private final Set<ButtonConnection> connections = new HashSet<>();

        private Builder(String entryId){
            this.entryId = entryId;
        }
        public static HexiconEntryInfo.Builder named(String entryId){
            return new HexiconEntryInfo.Builder(entryId);
        }
        public HexiconEntryInfo.Builder addTextPage(String translateKey){
            pages.add(new TextPage(entryId, translateKey));
            return this;
        }
        public HexiconEntryInfo.Builder addCraftingPage(ResourceLocation recipe){
            pages.add(new CraftingRecipePage(entryId, recipe));
            return this;
        }
        public HexiconEntryInfo.Builder addConnection(ButtonConnection connection){
            connections.add(connection);
            return this;
        }
        public HexiconEntryInfo.Builder icon(ItemStack stack){
            icon = stack;
            return this;
        }
        /**
         * Sets the position IF there is no connection to it. Otherwise connections trump manual position
         */
        public HexiconEntryInfo.Builder isRoot(int x, int y){
            pos = new Vec2(x,y);
            return this;
        }
        public HexiconEntryInfo build(String tab){
            HexiconEntryInfo ret = new HexiconEntryInfo();
            ret.id = this.entryId;
            ret.pages = this.pages;
            ret.tab = tab;
            ret.pos = this.pos;
            ret.icon = this.icon;
            ret.connectsTo = this.connections;
            return ret;
        }

    }
}
