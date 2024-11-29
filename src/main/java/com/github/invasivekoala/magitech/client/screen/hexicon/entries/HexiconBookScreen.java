package com.github.invasivekoala.magitech.client.screen.hexicon.entries;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.client.screen.hexicon.HexiconEntryInfo;
import com.github.invasivekoala.magitech.client.screen.hexicon.HexiconScreen;
import com.github.invasivekoala.magitech.client.screen.hexicon.button.BookExitEntryButton;
import com.github.invasivekoala.magitech.client.screen.hexicon.button.BookNavigateButton;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class HexiconBookScreen extends Screen {


    public static final ResourceLocation HEXICONBOOK_LOCATION = new ResourceLocation(Magitech.MOD_ID, "textures/gui/hexiconbook.png");
    public static final int screenWidth = 352, screenHeight = 320, imageWidth = 512, imageHeight=320;


    public int pageTurnNum = 0,x,y;


    final HexiconScreen parent;
    public final HexiconEntryInfo entry;
    BookNavigateButton backButton;
    BookNavigateButton forwardButton;

    public HexiconBookScreen(HexiconScreen parent, HexiconEntryInfo entry) {
        super(new TextComponent("Hexicon"));
        this.parent = parent;
        this.entry = entry;
    }

    @Override
    protected void init() {
        super.init();
        x = (this.width- screenWidth)/2;
        y =(this.height- screenHeight)/2;

        backButton = addRenderableWidget(new BookNavigateButton(this, x + 22, y + 272, this::handleBackArrow,17));
        backButton.visible = false;

        forwardButton = addRenderableWidget(new BookNavigateButton(this, x+310, y+272, this::handleForwardArrow, 0));
        forwardButton.visible = entry.pages.size() > 2;

        addRenderableWidget(new BookExitEntryButton(this, x+153, y+272, this::handleExitButton));

    }
    public void handleBackArrow(Button b){
        forwardButton.visible = true;
        pageTurnNum -= 1;
        backButton.visible = pageTurnNum > 0;
    }
    public void handleForwardArrow(Button b){
        backButton.visible = true;
        pageTurnNum += 1;
        forwardButton.visible = getPageNum(true) < entry.pages.size();

    }
    public void handleExitButton(Button b){
        onClose();
    }

    @Override
    public void onClose() {
        ClientEvents.getClient().setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HEXICONBOOK_LOCATION);
        blit(pPoseStack, x, y, 0, 0, screenWidth, screenHeight, imageWidth, imageHeight);

        // Render left side
        int pageNum = getPageNum(false);
        HexiconPage pg = entry.pages.get(pageNum);
        pg.renderPage(pPoseStack, false, this, pPartialTick, pMouseX, pMouseY);

        // Render right side (if it exists, otherwise render the cool icon.)
        if (pageNum + 1 < entry.pages.size()){
            pg = entry.pages.get(pageNum + 1);
            pg.renderPage(pPoseStack, true, this, pPartialTick, pMouseX, pMouseY);

        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);


    }
    public int getPageNum(boolean rightSide){
        int pg = pageTurnNum*2;
        return (rightSide)? pg + 1 : pg;
    }

}
