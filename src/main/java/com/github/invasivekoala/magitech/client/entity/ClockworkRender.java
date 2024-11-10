package com.github.invasivekoala.magitech.client.entity;

import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import com.github.invasivekoala.magitech.client.entity.layer.ClockworkHeldItemLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ClockworkRender extends GeoEntityRenderer<ClockworkEntity> {
    public ClockworkRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ClockworkModel());
        this.addLayer(new ClockworkHeldItemLayer(this));
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(GeoModel model, ClockworkEntity animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(1.1f, 2.0f, 1.1f);
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
