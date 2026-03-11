package com.mane.test2mod.event;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.enchantment.ModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Test2Mod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents{

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        Player player = event.player;

        if(player.isCreative()){return;}

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        int floaterLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FLOATER.get(), player);

        int depthStriderLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.DEPTH_STRIDER, boots);

        if(floaterLevel > 0 && depthStriderLevel == 0)
        {
            if(player.isUnderWater())
            {
                Vec3 motion = player.getDeltaMovement();
                player.setDeltaMovement(motion.x, 0.1f, motion.z);
                player.setAirSupply(player.getMaxAirSupply());
            }
        }
    }
}