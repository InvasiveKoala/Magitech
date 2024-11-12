package com.github.invasivekoala.magitech.entities;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, Magitech.MOD_ID);

    public static final RegistryObject<EntityType<ClockworkEntity>> CLOCKWORK = ENTITY_TYPES.register("clockwork",
            () -> EntityType.Builder.of(ClockworkEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.5f)
                    .build(new ResourceLocation(Magitech.MOD_ID, "clockwork").toString()));


    public static final RegistryObject<EntityType<MagicPlatformEntity>> MAGIC_PLATFORM = ENTITY_TYPES.register("platform",
            () -> EntityType.Builder.of(MagicPlatformEntity::new, MobCategory.MISC)
                    .sized(3.0f, 1.0f)
                    .build(new ResourceLocation(Magitech.MOD_ID, "platform").toString()));


    public static final RegistryObject<EntityType<BroomEntity>> BROOM = ENTITY_TYPES.register("broomstick",
            () -> EntityType.Builder.of(BroomEntity::new, MobCategory.MISC)
                    .sized(1.0f, 0.5f)
                    .build(new ResourceLocation(Magitech.MOD_ID, "broomstick").toString()));

    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }

}
