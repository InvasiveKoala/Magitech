package com.github.invasivekoala.magitech.client.entity;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.BroomEntity;

import com.github.invasivekoala.magitech.events.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BroomRenderer<T extends BroomEntity> extends EntityRenderer<T> {
    public static final ResourceLocation BROOM_TEXTURE = new ResourceLocation(Magitech.MOD_ID, "textures/entity/broomstick.png");
    private final BroomModel<BroomEntity> model;
    public BroomRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new BroomModel<>(pContext.bakeLayer(BroomModel.LAYER_LOCATION));

    }

    @Override
    public ResourceLocation getTextureLocation(BroomEntity pEntity) {
        return BROOM_TEXTURE;
    }


    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0, 1.75, 0);

        pPoseStack.scale(-1.0F, -1.0F, 1.0F);

        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(pEntityYaw));
        model.setXRot(Mth.DEG_TO_RAD * pEntity.getXRot()); // We translate the model instead because the axes act weird when rotated together


        VertexConsumer vertexconsumer = pBuffer.getBuffer(model.renderType(BROOM_TEXTURE));
        model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        pPoseStack.popPose();
        renderPlayer(pPoseStack, pBuffer, pPackedLight, pEntity, 0, 0, pPartialTick, 0, 0, 0);
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    // -----------------------------------------
    // #### Rendering the player on top of the broom
    // -----------------------------------------

    public void renderPlayer(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T broom, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();

        if (!broom.getPassengers().isEmpty()) {
            Entity passenger = broom.getPassengers().get(0);

            ClientEvents.broomRiders.remove(passenger.getUUID());
            //float riderYaw = passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTicks;
            translateToBody(matrixStackIn, model, 1, broom, passenger); // TODO maybe make this only activate on needed frames? EDIT: Probably not, each animation is different and it wouldn't be worth it
            matrixStackIn.pushPose();
            //matrixStackIn.mulPose(new Quaternion(Vector3f.YP, riderYaw, true));
            renderEntity(passenger, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            ClientEvents.broomRiders.add(passenger.getUUID());

        }
        matrixStackIn.popPose();
    }

    protected void translateToBody(PoseStack stack, BroomModel model, int passengerIndex, T broom, Entity passenger) {

        // Get the rider bone, which should be present if the passenger is able to get to this spot.
        ModelPart bone = model.riderPos;
        Vector3d modelPos = new Vector3d(bone.x, bone.y, bone.z);

        // Handle x rotation,
        Vector3f vecToRotate = new Vector3f(Vec3.directionFromRotation(0, broom.getYRot()+90));
        stack.mulPose(vecToRotate.rotationDegrees(broom.getXRot()));
        // Scale by 1/16 to get from block bench coordinates to minecraft coordinates.
        modelPos.scale(0.0625f);

        // X rotation
        // Translate the player accordingly
        stack.translate(modelPos.x, modelPos.y, modelPos.z);


        //stack.mulPose(Quaternion.fromXYZ(bone.xRot, bone.yRot, bone.zRot));






        // Get the camera position placeholder bone for this passenger
        //bone = model.getBone("cameraPos" + passengerIndex).get();
        // Store it in the dragon so ClientEvents can use it in the camera event.
        //dragon.cameraBonePos.put(passenger.getUUID(), bone.getLocalPosition());

        // Create a 4d vector, then transform it with the camera's rotation matrix.
        // This gives us a close-enough estimation of how the camera should be rotating/moving.
        //Vector4f vec = new Vector4f(1f, 1f, 1f, 1f);
        //vec.transform(bone.getModelRotationMat());

        // Minus one because one is the default value of the vector if there was no rotation.
        //dragon.cameraRotVector.set(vec.x() - 1f +  dragon.getDragonXRotation(), vec.y() - 1f, vec.z() - 1f);
    }




    private <E extends Entity> void renderEntity(E entityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();

        render = manager.getRenderer(entityIn);
        matrixStack.pushPose();
        try {
            render.render(entityIn, 0, partialTicks, matrixStack, bufferIn, packedLight);
        } catch (Throwable throwable1) {
            throw new ReportedException(CrashReport.forThrowable(throwable1, "Rendering entity in world"));
        }
        matrixStack.popPose();
    }
}
