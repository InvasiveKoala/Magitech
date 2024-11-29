package com.github.invasivekoala.magitech.client.screen.hexicon.entries;

import com.github.invasivekoala.magitech.Magitech;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;

public class TextPage extends HexiconPage{

    public final TranslatableComponent translation;

    public TextPage(String entryId, String translateKey){
        super(entryId);
        this.translation = new TranslatableComponent("page." + Magitech.MOD_ID + "." + translateKey);
    }

    @Override
    public void renderPage(PoseStack stack, boolean rightSide, HexiconBookScreen screen, float partialTick, int mouseX, int mouseY) {
        int x = (rightSide)? screen.x+190 : screen.x+25;
        font.drawWordWrap(translation, x, screen.y + 30, 140, 0);
    }
}
