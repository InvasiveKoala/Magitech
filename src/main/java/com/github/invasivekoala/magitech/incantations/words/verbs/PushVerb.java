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
            if (context.object.wordSingleton instanceof DirectionNoun dn) {
                DirectionNoun.checkDirection(context.object);
                direction = dn.getDirection(context, pushedEntity);

            } else if (context.object.wordSingleton instanceof GenericEntityNoun) {
                List<Object> objects = context.object.getThing(context);
                if (objects.size() > subjectLimit()) throw new IncantationException(context.object.wordNumber, IncantationException.OVER_ENTITY_LIMIT);
                if (objects.isEmpty()) throw new IncantationException(context.object.wordNumber, IncantationException.NO_TARGET_FOUND);
                Entity e = (Entity) objects.get(0);
                direction = e.position().subtract(pushedEntity.position()).normalize();
            }
            if (direction == null) throw new IncantationException(context.subject.wordNumber, IncantationException.WRONG_NOUN); // Maybe not needed?
            direction.scale(2.0f);


            push(pushedEntity, direction);

        }


        return true;
    }

    @Override
    public boolean hasSubject() {
        return true;
    }

    @Override
    public int subjectLimit() {
        return 1;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY, Types.DIRECTION);
    }
}
