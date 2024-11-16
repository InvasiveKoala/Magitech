package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.client.screen.hexicon.button.RelativePositionButton;
import com.github.invasivekoala.magitech.client.screen.hexicon.button.TabButton;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.HashSet;
import java.util.Set;

public class HexiconScreen extends Screen {


    public static final ResourceLocation HEXICON_LOCATION = new ResourceLocation(Magitech.MOD_ID, "textures/gui/hexicon.png");
    public static final int screenWidth = 288+64, screenHeight = 255+64, imageWidth = 512, imageHeight=320;



    Set<RelativePositionButton> currentWidgetsWithinTab = new HashSet<>();
    Set<AbstractWidget> currentWidgetsOutsideTab = new HashSet<>();

    public double scrollingXPos = 0, scrollingYPos = 0;
    public static String currentSelectedTab = "introduction";
    public TabButton currentSelectedButton;

    public int x,y,maxXMouseBound,maxYMouseBound;

    public HexiconScreen() {
        super(new TextComponent("Hexicon"));
    }

    @Override
    protected void init() {
        super.init();

        switchTab(getTab(currentSelectedTab), true); // saves the player's selected tab (not over server restarts, but still useful QoL)

        x = (this.width- screenWidth)/2;
        y =(this.height- screenHeight)/2;
        this.maxXMouseBound = x + screenWidth;
        this.maxYMouseBound = y +screenHeight;

        int i = 0;
        currentWidgetsOutsideTab.add(addRenderableWidget(new TabButton(x-31, y + (32 * i++), "introduction", this)));
        currentWidgetsOutsideTab.add(addRenderableWidget(new TabButton(x-31, y + (32 * i++), "incantations", this)));
        //addRenderableWidget(new RelativePositionButton(this, -i, i, Items.COAL.getDefaultInstance()));
        //addRenderableWidget(new RelativePositionButton(this, 0, 0, Items.DIAMOND.getDefaultInstance()));
        //addRenderableWidget(new RelativePositionButton(this, i, 0, ItemRegistry.CRYSTALLIZED_SHADOW.get().getDefaultInstance()));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {



        Window window = (ClientEvents.getClient().getWindow());
        double scale = window.getGuiScale();
        // 3 is the border width
        RenderSystem.enableScissor(
                (int) (x*scale + 3), (int) (y*scale + 3),
                (int) (screenWidth*scale - 3), (int) (screenHeight*scale - 3));
        renderBackground(pPoseStack);

        renderWidgetsWithinTab(pPoseStack, pMouseX, pMouseY, pPartialTick); // Renders widgets

        RenderSystem.disableScissor();



        // Render the border
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HEXICON_LOCATION);
        blit(pPoseStack, x, y, 0, 1, screenWidth, screenHeight, imageWidth, imageHeight);
        renderWidgetsOutsideTab(pPoseStack, pMouseX,pMouseY,pPartialTick);
    }

    private void renderWidgetsWithinTab(PoseStack ps, int pMouseX, int pMouseY, float pPartialTick){
        for (AbstractWidget w : currentWidgetsWithinTab){
            w.render(ps, pMouseX, pMouseY, pPartialTick);
        }
    }
    private void renderWidgetsOutsideTab(PoseStack ps, int pMouseX, int pMouseY, float pPartialTick){
        for (AbstractWidget w : currentWidgetsOutsideTab){
            w.render(ps, pMouseX, pMouseY, pPartialTick);
        }
    }

    public void switchTab(HexiconTabInfo info, boolean onStartup){
        if (info.id.equals(currentSelectedTab) && !onStartup) return;

        for (AbstractWidget button : currentWidgetsWithinTab){
            removeWidget(button);
        }
        currentWidgetsWithinTab.clear();
        currentSelectedTab = info.id;
        info.addWidgets(this);
        scrollingXPos=0; // reset position
        scrollingYPos=0;
    }
    public static HexiconTabInfo getTab(String id){
        return HexiconTabRegistry.TABS.get(id);
    }

    public void addWidgetsWithinTab(Set<RelativePositionButton> button){
        currentWidgetsWithinTab.addAll(button);
        button.forEach((b) -> {
            b.screen = this;
            addRenderableWidget(b);
        });
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (pButton != 0) return false;
        if (pMouseX < x || pMouseX > maxXMouseBound || pMouseY < y || pMouseY > maxYMouseBound) return false; // You can only drag "inside" the screen
        scrollingXPos += pDragX;
        scrollingYPos += pDragY;
        return true;
    }

}
