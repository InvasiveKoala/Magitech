package com.github.invasivekoala.magitech.blocks;

import com.github.invasivekoala.magitech.blocks.entity.WorkbenchBlockEntity;
import com.github.invasivekoala.magitech.containers.AiBrainContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class WorkbenchBlock extends Block implements EntityBlock {
    public WorkbenchBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WorkbenchBlockEntity(pPos,pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof final WorkbenchBlockEntity chest) {
            final MenuProvider container = new SimpleMenuProvider(AiBrainContainer.getServerContainer(chest, pos),
                    WorkbenchBlockEntity.TITLE);
            NetworkHooks.openGui((ServerPlayer) player, container, pos);
        }

        return InteractionResult.SUCCESS;
    }
}
