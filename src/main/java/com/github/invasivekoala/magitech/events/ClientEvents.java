package com.github.invasivekoala.magitech.events;

import com.github.invasivekoala.magitech.client.entity.BroomModel;
import com.github.invasivekoala.magitech.client.entity.BroomRenderer;
import com.github.invasivekoala.magitech.entities.BroomEntity;
import com.github.invasivekoala.magitech.entities.EntityRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
    }

    public static void hideBroomRiders(RenderLivingEvent.Pre event){
        Entity entity = event.getEntity().getVehicle();
        if (entity instanceof BroomEntity) {
            if (broomRiders.contains(event.getEntity().getUUID())) event.setCanceled(true); // Don't render the real player if they're riding a dragon
            CameraType camera = getClient().options.getCameraType();
            if (getClient().player == event.getEntity() && camera == CameraType.FIRST_PERSON) event.setCanceled(true); // Don't render the "fake" player if the player is in 1st person
        }
    }
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegistry.BROOM.get(), BroomRenderer::new);
    }
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BroomModel.LAYER_LOCATION, BroomModel::createBodyLayer);
    }
    public static Minecraft getClient(){
        return Minecraft.getInstance();
    }

}
