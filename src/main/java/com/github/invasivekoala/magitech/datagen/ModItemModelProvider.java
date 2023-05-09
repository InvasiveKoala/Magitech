package com.github.invasivekoala.magitech.datagen;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Magitech.MOD_ID, existingFileHelper);
    }

    List<Item> REGISTERED = new ArrayList<>();
    // Empty for now, but used for special item models
    void customItems(){
        //getBuilderFor("")
    }


    @Override
    protected void registerModels() {
        customItems();


        // Basically just gets all the items
        final Set<Item> items = ItemRegistry.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
        REGISTERED.forEach(items::remove);

        // Loop through
        for(Item item : items){
            // Add more to this as time goes on

            // Simple blocks
            if (item instanceof BlockItem){
                ResourceLocation registry = item.getRegistryName();
                getBuilderFor(item).parent(uncheckedModel(registry.getNamespace() + ":block/" + registry.getPath()));
            }
            else if (item instanceof ForgeSpawnEggItem){
                eggItem(item);
            }
            // Tools
            else if (item instanceof TieredItem){
                handheldItem(item);
            } else simpleItem(item);
        }

    }

    // Kaupenjoe tutorial lol
    private ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Magitech.MOD_ID,"item/" + item.getRegistryName().getPath()));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Magitech.MOD_ID,"item/" + item.getRegistryName().getPath()));
    }
    private ItemModelBuilder eggItem(Item item){
        return getBuilderFor(item).parent(uncheckedModel(mcLoc("item/template_spawn_egg").toString()));
    }


    // Used for creating the block model automatically
    // Took inspiration from wyrmroost's code for this
    private ItemModelBuilder getBuilderFor(ItemLike item)
    {
        REGISTERED.add(item.asItem());
        return getBuilder(item.asItem().getRegistryName().getPath());
    }

    private static ModelFile.UncheckedModelFile uncheckedModel(String path)
    {
        return new ModelFile.UncheckedModelFile(path);
    }
}
