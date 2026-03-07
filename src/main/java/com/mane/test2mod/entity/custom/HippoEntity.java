package com.mane.test2mod.entity.custom;

import com.mane.test2mod.entity.ModEntities;
import com.mane.test2mod.entity.ai.HippoAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public class HippoEntity extends Animal
{
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(HippoEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public HippoEntity(EntityType<? extends Animal> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(0, new HippoAttackGoal(this, 1f, false));

        this.goalSelector.addGoal(1,new BreedGoal(this,1.0D));
        this.goalSelector.addGoal(1,new TemptGoal(this,1.0D, Ingredient.of(Items.APPLE), true));

        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1d));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6f));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 50D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.FOLLOW_RANGE, 50D)
                .add(Attributes.ARMOR_TOUGHNESS, 1f)
                .add(Attributes.ATTACK_DAMAGE, 10f)
                .add(Attributes.ATTACK_KNOCKBACK, 3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1f);

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.HIPPO.get().create(pLevel);
    }

    private void setupAnimationStates()
    {
        if (this.idleAnimationTimeout <= 0)
        {
            this.idleAnimationTimeout = this.random.nextInt(100) + 200;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if(this.isAttacking() && attackAnimationTimeout <= 0)
        {
            attackAnimationTimeout = 24;
            attackAnimationState.start(this.tickCount);
        }else
        {
            --this.idleAnimationTimeout;
        }

        if(!this.isAttacking())
        {
            attackAnimationState.stop();
        }
    }

    protected void updateWalkAnimation(float v)
    {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide())
        {
            this.setupAnimationStates();
        }
    }

    public void setAttacking(boolean attacking)
    {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking()
    {
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }


    /* SOUNDS */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    /* BREEDING */

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.APPLE);
    }
}
