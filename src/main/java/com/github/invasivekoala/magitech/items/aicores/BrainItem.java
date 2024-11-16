package com.github.invasivekoala.magitech.items.aicores;

import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

            Set<WrappedGoal> goals = translateGoals(deserializeCores(stack), clockwork);


            clockwork.goalSelector.getAvailableGoals().addAll(goals);

            pPlayer.setItemInHand(pUsedHand, clockwork.getStackInSlot(1));
            clockwork.setStackInSlot(1, pStack);
            return InteractionResult.sidedSuccess(pPlayer.level.isClientSide);
        }
        return InteractionResult.PASS;
    }


    // The goals in the Ai brain are saved as items.
    // Each item is a separate goal that you put together to create a brain
    public static ItemStack serializeCores(NonNullList<ItemStack> items){
        ItemStack stack = new ItemStack(ItemRegistry.BRAIN_ITEM.get());

        CompoundTag tag = stack.getOrCreateTag();
        ListTag itemList = new ListTag();
        for(int i = 0; i < items.size(); i++){
            // Save each core as an item in the nbt
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Priority", i);
            items.get(i).getItem().getRegistryName();
            itemList.add(itemTag);
        }
        tag.put("Cores", itemList);

        return stack;
    }
    // The reason we return a hashmap is because we need the priority numbers to be saved alongside the goals.
    // However, we can't turn this into a wrapped goal yet because we don't have the mob to pass into the function yet.
    public static HashMap<ItemStack, Integer> deserializeCores(ItemStack stack){
        HashMap<ItemStack, Integer> priorityGoals = new HashMap<>();

        ListTag itemList = stack.getOrCreateTag().getList("Cores", Tag.TAG_COMPOUND);
        for(int i = 0; i < itemList.size(); i++){
            CompoundTag itemTag = itemList.getCompound(i);
            int priority = itemTag.getInt("Priority");


            ItemStack coreItem = ItemStack.of(itemTag);
            if(coreItem.getItem() instanceof AbstractAiCore) {
                priorityGoals.put(coreItem, priority);
            }

        }
        return priorityGoals;
    }

    public static Set<WrappedGoal> translateGoals(HashMap<ItemStack, Integer> coreGoals, PathfinderMob mob){
        Set<WrappedGoal> returnGoals = new HashSet<>();

        // Get the func goals
        Set<ItemStack> coreList = coreGoals.keySet();
        for (ItemStack core : coreList){
            // If it's somehow not an ai core skip this
            if(!(core.getItem() instanceof AbstractAiCore item)) continue;

            // Finally, with a reference to the mob we can get the actual goal.
            Goal goal = item.getGoal().apply(mob);
            int priority = coreGoals.get(core); // Then the priority from the hashmap.

            returnGoals.add(new WrappedGoal(priority, goal));

        }
        return returnGoals;

    }

    // Says the goals it has in its lore
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getOrCreateTag();
        ListTag itemList = tag.getList("Cores", Tag.TAG_COMPOUND);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag itemTag = itemList.getCompound(i);

            ItemStack coreItem = ItemStack.of(itemTag);

            pTooltipComponents.add(coreItem.getItem().getName(pStack));
        }
    }
}
