package com.github.invasivekoala.magitech.items.aicores;

import com.google.common.collect.Sets;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;

import java.util.Set;
import java.util.function.Function;

public abstract class AbstractAiCore extends Item {



    private final Set<Goal> goals = Sets.newLinkedHashSet();

    public AbstractAiCore(Properties pProperties) {
        super(pProperties);
    }

    public abstract Function<PathfinderMob, Goal> getGoal();



}
