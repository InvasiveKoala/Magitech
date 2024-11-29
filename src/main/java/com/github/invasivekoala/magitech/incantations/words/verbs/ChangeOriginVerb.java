package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.WordEvents;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.UUID;

public class ChangeOriginVerb extends VerbWord {

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        List<Object> objects = context.object.getThing(context);
        if (objects.size() > 1) return false;
        if (!(objects.get(0) instanceof BlockPos pos)) throw new IncantationException(context.object.wordNumber, IncantationException.OVER_NOUN_LIMIT);

        UUID uuid = context.playerCaster.getUUID();
        if (WordEvents.onGoingSpells.containsKey(uuid)){

            WordEvents.onGoingSpells.get(uuid).changeOrigin(pos);
        }
        return true;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.BLOCK);
    }

    @Override
    public int objectLimit() {
        return 1;
    }
}
