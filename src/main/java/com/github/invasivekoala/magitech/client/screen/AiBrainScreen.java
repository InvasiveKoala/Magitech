package com.github.invasivekoala.magitech.client.screen;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.blocks.entity.WorkbenchBlockEntity;
import com.github.invasivekoala.magitech.containers.AiBrainContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class AiBrainScreen extends AbstractContainerScreen<AiBrainContainer> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Magitech.MOD_ID, "textures/gui/brain_inventory.png");
    private static final Component BRAIN_COMPONENT = new TranslatableComponent("gui.magitech.craftbrain");

    public AiBrainScreen(AiBrainContainer pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 133;
        this.inventoryLabelY = 39;
        this.inventoryLabelX = 8;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);

        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        drawString(pPoseStack, this.font, this.title, this.titleLabelX, this.titleLabelY, 0xb34636);
        drawString(pPoseStack, this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xb34636);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new ExtendedButton(15, 3, 10, 10, new TranslatableComponent("gui.magitech.craftbrain"), btn ->{
            Minecraft.getInstance().player.displayClientMessage(new TextComponent("hi"), true);
        }));

    }
}
