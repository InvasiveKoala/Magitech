package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EffectVerb extends VerbWord {
    private final MobEffectInstance effect;
    public EffectVerb(MobEffectInstance effect) {
        super();
        this.effect = effect;
    }

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        for (Object object : context.object.getThing(context) ) {
            if (object instanceof LivingEntity le) {
                le.addEffect(new MobEffectInstance(effect));

            }
        }
        return true;
    }

}
