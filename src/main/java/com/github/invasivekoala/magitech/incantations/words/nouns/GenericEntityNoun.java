package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;

public class GenericEntityNoun<T extends Entity> extends NounWord<T> {
    private final Class<T> clazz;
    public GenericEntityNoun(String id, Class<T> entity) {
        super(id, Types.ENTITY);
        clazz = entity;
    }

    @Override
    public Class<T> nounClass(SentenceContext cxt) {
        return clazz;
    }
}
