package com.github.invasivekoala.magitech.items;

import com.github.invasivekoala.magitech.entities.EntityRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BroomItem extends Item {
    public BroomItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            ServerLevel lvl = (ServerLevel) pLevel;

            Entity e = EntityRegistry.BROOM.get().spawn(lvl, stack, pPlayer, pPlayer.blockPosition(), MobSpawnType.SPAWN_EGG, false, false);
            pPlayer.startRiding(e);

            stack.shrink(1);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }
}
