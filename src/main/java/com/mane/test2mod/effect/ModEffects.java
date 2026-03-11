package com.mane.test2mod.effect;


import com.mane.test2mod.Test2Mod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mane.test2mod.effect.ParalysisEffect.PARALYSIS_UUID;

public class ModEffects
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Test2Mod.MODID);

   public static final RegistryObject<MobEffect> PARALYSIS_EFFECT = MOB_EFFECTS.register("paralysis",
            () -> new ParalysisEffect(MobEffectCategory.NEUTRAL, 0x9e9e9e).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    PARALYSIS_UUID, -1, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
