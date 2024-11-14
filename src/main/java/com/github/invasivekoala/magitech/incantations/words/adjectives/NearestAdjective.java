package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class NearestAdjective extends AdjectiveWord implements IEntityAdjective, IBlockAdjective {
    public NearestAdjective(String id) {
        super(id);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> narrowEntityDown(List<Entity> currentList, SentenceContext cxt, GenericEntityNoun<Entity> noun) {
        List<Entity> iterate;
        if (currentList == null || currentList.isEmpty()){
            iterate = cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(cxt), AABB.ofSize(cxt.location, 10, 10, 10));
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


    @Override
    public List<BlockPos> narrowBlockDown(List<BlockPos> currentList, SentenceContext cxt, NounWord<BlockPos> noun) {
        if (currentList.size() > 0){
            BlockPos closest = currentList.remove(0);
            for (BlockPos pos : currentList){
                if (cxt.blockPos.distManhattan(pos) < cxt.blockPos.distManhattan(closest)){
                    closest = pos;
                }
            }
            return List.of(closest);
        }

        Optional<BlockPos> maybe = BlockPos.findClosestMatch(new BlockPos(cxt.location), 5, 5, (bp) -> !cxt.level.getBlockState(bp).isAir());
        if (maybe.isEmpty()) return currentList;
        return List.of(maybe.get());
    }
}
