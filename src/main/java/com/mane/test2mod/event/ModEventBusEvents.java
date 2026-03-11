package com.mane.test2mod.event;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.entity.ModEntities;
import com.mane.test2mod.entity.client.HippoModel;
import com.mane.test2mod.entity.custom.HippoEntity;
import com.mane.test2mod.entity.layers.ModModelLayers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Test2Mod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents
{
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.HIPPO_LAYER, HippoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HIPPO.get(), HippoEntity.createAttributes().build());
    }
}
