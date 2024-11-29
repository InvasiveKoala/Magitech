package com.github.invasivekoala.magitech.client.screen.hexicon.button;

import com.github.invasivekoala.magitech.client.screen.hexicon.entries.HexiconBookScreen;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CraftingRecipeClickable extends Button {
    final HexiconBookScreen parent;
    final List<ItemStack> stack;
    float time;
    public CraftingRecipeClickable(HexiconBookScreen parent, List<ItemStack> stack, int pX, int pY) {
        super(pX, pY, 16, 16, TextComponent.EMPTY, (b) -> {});
        this.parent= parent;
        this.stack = stack;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        time += pPartialTick;
        ItemStack toRender;
        if (stack.size() == 0) toRender = ItemStack.EMPTY;
        else if (stack.size() == 1) toRender = stack.get(0); // Maybe this makes it faster? idk
        else toRender = stack.get(Mth.floor(time / 30.0F) % stack.size());
        ClientEvents.getClient().getItemRenderer().renderGuiItem(toRender, x, y);
        if (isHoveredOrFocused()){
            parent.renderTooltip(pPoseStack, toRender.getHoverName(),pMouseX, pMouseY);
        }
    }


}
