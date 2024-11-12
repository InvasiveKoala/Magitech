package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.Word;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class NearbyAdjective extends AdjectiveWord implements IEntityAdjective {
    public NearbyAdjective(String id, Word.Types t) {
        super(id, t);
    }


    @Override
    public List<Entity> narrowEntityDown(List<Entity> currentList, SentenceContext cxt, NounWord<Entity> noun) {
        if (currentList.isEmpty())
            return cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(cxt), AABB.ofSize(cxt.location, 5, 5, 5));
        else
            return cxt.playerCaster.getLevel().getEntitiesOfClass(noun.nounClass(cxt), AABB.ofSize(cxt.location, 5, 5, 5), currentList::contains);
    }


    // Should always be first.
    @Override
    public int adjectivePriority() {
        return 0;
    }


}
