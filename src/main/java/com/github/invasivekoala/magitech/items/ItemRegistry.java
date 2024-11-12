package com.github.invasivekoala.magitech.items;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.EntityRegistry;
import com.github.invasivekoala.magitech.items.aicores.BrainItem;
import com.github.invasivekoala.magitech.items.aicores.WanderAiCore;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ItemRegistry {

    static final CreativeModeTab MAGITECH_TAB = new CreativeModeTab(Magitech.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CLOCK_BOOK.get());
        }
    };


    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Magitech.MOD_ID);
    public static final RegistryObject<Item> CLOCK_BOOK = register("clock_book", 1);

    public static final RegistryObject<Item> BRAIN_ITEM = ITEMS.register("ai_brain",
            () -> new BrainItem(defaultProperties().stacksTo(1)));
    public static final RegistryObject<Item> WANDER_AI_CORE = ITEMS.register("wander_ai_core",
            () -> new WanderAiCore(defaultProperties().stacksTo(64)));
    public static final RegistryObject<Item> BLANK_AI_CORE = register("blank_ai_core");
    public static final RegistryObject<Item> BROOMSTICK = ITEMS.register("broomstick",
            () -> new BroomItem(defaultProperties().stacksTo(1)));


    public static final RegistryObject<Item> PLATFORM_CLOCK = ITEMS.register("platform_clock",
            () -> new PlatformClock(defaultProperties().stacksTo(1)));



    public static final RegistryObject<Item> CLOCKWORK_SPAWN_EGG = registerSpawnEgg("clockwork_spawn_egg",
            EntityRegistry.CLOCKWORK, 0x8a4836, 0xe07438);
    public static final RegistryObject<Item> CRYSTALLIZED_SHADOW = register("crystallized_shadow");
    public static final RegistryObject<Item> RAW_SHADOW = register("raw_shadow");


    // Methods that just make registering items easier
    // -----------------------------------------------


    static RegistryObject<Item> register(String name){
        return register(name, 64);
    }
    static RegistryObject<Item> register(String name, int stack){
        return ITEMS.register(name, () -> new Item(defaultProperties().stacksTo(stack)));
    }



    public static <T extends Mob> RegistryObject<Item> registerSpawnEgg(String name, RegistryObject<EntityType<T>> entity, int bgColor, int highlightColor){
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(entity, bgColor, highlightColor, defaultProperties()));
    }


    public static Item.Properties defaultProperties(){
        return new Item.Properties().tab(MAGITECH_TAB);
    }


    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }



}
