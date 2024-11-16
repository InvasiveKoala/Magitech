package com.github.invasivekoala.magitech.blocks;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Magitech.MOD_ID);


    public static final RegistryObject<Block> TEST_BLOCK = registerBlock("test_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> BRAIN_WORKBENCH = registerBlock("brain_workbench",
            () -> new WorkbenchBlock(BlockBehaviour.Properties.of(Material.WOOD)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> blockReturn = BLOCKS.register(name, block);
        // Register the item for the block along with the block itself
        registerBlockItem(name, blockReturn);
        return blockReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(),
                ItemRegistry.defaultProperties()));
    }



    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
