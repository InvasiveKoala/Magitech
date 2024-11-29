package com.github.invasivekoala.magitech.client.screen.hexicon.button;

import com.github.invasivekoala.magitech.client.screen.hexicon.entries.HexiconBookScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;

public class BookExitEntryButton extends Button {
    final HexiconBookScreen parent;
    public BookExitEntryButton(HexiconBookScreen parent, int pX, int pY, OnPress pOnPress) {
        super(pX, pY, 10, 17, TextComponent.EMPTY, pOnPress);
        this.parent= parent;;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HexiconBookScreen.HEXICONBOOK_LOCATION);
        blit(pPoseStack, this.x, this.y-(getYImage(isHovered)-1), 352 + ((getYImage(isHovered)-1)  * 20), 34, 10, 17, HexiconBookScreen.imageWidth, HexiconBookScreen.imageHeight);
    }

    @Override
    public void playDownSound(SoundManager pHandler) {
        pHandler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }
}
