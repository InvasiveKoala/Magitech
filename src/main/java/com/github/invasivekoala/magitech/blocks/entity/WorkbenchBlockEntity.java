package com.github.invasivekoala.magitech.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

public class WorkbenchBlockEntity extends InventoryBlockEntity {
    public static final Component TITLE = new TranslatableComponent("gui.magitech.braintitle");
    public WorkbenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.AI_BRAIN_WORKBENCH.get(), pPos, pBlockState, 4);
    }

}
