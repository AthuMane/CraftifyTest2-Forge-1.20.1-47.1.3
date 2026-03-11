package com.mane.test2mod.potion;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions
{
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, Test2Mod.MODID);

    public static final RegistryObject<Potion> PARALYSIS_POTION =
            POTIONS.register("paralysis_potion", () -> new Potion(new MobEffectInstance(ModEffects.PARALYSIS_EFFECT.get(), 200)));

    public static void register(IEventBus eventBus)
    {
        POTIONS.register(eventBus);
    }
}
