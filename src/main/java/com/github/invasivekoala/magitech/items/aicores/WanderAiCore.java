package com.github.invasivekoala.magitech.items.aicores;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

import java.util.function.Function;


public class WanderAiCore extends AbstractAiCore{
    public WanderAiCore(Properties pProperties) {
        super(pProperties);
    }

    public Function<PathfinderMob, Goal> getGoal() {
        return (mob) -> new WaterAvoidingRandomStrollGoal(mob, 1.0D);
    }
}
