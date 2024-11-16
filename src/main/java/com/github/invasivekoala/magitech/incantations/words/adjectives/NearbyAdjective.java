package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class NearbyAdjective extends AdjectiveWord implements IEntityAdjective, IBlockAdjective {
    public NearbyAdjective(String id) {
        super(id);
    }


    @Override
    public List<Entity> narrowEntityDown(List<Entity> currentList, SentenceContext cxt, GenericEntityNoun<Entity> noun) {
        if (currentList.isEmpty())
            return cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(cxt), AABB.ofSize(cxt.location, 10, 10, 10));
        else
            return cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(cxt), AABB.ofSize(cxt.location, 10, 10, 10), currentList::contains);
    }

    @Override
    public List<BlockPos> narrowBlockDown(List<BlockPos> currentList, SentenceContext cxt, NounWord<BlockPos> noun) {
        boolean doWeCareAboutList = currentList.size() > 0;
        List<BlockPos> poses = new ArrayList<>();
        BlockPos.withinManhattan(cxt.blockPos, 5, 5, 5).forEach((bp) -> {
            if (!cxt.level.getBlockState(bp).isAir()) {
                if (doWeCareAboutList){
                    if (currentList.contains(bp)) poses.add(bp);
                } else poses.add(bp);
            }
        });
        return poses;
    }

    // Should always be first.
    @Override
    public int adjectivePriority() {
        return 0;
    }



}
