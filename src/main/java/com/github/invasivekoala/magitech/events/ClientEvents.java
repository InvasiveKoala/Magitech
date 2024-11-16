package com.github.invasivekoala.magitech.events;

import com.github.invasivekoala.magitech.client.entity.BroomModel;
import com.github.invasivekoala.magitech.client.entity.BroomRenderer;
import com.github.invasivekoala.magitech.client.entity.ClockworkRender;
import com.github.invasivekoala.magitech.client.entity.MagicPlatformRender;
import com.github.invasivekoala.magitech.client.screen.AiBrainScreen;
import com.github.invasivekoala.magitech.containers.ContainerRegistry;
import com.github.invasivekoala.magitech.entities.BroomEntity;
import com.github.invasivekoala.magitech.entities.EntityRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.UUID;

public class ClientEvents {

    public static final ArrayList<UUID> broomRiders = new ArrayList<>();

    public static void init(){
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;


        forgeBus.addListener(ClientEvents::hideBroomRiders);
        modBus.addListener(ClientEvents::registerRenderers);
        modBus.addListener(ClientEvents::registerLayerDefinitions);
        modBus.addListener(ClientEvents::clientSetup);
    }

    private static void hideBroomRiders(RenderLivingEvent.Pre event){
        Entity entity = event.getEntity().getVehicle();
        if (entity instanceof BroomEntity) {
            if (broomRiders.contains(event.getEntity().getUUID())) event.setCanceled(true); // Don't render the real player if they're riding a dragon
            CameraType camera = getClient().options.getCameraType();
            if (getClient().player == event.getEntity() && camera == CameraType.FIRST_PERSON) event.setCanceled(true); // Don't render the "fake" player if the player is in 1st person
        }
    }
    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegistry.BROOM.get(), BroomRenderer::new);
    }
    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BroomModel.LAYER_LOCATION, BroomModel::createBodyLayer);
    }

    private static void clientSetup(final FMLClientSetupEvent event){
        EntityRenderers.register(EntityRegistry.CLOCKWORK.get(), ClockworkRender::new);
        EntityRenderers.register(EntityRegistry.MAGIC_PLATFORM.get(), MagicPlatformRender::new);
        EntityRenderers.register(EntityRegistry.BROOM.get(), BroomRenderer::new);

        MenuScreens.register(ContainerRegistry.AI_BRAIN_CONTAINER.get(), AiBrainScreen::new);
    }
    public static Minecraft getClient(){
        return Minecraft.getInstance();
    }
}
