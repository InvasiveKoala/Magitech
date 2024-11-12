package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

import java.util.List;

public class ExplodeVerb extends VerbWord {
    public ExplodeVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for(Object object : context.object.getThing(context)){
            if (object instanceof BlockPos bp){
                context.level.explode(null, bp.getX(), bp.getY(), bp.getZ(), 2f, Explosion.BlockInteraction.BREAK);
            } else if (object instanceof Entity e){
                context.level.explode(null, e.getX(), e.getY(), e.getZ(), 2f, Explosion.BlockInteraction.BREAK);
            }
        }
        return true;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.BLOCK, Types.ENTITY);
    }
}