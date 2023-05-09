package com.github.invasivekoala.magitech.datagen;

import com.github.invasivekoala.magitech.Magitech;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Magitech.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {


    // Reminder: Any time anything in here changes, do runData.
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider((new ModRecipeProvider(generator)));
        generator.addProvider((new ModLootTableProvider(generator)));
        generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
        generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
    }
}
