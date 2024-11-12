package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.MemoryUtil;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;

import java.util.Deque;
import java.util.List;

public class AddToStackVerb extends VerbWord {
    public AddToStackVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            Deque<Object> deq = MemoryUtil.getOrCreateMemory(context.playerCaster.getUUID());
            deq.push(object);
        }
        return true;
    }


    @Override
    public List<Types> objectTypes() {
        return List.of(Types.values());
    }
}
