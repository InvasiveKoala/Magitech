package com.github.invasivekoala.magitech.incantations.words.adjectives;

import com.github.invasivekoala.magitech.incantations.SentenceContext;
import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.AdjectiveWord;
import com.github.invasivekoala.magitech.incantations.words.NounWord;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class BlockRaytraceAdjective extends AdjectiveWord implements IBlockAdjective {
    public BlockRaytraceAdjective(String id, Types t) {
        super(id, t);
    }

    // Should always be last
    @Override
    public int adjectivePriority() {
        return 99;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BlockPos> narrowBlockDown(List<BlockPos> currentList, SentenceContext cxt, NounWord<BlockPos> noun) {
        BlockHitResult bh = (BlockHitResult) cxt.playerCaster.pick(30, 0, false);
        return List.of(bh.getBlockPos());
    }
}
