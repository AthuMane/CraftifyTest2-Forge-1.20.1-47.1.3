package com.mane.test2mod.entity.custom;

import com.mane.test2mod.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class HippoEntity extends Animal implements NeutralMob {

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(HippoEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Set<Block> HIPPO_FOOD_BLOCKS = Set.of(
            Blocks.GRASS,
            Blocks.PUMPKIN,
            Blocks.MELON,
            Blocks.HAY_BLOCK
    );

    public static final Set<Item> HIPPO_FOOD_ITEMS = Set.of(
            Items.PUMPKIN,
            Items.MELON,
            Items.HAY_BLOCK
    );

    public static final Ingredient HIPPO_FOOD_INGREDIENT = Ingredient.of(
            Items.PUMPKIN,
            Items.MELON,
            Items.HAY_BLOCK
    );

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public int eatAnimationTick;

    private int warningSoundTicks;

    private int disturbanceLevel = 0;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(15, 20);
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;

    public HippoEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1f);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HippoAttackGoal(this, 1.0D, true));

        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new HippoTemptGoal(this, 1.0D, HIPPO_FOOD_INGREDIENT, false));

        this.goalSelector.addGoal(3, new HippoSearchForFoodItemsGoal());
        this.goalSelector.addGoal(4, new HippoSearchFoodBlockGoal(this, 1f, 8));

        this.goalSelector.addGoal(5, new HippoFindWaterGoal(this));
        this.goalSelector.addGoal(5, new HippoNightStrollGoal(this, 1f));

        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1));

        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6f));

        this.targetSelector.addGoal(1, new HippoHurtByTargetGoal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.FOLLOW_RANGE, 60D)
                .add(Attributes.ATTACK_DAMAGE, 12D)
                .add(Attributes.ATTACK_KNOCKBACK, 4D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .add(Attributes.ARMOR_TOUGHNESS, 1f);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return ModEntities.HIPPO.get().create(level);
    }

    private void setupAnimationStates() {

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 100;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 24;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            attackAnimationState.stop();
        }
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(pId);
        }

    }

    public float getHeadEatAngleScale(float partialTick) {
        if (this.eatAnimationTick > 0) {
            float f = ((float)this.eatAnimationTick - partialTick) / 40.0F;
            return 0.15F * Mth.sin(f * 6.0F);
        } else {
            return this.getXRot() * ((float)Math.PI / 180F);
        }
    }

    private boolean isBabyNearby() {
        List<HippoEntity> list = this.level().getEntitiesOfClass(
                HippoEntity.class,
                this.getBoundingBox().inflate(8.0D),
                e -> e.isBaby()
        );
        return !list.isEmpty();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.eatAnimationTick > 0) {
            --this.eatAnimationTick;
        }

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            return;
        }


        if (!this.isBaby()) {

            Player player = this.level().getNearestPlayer(this, 5.0D);

            if (player != null && !player.isCreative() && !player.isSpectator()) {

                if (isBabyNearby()) {
                    this.setTarget(player);
                    this.playWarningSound();
                    return;
                }

                disturbanceLevel++;

                if (disturbanceLevel > 200) {
                    this.setTarget(player);
                    disturbanceLevel = 0;
                }

            } else {
                disturbanceLevel = Math.max(0, disturbanceLevel - 1);
            }
        }

        this.updatePersistentAnger((ServerLevel)this.level(), true);
    }

    @Override
    public boolean isAngryAt(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        if (player.isCreative() || player.isSpectator()) {
            return false;
        }

        return this.distanceTo(player) < 6.0F;
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.POLAR_BEAR_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEvents.POLAR_BEAR_WARNING, 1.0F, this.getVoicePitch());
            this.warningSoundTicks = 40;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return HIPPO_FOOD_ITEMS.contains(stack.getItem());
    }

    @Override
    public boolean isAngry()
    {
        return this.getTarget() != null;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    class HippoHurtByTargetGoal extends HurtByTargetGoal {
        public HippoHurtByTargetGoal() {
            super(HippoEntity.this);
        }

        public void start() {
            super.start();
            if (HippoEntity.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }
        }

        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof HippoEntity && !mob.isBaby()) {
                super.alertOther(mob, target);
            }
        }
    }

    class HippoAttackGoal extends MeleeAttackGoal {

        private final int attackAnimationLength = 24;
        private final int attackHitTime = 20;
        private final int attackCooldown = 10;

        private int ticksUntilNextAttack = 0;

        public HippoAttackGoal(PathfinderMob mob, double speed, boolean follow) {
            super(mob, speed, follow);
        }

        @Override
        public void start() {
            super.start();
            ticksUntilNextAttack = 0;
        }

        protected void checkAndPerformAttack(LivingEntity enemy, double dist)
        {
            if(mob.getTarget() != null)
            {
                mob.getLookControl().setLookAt(enemy.getX(), enemy.getEyeY(), enemy.getZ());
            }
            if (dist <= this.getAttackReachSqr(enemy))
            {
                if (ticksUntilNextAttack <= 0) {
                    HippoEntity.this.setAttacking(true);
                    ticksUntilNextAttack = attackAnimationLength + attackCooldown;
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    HippoEntity.this.playWarningSound();
                }

                if (ticksUntilNextAttack == attackAnimationLength - attackHitTime) {
                    performAttack(enemy);
                }

            } else {
                HippoEntity.this.setAttacking(false);
            }
        }

        protected void performAttack(LivingEntity enemy) {
            mob.swing(InteractionHand.MAIN_HAND);
            mob.doHurtTarget(enemy);
        }

        protected double getAttackReachSqr(LivingEntity target) {
            return 12.25D;
        }

        public void tick() {
            super.tick();
            ticksUntilNextAttack = Math.max(ticksUntilNextAttack - 1, -1);
        }

        public void stop() {
            HippoEntity.this.setAttacking(false);
            super.stop();
        }
    }

    class HippoFindWaterGoal extends TryFindWaterGoal {

        PathfinderMob pMob;

        public HippoFindWaterGoal(PathfinderMob pMob)
        {
            super(pMob);
            this.pMob = pMob;
        }

        @Override
        public boolean canUse()
        {
            return level().isDay() ? super.canUse() : false;
        }

        @Override
        public boolean canContinueToUse()
        {
            return !this.pMob.getNavigation().isDone();
        }

        @Override
        public void start()
        {
            BlockPos waterTarget = null;

            for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(this.pMob.getX() - 16.0D),
                    Mth.floor(this.pMob.getY() - 16.0D), Mth.floor(this.pMob.getZ() - 6.0D),
                    Mth.floor(this.pMob.getX() + 16.0D), this.pMob.getBlockY(),
                    Mth.floor(this.pMob.getZ() + 16.0D)))
            {
                if (this.pMob.level().getFluidState(blockpos).is(FluidTags.WATER))
                {
                    waterTarget = blockpos;
                    break;
                }
            }

            if (waterTarget != null)
            {
                this.pMob.getNavigation().moveTo((double)waterTarget.getX(), (double)waterTarget.getY(), (double)waterTarget.getZ(), 1.0D);
            }

        }

        @Override
        public void tick()
        {
            if(this.pMob.getNavigation().isStuck())
            {
                stop();
                start();
            }
        }

        @Override
        public void stop()
        {
            this.pMob.getNavigation().stop();
        }
    }

    class HippoNightStrollGoal extends WaterAvoidingRandomStrollGoal {
        PathfinderMob pMob;

        public HippoNightStrollGoal(PathfinderMob pMob, double pSpeedModifier)
        {
            super(pMob, pSpeedModifier, 0.9f);
            this.pMob =  pMob;
        }

        @Override
        public boolean canUse()
        {
            return level().isNight() ? super.canUse() : false;
        }
    }

    class HippoSearchFoodBlockGoal extends MoveToBlockGoal {
        protected int ticksWaited;

        public HippoSearchFoodBlockGoal(PathfinderMob pMob, double pSpeedModifier, int pSearchRange)
        {
            super(pMob, pSpeedModifier, pSearchRange);
        }

        public double acceptedDistance() {
            return 4.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        public boolean canUse()
        {
            return level().isNight() && super.canUse();
        }

        @Override
        public void tick()
        {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40)
                {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }

            super.tick();
        }

        @Override
        protected boolean isReachedTarget()
        {
            return super.isReachedTarget();
        }

        private void eatBlockFood()
        {
            HippoEntity.this.level().broadcastEntityEvent(HippoEntity.this, (byte)10);

            HippoEntity.this.playSound(SoundEvents.HORSE_EAT, 1.0F, 1.0F);
            HippoEntity.this.level().removeBlock(blockPos, false);

            this.mob.ate();
        }

        protected void onReachedTarget()
        {
            BlockState blockstate = HippoEntity.this.level().getBlockState(this.blockPos);

            if (HIPPO_FOOD_BLOCKS.contains(blockstate.getBlock())) {
                this.eatBlockFood();
            }
        }

        public void start() {
            this.ticksWaited = 0;
            super.start();
        }

        @Override
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos)
        {
            return HIPPO_FOOD_BLOCKS.contains(pLevel.getBlockState(pPos).getBlock());
        }
    }

    class HippoSearchForFoodItemsGoal extends Goal {

        private ItemEntity targetItem;

        public HippoSearchForFoodItemsGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (HippoEntity.this.getTarget() == null && HippoEntity.this.getLastHurtByMob() == null)
            {
                if (HippoEntity.this.getRandom().nextInt(reducedTickDelay(10)) != 0)
                {
                    return false;
                } else {
                    List<ItemEntity> list = HippoEntity.this.level().getEntitiesOfClass(ItemEntity.class, HippoEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
                    return !list.isEmpty();
                }
            } else {
                return false;
            }
        }


        public void tick() {

            if (this.targetItem == null || !this.targetItem.isAlive()) {
                return;
            }

            HippoEntity.this.getNavigation().moveTo(this.targetItem, 1.2D);

            if (HippoEntity.this.distanceTo(this.targetItem) < 2.0F) {

                HippoEntity.this.playSound(SoundEvents.HORSE_EAT, 1.0F, 1.0F);

                HippoEntity.this.level().broadcastEntityEvent(HippoEntity.this, (byte)10);

                HippoEntity.this.ate();

                this.targetItem.getItem().shrink(1);

                if (this.targetItem.getItem().isEmpty()) {
                    this.targetItem.discard();
                }
            }
        }

        public void start() {

            List<ItemEntity> list = HippoEntity.this.level().getEntitiesOfClass(
                    ItemEntity.class,
                    HippoEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
                    item -> HIPPO_FOOD_ITEMS.contains(item.getItem().getItem())
            );

            this.targetItem = list.stream()
                    .min(Comparator.comparingDouble(HippoEntity.this::distanceToSqr))
                    .orElse(null);

            if (this.targetItem != null) {
                HippoEntity.this.getNavigation().moveTo(this.targetItem, 1.2D);
            }
        }

        public void stop() {
            this.targetItem = null;
        }
    }

    class HippoTemptGoal extends TemptGoal {

        public HippoTemptGoal(PathfinderMob pMob, double pSpeedModifier, Ingredient pItems, boolean pCanScare)
        {
            super(pMob, pSpeedModifier, pItems, pCanScare);
        }

        @Override
        public boolean canUse() {
            if (((HippoEntity)this.mob).isAngry()) {
                return false;
            }
            return super.canUse();
        }
    }
}