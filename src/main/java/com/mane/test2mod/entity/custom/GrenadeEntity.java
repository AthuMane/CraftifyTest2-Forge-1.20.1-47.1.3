package com.mane.test2mod.entity.custom;

import com.mane.test2mod.entity.ModEntities;
import com.mane.test2mod.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GrenadeEntity extends ThrowableItemProjectile
{

    private int fuse = 100;

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, Level level) {
        super(type, level);
    }

    public GrenadeEntity(Level level, LivingEntity thrower) {
        super(ModEntities.GRENADE_ENTITTY.get(), thrower, level);
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!level().isClientSide)
        {

            fuse--;

            if (fuse <= 0)
            {
                explode();
            }
        }
    }

    private void explode() {
        level().explode(null, getX(), getY(), getZ(), 4F, Level.ExplosionInteraction.TNT);
        discard();
    }

    @Override
    protected Item getDefaultItem()
    {
        return ModItems.GRENADE.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        this.setDeltaMovement(Vec3.ZERO);
    }
}
