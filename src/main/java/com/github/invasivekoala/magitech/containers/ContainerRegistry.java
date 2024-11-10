package com.github.invasivekoala.magitech.containers;

import com.github.invasivekoala.magitech.Magitech;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {

    private ContainerRegistry(){
    }

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Magitech.MOD_ID);

    public static final RegistryObject<MenuType<AiBrainContainer>> AI_BRAIN_CONTAINER = CONTAINERS.register("brain_container", () ->
            new MenuType<>(AiBrainContainer::new));

    public static void register(IEventBus bus){
        CONTAINERS.register(bus);
    }
}
