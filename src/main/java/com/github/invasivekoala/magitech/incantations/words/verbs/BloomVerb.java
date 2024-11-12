package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BloomVerb extends VerbWord {
    public BloomVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ){
            if (object instanceof BlockPos bs){
                BlockState state = context.level.getBlockState(bs);
                BonemealableBlock bonemealableblock = (BonemealableBlock)context.level.getBlockState(bs).getBlock();
                if (bonemealableblock.isValidBonemealTarget(context.level, bs, state, false)) {
                    if (bonemealableblock.isBonemealSuccess(context.level, context.level.random, bs, state)) {
                        bonemealableblock.performBonemeal(context.level, context.level.random, bs, state);
                    }
                }
            } else if (object instanceof LivingEntity le){
                le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200));
            }
        }
        return true;
    }

    @Override
    public List<Types> objectTypes() {
        return List.of(Types.ENTITY, Types.BLOCK);
    }
}
