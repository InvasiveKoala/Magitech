package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.MemoryUtil;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;

import java.util.Deque;
import java.util.List;

public class DoubleStackVerb extends VerbWord {

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            Deque<Object> deq = MemoryUtil.getOrCreateMemory(context.playerCaster.getUUID());
            for (int i =0;i<2;i++)
                deq.push(object);
        }
        return true;
    }


    @Override
    public List<Types> objectTypes() {
        return List.of(Types.values());
    }
}
