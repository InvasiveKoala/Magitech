package com.github.invasivekoala.magitech.datagen.loot;

import com.github.invasivekoala.magitech.blocks.BlockRegistry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        dropSelf(BlockRegistry.TEST_BLOCK.get());
        dropSelf(BlockRegistry.BRAIN_WORKBENCH.get());
        // Example for ores/blocks that dont always drop themself
        //add(BlockRegistry.TEST_BLOCK.get(), (block) -> createOreDrop(BlockRegistry.TEST_BLOCK.get(), RAW_ITEM))
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
