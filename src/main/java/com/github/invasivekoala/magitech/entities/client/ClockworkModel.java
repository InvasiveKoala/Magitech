package com.github.invasivekoala.magitech.entities.client;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ClockworkModel extends AnimatedGeoModel<ClockworkEntity> {

    @Override
    public void codeAnimations(ClockworkEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.codeAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        AnimationData manager = entity.getFactory().getOrCreateAnimationData(uniqueID.intValue());
        int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
    }

    @Override
    public ResourceLocation getModelLocation(ClockworkEntity object) {
        return new ResourceLocation(Magitech.MOD_ID, "geo/clockwork.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ClockworkEntity object) {
        return new ResourceLocation(Magitech.MOD_ID, "textures/entity/clockwork/clockwork.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ClockworkEntity animatable) {
        return new ResourceLocation(Magitech.MOD_ID, "animations/clockwork.animation.json");
    }
}
