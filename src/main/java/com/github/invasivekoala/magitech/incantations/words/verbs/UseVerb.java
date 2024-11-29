package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class UseVerb extends VerbWord {

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            if (object instanceof BlockPos bs){
                BlockState state = context.level.getBlockState(bs);
                state.use(context.level, context.playerCaster, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(bs), Direction.NORTH, bs, true));
            } else if (object instanceof LivingEntity e){
                e.interact(context.playerCaster, InteractionHand.MAIN_HAND);
            }
        }
        return true;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY, Types.BLOCK);
    }
}
