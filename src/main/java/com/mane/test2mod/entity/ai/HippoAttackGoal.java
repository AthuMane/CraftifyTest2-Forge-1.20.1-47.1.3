package com.mane.test2mod.entity.ai;

import com.mane.test2mod.entity.custom.HippoEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class HippoAttackGoal extends MeleeAttackGoal {

    private final HippoEntity entity;

    private final int attackAnimationLength = 24; // 1.2 seconds
    private final int attackHitTime = 20;         // hit at 1 second
    private final int attackCooldown = 10;

    private int ticksUntilNextAttack = 0;

    public HippoAttackGoal(PathfinderMob mob, double speedModifier, boolean followTarget) {
        super(mob, speedModifier, followTarget);
        this.entity = (HippoEntity) mob;
    }

    @Override
    public void start() {
        super.start();
        ticksUntilNextAttack = 0;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {

        if (distToEnemySqr <= this.getAttackReachSqr(enemy)) {

            mob.getLookControl().setLookAt(enemy.getX(), enemy.getEyeY(), enemy.getZ());

            // Start attack
            if (ticksUntilNextAttack <= 0) {
                entity.setAttacking(true);
                ticksUntilNextAttack = attackAnimationLength + attackCooldown;
            }

            // Hit moment
            if (ticksUntilNextAttack == attackAnimationLength - attackHitTime) {
                performAttack(enemy);
            }

        } else {
            entity.setAttacking(false);
        }
    }

    protected void performAttack(LivingEntity enemy) {
        mob.swing(InteractionHand.MAIN_HAND);
        mob.doHurtTarget(enemy);

        enemy.knockback(0.8F,
                mob.getX() - enemy.getX(),
                mob.getZ() - enemy.getZ());
    }

    @Override
    protected double getAttackReachSqr(LivingEntity target) {
        return 16D; // 4 block reach
    }

    @Override
    public void tick() {
        super.tick();
        ticksUntilNextAttack = Math.max(ticksUntilNextAttack - 1, -1);
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }
}