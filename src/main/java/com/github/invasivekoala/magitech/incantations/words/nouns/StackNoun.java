package com.github.invasivekoala.magitech.incantations.words.nouns;


import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.NounWord;

import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class StackNoun<T> extends NounWord<T> {
    private final Function<Deque<T>, List<T>> getter;
    public StackNoun(String id, Function<Deque<T>, List<T>> dir) {
        super(id, Types.ANY);
        getter = dir;
    }

    public List<T> get(Deque<T> cxt) throws IncantationException {
        try {
            List<T> toReturn = getter.apply(cxt);
            if (toReturn.isEmpty()) throw new IncantationException(0, IncantationException.NO_VALID_MEMORY);
            return toReturn;
        } catch (NoSuchElementException e){
            throw new IncantationException(0, IncantationException.NO_VALID_MEMORY);
        }
    }

}
