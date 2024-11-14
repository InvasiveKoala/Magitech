package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.nouns.DirectionNoun;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import com.github.invasivekoala.magitech.packets.ClientboundPushPacket;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PushVerb extends VerbWord {
    public PushVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException{
        List pushees = context.subject.getThing(context);

        for (Object pushee : pushees)
        {
            if (!(pushee instanceof Entity pushedEntity)) throw new IncantationException(context.subject.wordNumber, IncantationException.WRONG_NOUN);
            Vec3 direction = null;

            List<Object> objects = context.object.getThing(context, context.playerCaster);
            if (objects.size() > objectLimit()) throw new IncantationException(context.object.wordNumber, IncantationException.OVER_ENTITY_LIMIT);
            if (objects.isEmpty()) throw new IncantationException(context.object.wordNumber, IncantationException.NO_TARGET_FOUND);

            if (objects.get(0) instanceof Vec3 v) {
                direction = v;
            } else if (objects.get(0) instanceof Entity e) {
                direction = e.position().subtract(pushedEntity.position()).normalize().scale(2);
            }
            if (direction == null) throw new IncantationException(context.subject.wordNumber, IncantationException.WRONG_NOUN); // Maybe not needed?


            push(pushedEntity, direction);

        }


        return true;
    }

    @Override
    public boolean hasSubject() {
        return true;
    }

    @Override
    public int objectLimit() {
        return 1;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY, Types.DIRECTION);
    }
}
