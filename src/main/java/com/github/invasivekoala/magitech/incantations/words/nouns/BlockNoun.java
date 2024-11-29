package com.github.invasivekoala.magitech.incantations.words.nouns;

import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.core.BlockPos;

public class BlockNoun<T extends BlockPos> extends NounWord<T> {
    public BlockNoun() {
        super(Types.BLOCK);
    }


}
