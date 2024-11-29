package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.packets.ClientboundDropItemPacket;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DropItemVerb extends VerbWord {

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            if (object instanceof ServerPlayer p){
                PacketRegistry.sendTo(new ClientboundDropItemPacket(), p);
            }
            else if (object instanceof LivingEntity le){
                le.spawnAtLocation(le.getMainHandItem());
                le.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }
        return true;
    }

}
