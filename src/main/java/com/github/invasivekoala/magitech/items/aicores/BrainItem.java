package com.github.invasivekoala.magitech.items.aicores;

import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class BrainItem extends Item {
    public BrainItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof ClockworkEntity clockwork) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            clockwork.goalSelector.removeAllGoals();

            CompoundTag tag = stack.getOrCreateTag().getCompound("Goals");
            Set<WrappedGoal> goals = deserializeGoals(tag);

            clockwork.goalSelector.getAvailableGoals().addAll(goals);

            pPlayer.setItemInHand(pUsedHand, clockwork.getStackInSlot(1));
            clockwork.setStackInSlot(1, pStack);
            return InteractionResult.sidedSuccess(pPlayer.level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    protected CompoundTag serializeGoals(Set<WrappedGoal> goals){
        return new CompoundTag();
    }
    protected Set<WrappedGoal> deserializeGoals(CompoundTag nbt){
        return new HashSet<>();
    }
}
