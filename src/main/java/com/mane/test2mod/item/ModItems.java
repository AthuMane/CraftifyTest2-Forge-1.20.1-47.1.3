package com.mane.test2mod.item;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.entity.ModEntities;
import com.mane.test2mod.item.custom.GrenadeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Test2Mod.MODID);

    public static final RegistryObject<Item> HIPPO_SPAWN_EGG = ITEMS.register("hippo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HIPPO,0x3b2c2c,0x2b0b0b,new Item.Properties()));

    public static final RegistryObject<Item> GRENADE = ITEMS.register("grenade",
            () -> new GrenadeItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
