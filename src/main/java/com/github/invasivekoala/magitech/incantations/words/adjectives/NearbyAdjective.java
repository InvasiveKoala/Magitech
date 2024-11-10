package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.WordRegistry;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class NearbyAdjective extends AdjectiveWord implements IEntityAdjective, IBlockAdjective {
    public NearbyAdjective(String id, WordRegistry.Types... t) {
        super(id, t);
    }


    @Override
    public <T extends Entity> List<T> narrowEntityDown(List<?> currentList, SentenceContext cxt, NounWord<T> noun) {
        if (currentList.isEmpty())
            return cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(), AABB.ofSize(cxt.playerCaster.position(), 5, 5, 5));
        else
            return cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(), AABB.ofSize(cxt.playerCaster.position(), 5, 5, 5), currentList::contains);
    }
    @Override
    public <T extends BlockState> List<T> narrowBlockDown(List<?> currentList, SentenceContext cxt, NounWord<T> noun) {
        return null;
    }

    @Override
    public boolean isPrimaryAdjective() {
        return true;
    }


}
