package com.mane.test2mod.enchantment;

import com.mane.test2mod.Test2Mod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Test2Mod.MODID);

    public static final RegistryObject<Enchantment> FLOATER =
            ENCHANTMENTS.register("floater", () -> new FloaterEnchantment(
                    Enchantment.Rarity.UNCOMMON ,EnchantmentCategory.ARMOR,
            new EquipmentSlot[]{ EquipmentSlot.HEAD, EquipmentSlot.CHEST}));

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
