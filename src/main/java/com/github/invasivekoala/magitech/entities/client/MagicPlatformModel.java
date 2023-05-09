package com.github.invasivekoala.magitech.entities.client;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.MagicPlatformEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MagicPlatformModel extends AnimatedGeoModel<MagicPlatformEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(Magitech.MOD_ID, "geo/magic_platform.geo.json");
    private static final ResourceLocation TEX = new ResourceLocation(Magitech.MOD_ID, "textures/entity/magic_platform.png");
    private static final ResourceLocation ANIMS = new ResourceLocation(Magitech.MOD_ID, "animations/magic_platform.animation.json");
    @Override
    public ResourceLocation getModelLocation(MagicPlatformEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(MagicPlatformEntity object) {
        return TEX;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MagicPlatformEntity animatable) {
        return ANIMS;
    }
}
