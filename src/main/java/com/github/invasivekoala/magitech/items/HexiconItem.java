package com.github.invasivekoala.magitech.items;

import com.github.invasivekoala.magitech.client.screen.hexicon.HexiconScreen;
import com.github.invasivekoala.magitech.events.ClientEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HexiconItem extends Item {
    public HexiconItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide)
            ClientEvents.getClient().setScreen(new HexiconScreen());
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
