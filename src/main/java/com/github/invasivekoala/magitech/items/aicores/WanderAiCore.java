package com.github.invasivekoala.magitech.items.aicores;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class WanderAiCore extends AbstractAiCore{
    public WanderAiCore(Properties pProperties) {
        super(pProperties);
    }

    @Override
    void createGoals(PathfinderMob mob) {
        this.addGoal(7, new WaterAvoidingRandomStrollGoal(mob, 1.0D));
    }
}
