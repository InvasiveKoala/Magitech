package com.github.invasivekoala.magitech.client.screen.hexicon.entries;

import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Widget;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public abstract class HexiconPage {

    public List<Widget> renderables = Lists.newArrayList();

    protected final Font font;
    public final String entryId;
    public HexiconPage(String entryId){
        this.entryId=entryId;
        this.font = ClientEvents.getClient().font;
    }
    public void renderPage(PoseStack stack, boolean rightSide, HexiconBookScreen screen, float partialTick, int mouseX, int mouseY){
        for (Widget w : renderables){
            w.render(stack, mouseX, mouseY, partialTick);
        }

    }
}
