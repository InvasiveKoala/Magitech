package com.github.invasivekoala.magitech.client.entity;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.entities.BroomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BroomModel<T extends BroomEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Magitech.MOD_ID, "broomstick"), "main");
	public final ModelPart broomstick;
	public final ModelPart riderPos;

	public BroomModel(ModelPart root) {
		this.broomstick = root.getChild("broomstick");
		this.riderPos = this.broomstick.getChild("riderPos");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition broomstick = partdefinition.addOrReplaceChild("broomstick", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, -5.0F, 3.0F, 3.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(-1.5F, -4.0F, -18.0F, 3.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(-3.5F, -5.0F, 19.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition riderPos = broomstick.addOrReplaceChild("riderPos", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setXRot(float rot){
		broomstick.setRotation(rot, 0, 0);
		riderPos.setRotation(rot, 0, 0);
	}

	@Override
	public void setupAnim(BroomEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		broomstick.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}