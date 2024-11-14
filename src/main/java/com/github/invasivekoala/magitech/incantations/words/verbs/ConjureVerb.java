package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ConjureVerb extends VerbWord {
    public ConjureVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        List<Object> object = context.object.getThing(context, context.playerCaster);
        if (object.size() > 1) throw new IncantationException(context.object.wordNumber, IncantationException.OVER_ENTITY_LIMIT);
        Vec3 pos;
        Object obj = object.get(0);
        if (obj instanceof Vec3 v)
            pos = v.add(context.playerCaster.getEyePosition());
        else
            pos = getPosition(obj);


        if (context.subject.wordSingleton instanceof GenericEntityNoun n){
            if (n.isConjurable()){
                Entity e = n.entityType(context).spawn(context.level, null, context.playerCaster, new BlockPos(pos), MobSpawnType.MOB_SUMMONED, false, false);
                if (e != null) e.setPos(pos);

            } else throw new IncantationException(context.subject.wordNumber, IncantationException.NOT_CONJURABLE);
        }

        return true;
    }

    @Override
    public boolean hasSubject() {
        return true;
    }


    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY, Types.BLOCK, Types.DIRECTION);
    }
}
