package com.mane.test2mod.entity;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Test2Mod.MODID);

    public static final RegistryObject<EntityType<HippoEntity>> HIPPO =
            ENTITY_TYPES.register("hippo", () -> EntityType.Builder.of(HippoEntity::new, MobCategory.CREATURE)
                    .sized(2.5f, 2.5f).build("hippo"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
