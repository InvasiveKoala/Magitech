package com.github.invasivekoala.magitech.client.screen.hexicon.entries;

import com.github.invasivekoala.magitech.client.screen.hexicon.button.CraftingRecipeClickable;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;


public class CraftingRecipePage extends HexiconPage{

    final ResourceLocation recipe;

    Recipe<?> cachedRecipe;
    public CraftingRecipePage(String entryId, ResourceLocation recipe){
        super(entryId);
        this.recipe = recipe;
    }
    private int x,y;

    @Override
    public void renderPage(PoseStack stack, boolean rightSide, HexiconBookScreen screen, float partialTick, int mouseX, int mouseY) {

        if (cachedRecipe == null){
            cachedRecipe = ClientEvents.getClient().level.getRecipeManager().byKey(recipe).get();

            x = (rightSide)? screen.x+227 : screen.x+62;
            y = screen.y + 87;

            ItemStack result = cachedRecipe.getResultItem();
            //ClientEvents.getClient().getItemRenderer().renderGuiItem(result, x+21, (y + 2));
            renderables.add(new CraftingRecipeClickable(screen, List.of(result), x+21, y+2));

            int iX = x + 2;
            int iY = y + 72;
            for(int i =0;i<cachedRecipe.getIngredients().size();i++){
                ItemStack[] aitemstack = cachedRecipe.getIngredients().get(i).getItems();

                if ((aitemstack.length > 0 && !aitemstack[0].is(Items.AIR))) // If it's not air we add a clickable icon to be the ingredient
                    renderables.add(new CraftingRecipeClickable(screen, List.of(aitemstack), iX, iY));


                if ((i+1)%3 == 0){
                    iX = x + 2;
                    iY +=18;
                } else iX += 18;
            }


        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HexiconBookScreen.HEXICONBOOK_LOCATION);
        GuiComponent.blit(stack, x, y, 352, 51, 56, 126, HexiconBookScreen.imageWidth, HexiconBookScreen.imageHeight);
        super.renderPage(stack, rightSide, screen, partialTick, mouseX, mouseY);
    }
}
