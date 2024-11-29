package com.github.invasivekoala.magitech.client.screen.hexicon;

import com.github.invasivekoala.magitech.client.screen.hexicon.button.RelativePositionButton;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class ButtonConnection {

    public enum Piece{
        DOWN_ARROW(352, 112,7,6),
        RIGHT_ARROW(359, 112,6,7),
        UP_ARROW(365, 112,7,6),
        LEFT_ARROW(372, 112,6,7),
        HOR_PIPE(381, 112,24,3),
        VERT_PIPE(378, 112,3,24);


        public final int uvX,uvY,xSize,ySize;

        Piece(int uvX, int uvY, int xSize, int ySize){
            this.uvX=uvX;
            this.uvY = uvY;
            this.xSize=xSize;
            this.ySize=ySize;
        }
    }
    public List<Piece> pieces;
    public String to;

    public Vec2 direction;
    public static final Vec2 LEFT = Vec2.NEG_UNIT_X,RIGHT = Vec2.UNIT_X,UP =Vec2.NEG_UNIT_Y,DOWN = Vec2.UNIT_Y;

    public void instantiateConnectionButton(HexiconScreen screen, Button fromButton){
        HexiconEntryInfo info = HexiconTabRegistry.ENTRIES.get(to);
        if (info.pos != null) return; // If it has a manual position set we don't want to create another button

        Vec2 pos = getStartingPos(fromButton, pieces.get(0));
        int x = (int) pos.x;
        int y = (int) pos.y;
        for (int i = 1;i<pieces.size();i++){
            Piece piece = pieces.get(i);
            x += piece.xSize * direction.x;
            y += piece.ySize * direction.y;
        }
        RelativePositionButton button = new RelativePositionButton(to, x, y, info.icon);
        screen.addWidgetWithinTab(button);
        for (ButtonConnection connection : info.connectsTo){
            connection.instantiateConnectionButton(screen, button);
        }
    }

    public void render(Button fromButton, PoseStack stack){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HexiconScreen.HEXICON_LOCATION);
        RenderSystem.enableBlend();
        Vec2 pos = getStartingPos(fromButton, pieces.get(0));
        int x = (int) pos.x;
        int y = (int) pos.y;
        for (Piece piece : pieces) {
            GuiComponent.blit(stack, x, y, piece.uvX, piece.uvY, piece.xSize, piece.ySize, HexiconScreen.imageWidth, HexiconScreen.imageHeight);
            x += piece.xSize * direction.x;
            y += piece.ySize * direction.y;
        }
    }

    // Omg this might be bad coding
    public Vec2 getStartingPos(Button button, Piece piece){
        int x=button.x,y=button.y;
        if (LEFT.equals(direction)) {
            x -= piece.xSize;
        } else if (RIGHT.equals(direction)){
            x += button.getWidth() + piece.xSize;
        } else if (UP.equals(direction)){
            y-= piece.ySize;
        } else {
            y += piece.ySize + button.getHeight();
        }
        return new Vec2(x,y);
    }

    public static class Builder {
        private final List<Piece> pieces = new ArrayList<>();
        private final String to;
        private final Vec2 dir;
        private Builder(String to, Vec2 dir){
            this.to = to;
            this.dir = dir;
        }
        public static Builder dir(String to, Vec2 dir){
            return new Builder(to,dir);
        }
        public Builder addPiece(Piece piece){
            pieces.add(piece);
            return this;
        }
        public ButtonConnection build(){
            ButtonConnection ret = new ButtonConnection();
            ret.to = this.to;
            ret.direction = this.dir;
            ret.pieces = this.pieces;
            return ret;
        }

    }

}
