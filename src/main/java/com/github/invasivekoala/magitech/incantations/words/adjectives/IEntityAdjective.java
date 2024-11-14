package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import com.github.invasivekoala.magitech.incantations.words.nouns.GenericEntityNoun;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface IEntityAdjective{

    List<Entity> narrowEntityDown(List<Entity> currentList, SentenceContext cxt, GenericEntityNoun<Entity> noun) throws IncantationException;
}
