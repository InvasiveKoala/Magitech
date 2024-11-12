package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.NounInstance;
import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class DirectionNoun extends NounWord<Vec3> {
    private final BiFunction<SentenceContext, Entity, Vec3> direction;
    public DirectionNoun(String id, BiFunction<SentenceContext, Entity, Vec3> dir) {
        super(id, Types.DIRECTION);
        direction = dir;
    }

    public Vec3 getDirection(SentenceContext cxt, Entity le){
        return direction.apply(cxt, le);
    }
    public static void checkDirection(NounInstance myNoun) throws IncantationException {
        if (!myNoun.adjectives.isEmpty()) throw new IncantationException(myNoun.wordNumber, IncantationException.DIRECTION_WITH_ADJECTIVE);
    }
    // This doesn't really matter
    @Override
    public Class<Vec3> nounClass(SentenceContext cxt) {
        return Vec3.class;
    }
}
