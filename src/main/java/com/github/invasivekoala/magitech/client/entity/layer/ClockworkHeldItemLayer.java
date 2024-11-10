package com.github.invasivekoala.magitech.client.entity.layer;

import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class ClockworkHeldItemLayer extends GeoLayerRenderer<ClockworkEntity> {
    protected final GeoModelProvider provider;
    public ClockworkHeldItemLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
        provider = entityRendererIn.getGeoModelProvider();
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ClockworkEntity clockwork, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (clockwork.getHeldItem().isEmpty()) return; // Don't begin rendering without item

        // Get the renderer that handles this for players
        ItemInHandRenderer renderer = Minecraft.getInstance().getItemInHandRenderer();

        // Translate to correct position
        translate(matrixStackIn, clockwork);


        // Render the item in the hand
        renderer.renderItem(clockwork, clockwork.getHeldItem(), ItemTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);


    }

    private void translate(PoseStack poseStack, ClockworkEntity clockwork){
        GeoModel model = provider.getModel(provider.getModelLocation(clockwork));

        // Here we get the bone in the model that represents the position we want to render the item at.
        GeoBone bone = model.getBone("itemrender").orElseThrow(() ->
                new ReportedException(CrashReport.forThrowable(new Throwable(), "Entity should have a bone named 'itemrender' to have a rider layer!")));
        poseStack.translate(bone.getModelPosition().x * 00.0625F, bone.getModelPosition().y * 00.0625F, bone.getModelPosition().z * 00.0625F);
        poseStack.mulPoseMatrix(bone.getModelRotationMat());
    }
}
