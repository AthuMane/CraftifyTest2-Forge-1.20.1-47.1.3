package com.mane.test2mod.event;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Test2Mod.MODID, value = Dist.CLIENT)
public class ModEventClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {

        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) return;

        if (player.hasEffect(ModEffects.PARALYSIS_EFFECT.get())) {
            Minecraft.getInstance().options.keyJump.setDown(false);
        }
    }
}