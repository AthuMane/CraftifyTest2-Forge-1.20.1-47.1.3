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
                    .sized(2f, 1.8f).build("hippo"));

    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE_ENTITTY =
            ENTITY_TYPES.register("grenade_entity",
                    () -> EntityType.Builder
                            .<GrenadeEntity>of(GrenadeEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("grenade_entity"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
