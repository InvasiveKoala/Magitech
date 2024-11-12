package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntityRaytraceAdjective extends AdjectiveWord implements IEntityAdjective {
    public EntityRaytraceAdjective(String id, Types t) {
        super(id, t);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> narrowEntityDown(List<Entity> currentList, SentenceContext cxt, NounWord<Entity> noun) throws IncantationException {
        Predicate<Entity> predicate;
        if (currentList == null || currentList.isEmpty()){
            predicate = (e) -> true;
        } else predicate = currentList::contains;
        // Otherwise find the nearest of the already-chosen mobs
        Vec3 vecToAdd = cxt.playerCaster.getLookAngle().scale(30);

        EntityHitResult target = ProjectileUtil.getEntityHitResult(cxt.level, cxt.playerCaster,
                cxt.playerCaster.getEyePosition(),
                cxt.playerCaster.getEyePosition().add(vecToAdd),
                AABB.ofSize(cxt.playerCaster.getEyePosition(), 2, 2, 2).expandTowards(vecToAdd).inflate(1.0f),
                predicate,
                0f);
        if (target == null) throw new IncantationException(0, "no_target_found");
        return List.of(target.getEntity());

    }


    // Should always be last
    @Override
    public int adjectivePriority() {
        return 99;
    }


}
