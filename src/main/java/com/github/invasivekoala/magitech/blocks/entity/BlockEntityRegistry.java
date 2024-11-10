package com.github.invasivekoala.magitech.blocks.entity;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.blocks.BlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITIES, Magitech.MOD_ID);

    public static final RegistryObject<BlockEntityType<WorkbenchBlockEntity>> AI_BRAIN_WORKBENCH = BLOCK_ENTITIES
            .register("example_chest", () -> BlockEntityType.Builder
                    .of(WorkbenchBlockEntity::new, BlockRegistry.BRAIN_WORKBENCH.get()).build(null));


    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
    private BlockEntityRegistry() {
    }
}
