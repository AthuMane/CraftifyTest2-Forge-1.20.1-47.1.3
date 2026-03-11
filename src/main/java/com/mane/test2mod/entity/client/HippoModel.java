// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package com.mane.test2mod.entity.client;


import com.mane.test2mod.entity.animations.HippoAnimationDefinitions;
import com.mane.test2mod.entity.custom.HippoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class HippoModel<T extends Entity> extends HierarchicalModel<T>
{
	private final ModelPart hippo;
	private final ModelPart Body;
	private final ModelPart Body2;
	private final ModelPart Tail;
	private final ModelPart Head;
	private final ModelPart Head2;
	private final ModelPart Jaw;
	private final ModelPart Jaw2;
	private final ModelPart Ears;
	private final ModelPart Ear1;
	private final ModelPart Ear2;
	private final ModelPart Paws;
	private final ModelPart Paw1;
	private final ModelPart Paw2;
	private final ModelPart Paw3;
	private final ModelPart Paw4;

	public HippoModel(ModelPart root) {
		this.hippo = root.getChild("hippo");
		this.Body = this.hippo.getChild("Body");
		this.Body2 = this.Body.getChild("Body2");
		this.Tail = this.Body.getChild("Tail");
		this.Head = this.Body.getChild("Head");
		this.Head2 = this.Head.getChild("Head2");
		this.Jaw = this.Head.getChild("Jaw");
		this.Jaw2 = this.Jaw.getChild("Jaw2");
		this.Ears = this.Head.getChild("Ears");
		this.Ear1 = this.Ears.getChild("Ear1");
		this.Ear2 = this.Ears.getChild("Ear2");
		this.Paws = this.hippo.getChild("Paws");
		this.Paw1 = this.Paws.getChild("Paw1");
		this.Paw2 = this.Paws.getChild("Paw2");
		this.Paw3 = this.Paws.getChild("Paw3");
		this.Paw4 = this.Paws.getChild("Paw4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hippo = partdefinition.addOrReplaceChild("hippo", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 0.0F));

		PartDefinition Body = hippo.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.0F));

		PartDefinition Body2 = Body.addOrReplaceChild("Body2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.6667F, -8.1667F, 10.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-4.0F, -5.6667F, -7.1667F, 8.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(60, 19).addBox(-4.0F, -4.6667F, -9.1667F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.3333F, 1.1667F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, -11.543F, 8.682F));

		PartDefinition Tail_r1 = Tail.addOrReplaceChild("Tail_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-1.0F, -1.0858F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2248F, 0.2929F, 0.5236F, 0.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -8.0F));

		PartDefinition Head2 = Head.addOrReplaceChild("Head2", CubeListBuilder.create().texOffs(30, 25).addBox(-3.0F, -3.5F, -4.5F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -0.5F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(39, 1).addBox(-3.0F, -6.0F, -7.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -2.0F));

		PartDefinition Jaw2 = Jaw.addOrReplaceChild("Jaw2", CubeListBuilder.create().texOffs(39, 39).addBox(-3.0F, -1.0F, -4.8333F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(37, 11).addBox(-2.0F, -2.0F, -3.8333F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 34).addBox(1.0F, -2.0F, -3.8333F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.1667F));

		PartDefinition Ears = Head.addOrReplaceChild("Ears", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 7.0F));

		PartDefinition Ear1 = Ears.addOrReplaceChild("Ear1", CubeListBuilder.create().texOffs(9, 0).addBox(0.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -13.0F, -8.5F));

		PartDefinition Ear2 = Ears.addOrReplaceChild("Ear2", CubeListBuilder.create().texOffs(9, 0).addBox(-2.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -13.0F, -8.5F));

		PartDefinition Paws = hippo.addOrReplaceChild("Paws", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.0F));

		PartDefinition Paw1 = Paws.addOrReplaceChild("Paw1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -4.5F, -4.5F));

		PartDefinition Paw2 = Paws.addOrReplaceChild("Paw2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -4.5F, -4.5F));

		PartDefinition Paw3 = Paws.addOrReplaceChild("Paw3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -4.5F, 6.5F));

		PartDefinition Paw4 = Paws.addOrReplaceChild("Paw4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -4.0F, 6.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.hippo.getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation((HippoEntity) entity, netHeadYaw, headPitch, ageInTicks);

		float eatAngle = ((HippoEntity) entity).getHeadEatAngleScale(ageInTicks);

		this.Head.xRot = eatAngle;

		if (((HippoEntity) entity).eatAnimationTick > 0)
		{
			this.Jaw2.xRot = 0.35F * Mth.sin(ageInTicks * 0.45F);
		}

		if(((HippoEntity) entity).isAngry())
		{
			this.animateWalk(HippoAnimationDefinitions.HIPPO_CHASE, limbSwing, limbSwingAmount, 2f, 1.5f);
		}
		else
		{
			this.animateWalk(HippoAnimationDefinitions.HIPPO_WALK, limbSwing, limbSwingAmount, 2f, 1.5f);
		}
		this.animate(((HippoEntity) entity).idleAnimationState, HippoAnimationDefinitions.HIPPO_IDLE, ageInTicks, 0.5f);
		this.animate(((HippoEntity) entity).attackAnimationState, HippoAnimationDefinitions.HIPPO_ATTACK, ageInTicks, 1f);
	}


	private void applyHeadRotation(HippoEntity pEntity, float pNetHeadYaw, float pHeadPitch, float pAgeInTicks)
	{
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hippo.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root()
	{
		return hippo;
	}

}