package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class NearestAdjective extends AdjectiveWord implements IEntityAdjective {
    public NearestAdjective(String id, Types t) {
        super(id, t);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> narrowEntityDown(List<Entity> currentList, SentenceContext cxt, NounWord<Entity> noun) {
        List<Entity> iterate;
        if (currentList == null || currentList.isEmpty()){
            iterate = cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(cxt), AABB.ofSize(cxt.location, 5, 5, 5));
            iterate.remove(cxt.playerCaster); // Remove the caster from this list.
            if (iterate.isEmpty()) return currentList;
        }
        else {
            iterate = currentList;
        }
        // Otherwise find the nearest of the already-chosen mobs
        Entity closest = iterate.get(0);
        for (Object e : iterate){
            if (!(e instanceof Entity ent)) continue;
            if (cxt.location.distanceTo(ent.position()) < cxt.location.distanceTo(closest.position())) closest = ent;
        }
        return List.of(closest);

    }


    // Should always be last
    @Override
    public int adjectivePriority() {
        return 100;
    }


}
