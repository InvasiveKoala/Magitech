package com.github.invasivekoala.magitech.incantations;

import com.github.invasivekoala.magitech.incantations.exceptions.IncantationException;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MultiLineSpell {
    public final List<SentenceContext> lines;
    public int delay;
    public int line;
    public BlockPos origin;
    private boolean hasOriginChanged = false;


    public MultiLineSpell(List<SentenceContext> spells){
        lines = spells;
    }

    public void tick() throws IncantationException{
        if (delay > 0) delay--;
        else if (!lines.isEmpty()) {
            triggerNextSpell();
        }
    }
    public void changeOrigin(BlockPos origin){
        this.origin = origin;
        hasOriginChanged = true;
    }

    public void triggerNextSpell() throws IncantationException {
        SentenceContext context = lines.remove(0);
        if (origin == null) origin = context.blockPos;

        // We do this so that the origin can be changed mid-spell
        if (hasOriginChanged) {
            context.location = new Vec3(origin.getX(), origin.getY(), origin.getZ());
            context.blockPos = origin;
        }

        context.effect();
        line++;
    }
}
