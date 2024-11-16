package com.github.invasivekoala.magitech.containers;

import com.github.invasivekoala.magitech.blocks.BlockRegistry;
import com.github.invasivekoala.magitech.blocks.entity.WorkbenchBlockEntity;
import com.github.invasivekoala.magitech.items.aicores.AbstractAiCore;
import com.github.invasivekoala.magitech.items.aicores.BrainItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class AiBrainContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;
    private final Slot resultSlot;
    private final Player player;
    private final NonNullList<Slot> craftSlots = NonNullList.create();
    public final Container craftSlotContainer = new SimpleContainer(3){
        @Override
        public void setChanged() {
            slotChangedCraftingGrid();
            super.setChanged();
        }
    };
    private final ResultContainer resultSlots = new ResultContainer(){
        @Override
        public void setChanged() {
            //slotChangedCraftingGrid();
            super.setChanged();
        }
    };
    // Client Constructor
    public AiBrainContainer(int id, Inventory playerInv) {
        this(id, playerInv, new ItemStackHandler(4), BlockPos.ZERO);
    }

    // Server Constructor
    public AiBrainContainer(int id, Inventory playerInv, IItemHandler slots, BlockPos pos) {
        super(ContainerRegistry.AI_BRAIN_CONTAINER.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInv.player.level, pos);
        this.player = playerInv.player;

        final int slotSizePlus2 = 18, startX = 8, startY = 51, hotbarY = 109, inventoryX = 44, inventoryY = 20;


        // PlayerNoun Inv slots
        for (int row = 0; row < 3; row++){
            for(int column = 0; column < 9; column++){
                addSlot(new Slot(playerInv, 9 + row * 9 + column, startX + column * slotSizePlus2,
                        startY + row * slotSizePlus2));
            }
        }
        // Hotbar
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, startX + column * slotSizePlus2, hotbarY));
        }

        // Slots to place the ai cores that craft the brain
        for (int column = 0; column < 3; column++) {
            addSlot(new Slot(craftSlotContainer, column, inventoryX + (column * slotSizePlus2),
                    inventoryY){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getItem() instanceof AbstractAiCore;
                }
                @Override
                public int getMaxStackSize(@NotNull ItemStack stack) {
                    return 1;
                }
                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            });
        }

        // Result slot
        // I don't use the result slot class because that seems to be more geared to crafting tables specifically.
        this.resultSlot = addSlot(new Slot(resultSlots, 0, 116, 20){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

    }

    public void slotChangedCraftingGrid() {
        if (!player.getLevel().isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer)player;

            // Get the cores from all the core slots.
            NonNullList<ItemStack> coreStacks = NonNullList.create();
            for (Slot coreSlot : craftSlots) {
                coreStacks.add(coreSlot.getItem());
            }

            // Create a new brain item
            ItemStack stack = BrainItem.serializeCores(coreStacks);// Put the cores into the brain's NBT

            // Finally update the result slot to the new cores.
            resultSlot.set(stack);
            resultSlots.setItem(0, stack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), resultSlot.getSlotIndex(), stack));
        }
    }



    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        var retStack = ItemStack.EMPTY;
        final Slot slot = this.getSlot(pIndex);
        if(slot.hasItem()){
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if(pIndex < 27) {
                if (!moveItemStackTo(item, 27, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }else if (!moveItemStackTo(item, 0, 27, false))
                return ItemStack.EMPTY;

            if (item.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return retStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(containerAccess, pPlayer, BlockRegistry.BRAIN_WORKBENCH.get());
    }

    public static MenuConstructor getServerContainer(WorkbenchBlockEntity workbench, BlockPos pos){
        return (id, playerInv, player) -> new AiBrainContainer(id, playerInv, workbench.inventory, pos);
    }






}