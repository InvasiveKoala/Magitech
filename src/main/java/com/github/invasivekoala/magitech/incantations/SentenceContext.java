package com.github.invasivekoala.magitech.incantations;


import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import com.github.invasivekoala.magitech.incantations.words.VerbWord;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;


// Passed to VerbWord#effect for context about the rest of the sentence
public class SentenceContext {

    public SentenceContext(){

    }
    public SentenceContext(ServerPlayer p){
        setPlayerCaster(p);
    }
    public void setPlayerCaster(ServerPlayer p){
        playerCaster = p;
        location = playerCaster.position();
        blockPos = playerCaster.blockPosition();
        level = playerCaster.getLevel();
    }
    public void effect() throws IncantationException {
        verb.effect(this);
    }

    public ServerLevel level;
    public ServerPlayer playerCaster;
    public Vec3 location;
    public BlockPos blockPos;

    public VerbWord verb;
    @Nullable
    public NounInstance subject;
    @Nullable
    public NounInstance object;


    public static CompoundTag writeNbt(SentenceContext context){
        CompoundTag tag = new CompoundTag();
        if (context.subject != null){
            tag.put("subject", NounInstance.toNbt(context.subject));
        }
        if (context.object != null){
            tag.put("object", NounInstance.toNbt(context.object));
        }
        tag.put("verb", StringTag.valueOf(context.verb.registryName));
        return tag;
    }
    public static SentenceContext fromNbt(CompoundTag tag){
        SentenceContext cxt = new SentenceContext();
        if (tag.contains("subject")){
            cxt.subject = NounInstance.fromNbt(tag.getCompound("subject"));
        }
        if (tag.contains("object")){
            cxt.object = NounInstance.fromNbt(tag.getCompound("object"));
        }
        cxt.verb = WordRegistry.VERBS.get(tag.getString("verb"));
        return cxt;
    }



}
