package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public class BlockNoun<T extends BlockPos> extends NounWord<T> {
    private final Class<T> clazz;
    public BlockNoun(String id, Class<T> block) {
        super(id, Types.BLOCK);
        clazz=block;
    }


}
