package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.client.screen.hexicon.button.RelativePositionButton;

// I was thinking of using JSON for this, but I'm not good at JSON and there probably isn't enough of a reason to do it with JSON.
public class HexiconTabInfo {

    public String id;


    public void addWidgets(HexiconScreen screen){
        for (HexiconEntryInfo entry : HexiconTabRegistry.ENTRIES_BY_TAB.get(id)){

            if (entry.pos == null) continue;

            RelativePositionButton button = new RelativePositionButton(entry.id, (int) entry.pos.x, (int) entry.pos.y, entry.icon);
            screen.addWidgetWithinTab(button);

            for (ButtonConnection connection : entry.connectsTo){
                connection.instantiateConnectionButton(screen, button);
            }
        }
    }

    
    public static class Builder{
        private String tabId;

        private Builder(String tabId){
            this.tabId = tabId;
        }
        public static Builder neww(String tabId){
            return new Builder(tabId);
        }

        /*public Builder addEntry(String entryId, int x, int y, ItemStack icon, String... connections){
            widgets.add(new RelativePositionButton(entryId, x, y, icon));
            return this;
        }*/
        public HexiconTabInfo build(){
            HexiconTabInfo ret = new HexiconTabInfo();
            ret.id = this.tabId;
            return ret;
        }

    }

}
