package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.MemoryUtil;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VectorBetweenStackVerb extends VerbWord {
    public VectorBetweenStackVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
       List<Object> object = context.object.getThing(context);
       if (object.size() > objectLimit()) throw new IncantationException(context.object.wordNumber, IncantationException.OVER_ENTITY_LIMIT);
       List<Object> subject = context.subject.getThing(context);
       if (subject.size() > subjectLimit()) throw new IncantationException(context.subject.wordNumber, IncantationException.OVER_ENTITY_LIMIT);

       Vec3 ret = getPosition(object.get(0), false).subtract(getPosition(subject.get(0), false));
       MemoryUtil.getOrCreateMemory(context.playerCaster.getUUID()).push(ret);

       return true;
    }


    @Override
    public boolean hasSubject() {
        return true;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY, Types.BLOCK);
    }

    @Override
    public List<Types> subjectTypes() {
        return List.of(Types.ENTITY, Types.BLOCK);
    }

    @Override
    public int subjectLimit() {
        return 1;
    }

    @Override
    public int objectLimit() {
        return 1;
    }
}
