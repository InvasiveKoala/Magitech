package com.github.invasivekoala.magitech.events;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.MagicPlatformEntity;
import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import com.github.invasivekoala.magitech.entities.EntityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= Magitech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.CLOCKWORK.get(), ClockworkEntity.setAttributes());
        event.put(EntityRegistry.MAGIC_PLATFORM.get(), MagicPlatformEntity.setAttributes());
        event.put(EntityRegistry.BROOM.get(), LivingEntity.createLivingAttributes().build());
    }
}
