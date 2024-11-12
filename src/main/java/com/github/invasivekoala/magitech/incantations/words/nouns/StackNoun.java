package com.github.invasivekoala.magitech.incantations.words.nouns;


import com.github.invasivekoala.magitech.incantations.MemoryUtil;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

public class StackNoun<T> extends NounWord<T> {
    private final Function<Deque<T>, List<T>> direction;
    public StackNoun(String id, Function<Deque<T>, List<T>> dir) {
        super(id, Types.ANY);
        direction = dir;
    }

    public List<T> get(Deque<T> cxt) throws IncantationException {
        List<T> toReturn = direction.apply(cxt);
        if (toReturn.isEmpty()) throw new IncantationException(0, IncantationException.NO_VALID_MEMORY);
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> nounClass(SentenceContext context) {
        Object obj = MemoryUtil.getOrCreateMemory(context.playerCaster.getUUID());
        if (obj == null) return null; // TODO maybe throw an error here?
        return (Class<T>) obj.getClass(); // ignore unchecked cast warning, we know its good
    }
}
