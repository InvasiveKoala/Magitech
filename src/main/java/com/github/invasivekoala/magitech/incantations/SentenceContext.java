package com.github.invasivekoala.magitech.incantations;


import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;


// Passed to VerbWord#effect for context about the rest of the sentence
public class SentenceContext {

    public SentenceContext(ServerPlayer p){
        playerCaster = p;
        location = playerCaster.position();
        blockPos = playerCaster.blockPosition();
        level = playerCaster.getLevel();
    }
    public ServerLevel level;
    public ServerPlayer playerCaster;
    public Vec3 location; // Can be changed during the spell
    public BlockPos blockPos;

    public VerbWord verb;
    @Nullable
    public NounInstance subject;
    @Nullable
    public NounInstance object;



}
