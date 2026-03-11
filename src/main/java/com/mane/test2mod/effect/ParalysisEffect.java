package com.mane.test2mod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

    public class ParalysisEffect extends MobEffect
    {

        public static final String PARALYSIS_UUID = "57a07fe1-5629-42a3-8756-639797379751";

        protected ParalysisEffect(MobEffectCategory pCategory, int pColor)
        {
            super(pCategory, pColor);
        }


        @Override
        public void applyEffectTick(LivingEntity entity, int amplifier) {

            entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);

            if (entity instanceof Player player) {
                player.setJumping(false);
            }

            if (entity.getDeltaMovement().y > 0) {
                entity.setDeltaMovement(0, 0, 0);
            }

            super.applyEffectTick(entity, amplifier);
        }

        @Override
        public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
            super.addAttributeModifiers(entity, map, amplifier);

            MobEffectInstance instance = entity.getEffect(this);

            if (instance != null) {

                int duration = instance.getDuration();
                int resistanceDuration = duration / 2;

                entity.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE,
                        resistanceDuration,
                        0
                ));
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return true;
        }
    }
