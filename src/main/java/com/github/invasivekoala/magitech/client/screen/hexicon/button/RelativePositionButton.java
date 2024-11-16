package com.github.invasivekoala.magitech.client.screen.hexicon.button;

import com.github.invasivekoala.magitech.client.screen.hexicon.HexiconScreen;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

public class RelativePositionButton extends Button {
    private final ItemStack toDisplay;
    public final int lockedXPos, lockedYPos;
    public HexiconScreen screen;




    public final String entryId;
    public RelativePositionButton(String entryId, int pX, int pY, ItemStack toDisplay) {
        super(pX, pY, 24, 24, TextComponent.EMPTY, (b) -> {});
        lockedXPos = pX;
        lockedYPos = pY;
        this.entryId = entryId;
        this.toDisplay = toDisplay;
    }


    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.x = getRealPosFromRelativePos(lockedXPos, true);
        this.y = getRealPosFromRelativePos(lockedYPos, false);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HexiconScreen.HEXICON_LOCATION);
        //Matrix4f matrix4f = Matrix4f.orthographic(0.0F, screen.width, 0.0F, screen.height, 1000.0F, 3000.0F);
        //RenderSystem.setProjectionMatrix(matrix4f);
        int i = getYImage(isHovered) - 1;
        blit(pPoseStack, this.x, this.y, 352, 24 * i, this.width, this.height, HexiconScreen.imageWidth, HexiconScreen.imageHeight);

        // Render item on top
        if (toDisplay != null){
            ClientEvents.getClient().getItemRenderer().renderGuiItem(toDisplay, this.x+4, this.y+4);
        }

    }

    public int getRealPosFromRelativePos(int relativePos, boolean x){
        if (x){
            return (int) (((screen.width/2) + screen.scrollingXPos + relativePos) - (this.width/2));
        } else return (int) (((screen.height/2) + screen.scrollingYPos + relativePos) - (this.height/2));
    }


}
