package com.github.invasivekoala.magitech.items.aicores;

import com.google.common.collect.Sets;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.item.Item;

import java.util.Set;

public abstract class AbstractAiCore extends Item {
    protected enum GOALS {

    }



    private final Set<WrappedGoal> goals = Sets.newLinkedHashSet();

    public AbstractAiCore(Properties pProperties) {
        super(pProperties);
    }

    abstract void createGoals(PathfinderMob mob);

    protected void addGoal(int pPriority, Goal pGoal) {
        this.goals.add(new WrappedGoal(pPriority, pGoal));
    }

    public Set<WrappedGoal> getGoals() {
        return goals;
    }

}
