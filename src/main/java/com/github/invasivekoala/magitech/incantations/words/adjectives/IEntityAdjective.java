package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface IEntityAdjective {
    <T extends Entity> List<T> narrowEntityDown(List<?> currentList, SentenceContext cxt, NounWord<T> noun);
}
