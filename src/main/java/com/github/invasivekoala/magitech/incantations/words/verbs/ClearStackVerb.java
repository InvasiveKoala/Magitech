package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.MemoryUtil;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ClearStackVerb extends VerbWord {

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            if (object instanceof ServerPlayer p)
            MemoryUtil.getOrCreateMemory(p.getUUID()).clear();
        }
        return true;
    }


    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY);
    }
}
