package com.github.invasivekoala.magitech.client.screen.hexicon.button;

import com.github.invasivekoala.magitech.client.screen.hexicon.HexiconScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;

public class TabButton extends Button {
    public final HexiconScreen screen;
    public boolean isPressed;
    public TabButton(int pX, int pY, String tabId, HexiconScreen screen) {
        super(pX, pY, 64, 32, TextComponent.EMPTY, (b) -> {
            TabButton but = ((TabButton) b);
            but.screen.switchTab(HexiconScreen.getTab(tabId), false);
            if (but.screen.currentSelectedButton != null) // This should be done within the switchtab method!! but idc
                but.screen.currentSelectedButton.isPressed = false;
            but.screen.currentSelectedButton = but;
            but.isPressed = true;
        });
        isPressed = tabId.equals(HexiconScreen.currentSelectedTab);
        screen.currentSelectedButton = (isPressed)? this : screen.currentSelectedButton;
        this.screen = screen;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HexiconScreen.HEXICON_LOCATION);
        int i = getYImage(isPressed) - 1;
        blit(pPoseStack, this.x, this.y, 352, 48 + 32 * i, this.width, this.height, HexiconScreen.imageWidth, HexiconScreen.imageHeight);
    }


}
