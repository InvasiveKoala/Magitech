package com.github.invasivekoala.magitech.incantations.words.verbs;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.List;

public class TargetVerb extends VerbWord {


    @Override
    public boolean effect(SentenceContext context) throws IncantationException {

        // Targets
        List<LivingEntity> targets = new ArrayList<>();
        for (Object object : context.object.getThing(context, context.playerCaster) ){
            if (object instanceof LivingEntity le){
                targets.add(le);
            }
        }
        int size = targets.size();
        for (Object object : context.subject.getThing(context, context.playerCaster) ){
            if (object instanceof Mob m){
                m.setTarget(targets.get(context.playerCaster.getRandom().nextInt(size)));
            }
        }
        return true;
    }

    @Override
    public boolean hasSubject() {
        return true;
    }

}
