package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class GenericEntityNoun<T extends Entity> extends NounWord<T> {
    private final EntityType<T> type;
    private final Class<T> entityClass;
    private final boolean conjurable;

    public GenericEntityNoun(String id, EntityType<T> entity) {
        this(id,entity,null, false);
    }
    public GenericEntityNoun(String id, EntityType<T> entity, boolean conjurable) {
        this(id,entity,null, conjurable);
    }
    public GenericEntityNoun(String id, Class<T> entity) {
        this(id,null, entity, false);
    }
    public GenericEntityNoun(String id, EntityType<T> entity, Class<T> clazz,  boolean conjurable) {
        super(id, Types.ENTITY);
        type = entity;
        this.entityClass= (clazz != null)? clazz : (Class<T>) entity.getBaseClass();
        this.conjurable = conjurable;
    }

    public boolean isConjurable(){
        return conjurable;
    }

    public Class<T> nounClass(SentenceContext cxt){
        return entityClass;

    }

    public EntityType<T> entityType(SentenceContext cxt) {
        return type;
    }
}
