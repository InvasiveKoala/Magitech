package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface IDirectionAdjective {
    List<Vec3> narrowItDown(List<Vec3> currentList, SentenceContext cxt, NounWord<Vec3> noun);
}
