package com.github.invasivekoala.magitech.items;

import com.github.invasivekoala.magitech.entities.MagicPlatformEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PlatformClock extends Item {
    public PlatformClock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            MagicPlatformEntity platform = MagicPlatformEntity.spawnPlatform(pLevel, pPlayer.getX(), pPlayer.getY()-1.2 , pPlayer.getZ());
            platform.setYRot(pPlayer.getYRot());
            pLevel.addFreshEntity(platform);
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
