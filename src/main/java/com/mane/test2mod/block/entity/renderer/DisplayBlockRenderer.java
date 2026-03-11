package com.mane.test2mod.block.entity.renderer;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.block.entity.DisplayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class DisplayBlockRenderer implements BlockEntityRenderer<DisplayBlockEntity>
{

    public DisplayBlockRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    @Override
    public void render(DisplayBlockEntity entity, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay)
    {

        ItemStack stack = entity.getItem();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        if (stack.isEmpty()){return;}

            poseStack.pushPose();

            poseStack.translate(0.5f, 0.5f, 0.5f);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            float time = entity.getLevel().getGameTime() + partialTick;
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 2));

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    getLightLevel(entity.getLevel(), entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.getLevel(),
                    1
            );

            poseStack.popPose();

            Font font = Minecraft.getInstance().font;
            Component name = stack.getHoverName();

            poseStack.pushPose();

// position text above the item
            poseStack.translate(0.5, 1.3, 0.5);

// make text face the player
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

// scale text
            poseStack.scale(-0.02f, -0.02f, 0.02f);

// center text
            float width = font.width(name) / 2f;

            font.drawInBatch(
                    name,
                    -width,
                    0,
                    0xFFFFFF,
                    false,
                    poseStack.last().pose(),
                    buffer,
                    Font.DisplayMode.NORMAL,
                    0,
                    LightTexture.FULL_BRIGHT
            );

            poseStack.popPose();
        }

        private int getLightLevel (Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}