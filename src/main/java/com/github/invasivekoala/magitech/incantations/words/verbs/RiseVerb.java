package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.packets.ClientboundPushPacket;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class RiseVerb extends VerbWord {
    public RiseVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            if (object instanceof Entity e){
                push(e, new Vec3(0, 1,0));
            }
        }
        return true;
    }

}
