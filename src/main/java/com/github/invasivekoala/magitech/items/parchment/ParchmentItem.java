package com.github.invasivekoala.magitech.items.parchment;

import com.github.invasivekoala.magitech.client.screen.incantation_screens.ParchmentScreen;
import com.github.invasivekoala.magitech.events.ClientEvents;
import com.github.invasivekoala.magitech.incantations.MultiLineSpell;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.WordEvents;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ParchmentItem extends Item {
    public ParchmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!stack.hasTag() || pPlayer.isShiftKeyDown()) {
            if (pLevel.isClientSide)
            ClientEvents.getClient().setScreen(new ParchmentScreen(pPlayer, stack, pUsedHand));
        } else if (!pLevel.isClientSide()){
            ServerPlayer sp = (ServerPlayer) pPlayer;
            WordEvents.castMultiLine(sp, loadSpellFromNBT(sp, stack));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }


    public static void savePageTextToNbt(ServerPlayer player, ItemStack stack, String text){
        assert stack.is(ItemRegistry.SPELL_PARCHMENT.get());

        ListTag cxtList = new ListTag();

        String[] lines = text.split("\\R+");

        for (String line : lines){

            line = line.replaceAll("#(.*)", ""); // comments, removes anything after a #

            String[] splitString = line.split("\\s+");
            try {
                SentenceContext context = WordEvents.parseSpell(player, List.of(splitString));
                cxtList.add(SentenceContext.writeNbt(context));
            } catch (IncantationException e) {
                e.printStackTrace();
            }
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("spells", cxtList);
        tag.putString("rawText", text);
    }
    public static MultiLineSpell loadSpellFromNBT(ServerPlayer player, ItemStack stack){
        assert stack.is(ItemRegistry.SPELL_PARCHMENT.get());
        ListTag tag = stack.getOrCreateTag().getList("spells", 10);
        List<SentenceContext> spells = new ArrayList<>();
        for (int i = 0;i<tag.size();i++){
            CompoundTag sentence = tag.getCompound(i);
            SentenceContext cxt = SentenceContext.fromNbt(sentence);
            cxt.setPlayerCaster(player);
            spells.add(cxt);
        }
        return new MultiLineSpell(spells);
    }
}
