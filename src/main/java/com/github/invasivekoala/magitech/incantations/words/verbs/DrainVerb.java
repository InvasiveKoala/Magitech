package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class DrainVerb extends VerbWord {

    @Override
    public boolean effect(SentenceContext context) throws IncantationException {
        float netHealth = 0.0f;
        for(Object object : context.object.getThing(context)){
            if (object instanceof LivingEntity le){
                le.hurt(DamageSource.indirectMagic(context.playerCaster, null), 2.0f);
                netHealth += 2.0f;
            }
        }
        List healedPeople = context.subject.getThing(context);
        for(Object object : healedPeople){
            if (object instanceof LivingEntity le){
                le.heal(netHealth/healedPeople.size());
            }
        }
        return netHealth > 0;
    }

    @Override
    public boolean hasSubject() {
        return true;
    }

}
