package com.mane.test2mod.block.entity;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Test2Mod.MODID);

    public static final RegistryObject<BlockEntityType<DisplayBlockEntity>> DISPLAY_BLOCK_BE =
            BLOCK_ENTITIES.register("display_block", () ->
                    BlockEntityType.Builder.of(DisplayBlockEntity::new, ModBlocks.DISPLAY_BLOCK.get()).build(null));


    public static void register(IEventBus bus)
            {
                BLOCK_ENTITIES.register(bus);
            }
}
