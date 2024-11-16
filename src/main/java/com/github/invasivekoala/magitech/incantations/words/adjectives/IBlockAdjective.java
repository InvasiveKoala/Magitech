package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface IBlockAdjective{
    List<BlockPos> narrowBlockDown(List<BlockPos> currentList, SentenceContext cxt, NounWord<BlockPos> noun);
}
