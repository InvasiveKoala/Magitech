package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class WiltVerb extends VerbWord {
    public WiltVerb(String id) {
        super(id);
    }

    @Override
    public boolean effect(SentenceContext context) {
        for (Object object : context.object.getThing(context) ){
            if (object instanceof LivingEntity le){
                le.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 60));
                return true;
            }
        }
        return false;
    }

}
