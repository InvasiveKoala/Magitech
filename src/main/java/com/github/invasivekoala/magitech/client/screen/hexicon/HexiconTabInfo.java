package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.client.screen.hexicon.button.RelativePositionButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

// I was thinking of using JSON for this, but I'm not good at JSON and there probably isn't enough of a reason to do it with JSON.
// EDIT 11/15/24: Probably using JSON for individual entries, and code for tabs. Hopefully I'll learn enough in the future to make the full switch to JSON.
public class HexiconTabInfo {
    // We don't actually render anything here

    public String id;
    public Set<RelativePositionButton> widgets;


    public void addWidgets(HexiconScreen screen){
        screen.addWidgetsWithinTab(widgets);
    }

    
    public static class Builder{
        private Set<RelativePositionButton> widgets;
        private String tabId;

        private Builder(String tabId){
            widgets = new HashSet<>();
            this.tabId = tabId;
        }
        public static Builder neww(String tabId){
            return new Builder(tabId);
        }
        public Builder addEntry(String entryId, int x, int y, ItemStack icon){
            widgets.add(new RelativePositionButton(entryId, x, y, icon));
            return this;
        }
        /*public Builder addEntry(String entryId, int x, int y, ItemStack icon, String... connections){
            widgets.add(new RelativePositionButton(entryId, x, y, icon));
            return this;
        }*/
        public HexiconTabInfo build(){
            HexiconTabInfo ret = new HexiconTabInfo();
            ret.id = this.tabId;
            ret.widgets = this.widgets;
            return ret;
        }

    }

}
