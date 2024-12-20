package com.github.invasivekoala.magitech;

import com.github.invasivekoala.magitech.blocks.BlockRegistry;
import com.github.invasivekoala.magitech.blocks.entity.BlockEntityRegistry;
import com.github.invasivekoala.magitech.client.entity.BroomRenderer;
import com.github.invasivekoala.magitech.client.entity.ClockworkRender;
import com.github.invasivekoala.magitech.client.entity.MagicPlatformRender;
import com.github.invasivekoala.magitech.client.screen.AiBrainScreen;
import com.github.invasivekoala.magitech.client.screen.hexicon.HexiconTabRegistry;
import com.github.invasivekoala.magitech.containers.ContainerRegistry;
import com.github.invasivekoala.magitech.entities.EntityRegistry;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;


@Mod(Magitech.MOD_ID)
public class Magitech {

    public static final String MOD_ID = "magitech";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Magitech() {
        // Register the setup method for modloading

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        //eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        //eventBus.addListener(this::processIMC);


        if (FMLEnvironment.dist == Dist.CLIENT)
            ClientEvents.init();

        PacketRegistry.init();
        // Registries
        ItemRegistry.register(eventBus);
        BlockRegistry.register(eventBus);
        EntityRegistry.register(eventBus);
        ContainerRegistry.register(eventBus);
        BlockEntityRegistry.register(eventBus);



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        HexiconTabRegistry.init();
    }


    /*private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("magitech", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }*/

}
