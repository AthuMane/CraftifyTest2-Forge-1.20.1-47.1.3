package com.mane.test2mod.item;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Test2Mod.MODID);

    public static final RegistryObject<CreativeModeTab> TEST2 = CREATIVE_MODE_TABS.register("test2_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GRENADE.get()))
                    .title(Component.translatable("creativetab.test2_tab"))
                    .displayItems((displayParameters, output) -> {
                        output.accept(ModItems.HIPPO_SPAWN_EGG.get());
                        output.accept(ModBlocks.DISPLAY_BLOCK.get());
                        output.accept(ModItems.GRENADE.get());
                        output.accept(ModBlocks.TEST_PORTAL.get());
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
