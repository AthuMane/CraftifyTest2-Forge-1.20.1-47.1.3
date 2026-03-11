package com.mane.test2mod.item.custom;

import com.mane.test2mod.entity.custom.GrenadeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class GrenadeItem extends Item {

    public static final String FUSE_START = "fuse_start";
    public static final int MAX_FUSE = 100;

    public GrenadeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        var tag = stack.getOrCreateTag();

        if (player.isShiftKeyDown() && !tag.contains(FUSE_START)) {


            tag.putLong(FUSE_START, level.getGameTime());

            level.playSound(null, player.blockPosition(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.PLAYERS,
                    1F, 1F);

            return InteractionResultHolder.success(stack);
        }

        int remainingFuse = MAX_FUSE;

        if (tag.contains(FUSE_START)) {
            long start = tag.getLong(FUSE_START);
            long held = level.getGameTime() - start;
            remainingFuse = (int)(MAX_FUSE - held);
        }

        if (!level.isClientSide) {

            GrenadeEntity grenade = new GrenadeEntity(level, player);
            grenade.setFuse(Math.max(remainingFuse, 1));

            grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.3F, 1.0F);
            level.addFreshEntity(grenade);
        }

        tag.remove(FUSE_START);
        stack.shrink(1);

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {

        if (!stack.hasTag() || !stack.getTag().contains(FUSE_START))
            return;

        long start = stack.getTag().getLong(FUSE_START);
        long elapsed = level.getGameTime() - start;

        if (elapsed >= MAX_FUSE) {

            if (!level.isClientSide) {
                level.explode(
                        null,
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        4F,
                        Level.ExplosionInteraction.TNT
                );
            }

            stack.shrink(1);
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {

        Level level = entity.level();

        if (!stack.hasTag() || !stack.getTag().contains(FUSE_START))
            return false;

        long start = stack.getTag().getLong(FUSE_START);
        long elapsed = level.getGameTime() - start;

        if (elapsed >= MAX_FUSE) {

            if (!level.isClientSide) {
                level.explode(
                        null,
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        4F,
                        Level.ExplosionInteraction.TNT
                );
            }

            entity.discard();
        }

        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(FUSE_START);
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        if (!stack.hasTag() || !stack.getTag().contains(FUSE_START))
            return 13;

        long start = stack.getTag().getLong(FUSE_START);
        long elapsed = Minecraft.getInstance().level.getGameTime() - start;

        float remaining = (float)(MAX_FUSE - elapsed) / MAX_FUSE;
        remaining = Math.max(0F, Math.min(1F, remaining));

        return Math.round(13 * remaining);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF4444;
    }
}
