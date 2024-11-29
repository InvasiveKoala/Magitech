package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.WordEvents;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;

import java.util.UUID;

public class DelayVerb extends VerbWord {
    private final int ticks;
    public DelayVerb(int ticks) {
        super();
        this.ticks = ticks;
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        UUID uuid = context.playerCaster.getUUID();
        if (WordEvents.onGoingSpells.containsKey(uuid)){
            WordEvents.onGoingSpells.get(uuid).delay += ticks;
        }
        return true;
    }

    @Override
    public boolean hasSubject() {
        return false;
    }

    @Override
    public boolean hasObject() {
        return false;
    }
}
