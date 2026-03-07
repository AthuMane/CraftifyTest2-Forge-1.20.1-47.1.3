package com.mane.test2mod.entity.client;

import com.google.common.collect.Maps;
import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.entity.custom.HippoEntity;
import com.mane.test2mod.entity.layers.ModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class HippoRenderer extends MobRenderer<HippoEntity, HippoModel<HippoEntity>>
{

    public HippoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HippoModel<>(pContext.bakeLayer(ModModelLayers.HIPPO_LAYER)), 1f);
    }


    @Override
    public ResourceLocation getTextureLocation(HippoEntity pEntity)
    {
        return new ResourceLocation(Test2Mod.MODID, "textures/entity/hippo.png");
    }

    @Override
    public void render(HippoEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight)
    {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.45f, 0.45f, 0.45f);
        }

        pMatrixStack.pushPose();

        pMatrixStack.scale(2F, 2F, 2F);

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

        pMatrixStack.popPose();
    }
}
