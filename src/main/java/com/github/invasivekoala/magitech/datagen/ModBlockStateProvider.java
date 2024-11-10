package com.github.invasivekoala.magitech.datagen;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.blocks.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Magitech.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(BlockRegistry.TEST_BLOCK.get());
        simpleBlock(BlockRegistry.BRAIN_WORKBENCH.get());

    }
}
