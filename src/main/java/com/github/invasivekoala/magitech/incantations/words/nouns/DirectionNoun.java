package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class DirectionNoun extends NounWord<Vec3> {
    private final BiFunction<SentenceContext, Entity, Vec3> direction;
    public DirectionNoun(BiFunction<SentenceContext, Entity, Vec3> dir) {
        super(Types.DIRECTION);
        direction = dir;
    }

    public Vec3 getDirection(SentenceContext cxt, @Nullable Entity le){
        try{
            return direction.apply(cxt, le).scale(2);
        } catch (NullPointerException e){
            return Vec3.ZERO;
        }

    }
}
