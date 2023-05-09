package com.github.invasivekoala.magitech.entities.client;

import com.github.invasivekoala.magitech.entities.MagicPlatformEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MagicPlatformRender extends GeoEntityRenderer<MagicPlatformEntity> {
    public MagicPlatformRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MagicPlatformModel());
    }


    @Override
    public RenderType getRenderType(MagicPlatformEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture, true);
    }
}
