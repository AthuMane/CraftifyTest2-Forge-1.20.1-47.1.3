package com.mane.test2mod.worldgen.dimension;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.worldgen.biome.ModBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {

    // Dimension registry keys
    public static final ResourceKey<LevelStem> TEST2_KEY =
            ResourceKey.create(Registries.LEVEL_STEM,
                    new ResourceLocation(Test2Mod.MODID, "test2dim"));

    public static final ResourceKey<Level> TEST2_LEVEL_KEY =
            ResourceKey.create(Registries.DIMENSION,
                    new ResourceLocation(Test2Mod.MODID, "test2dim"));

    public static final ResourceKey<DimensionType> TEST2_DIM_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE,
                    new ResourceLocation(Test2Mod.MODID, "test2_type"));


    // Dimension type
    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(TEST2_DIM_TYPE, new DimensionType(
                OptionalLong.empty(), // normal day/night cycle
                true,  // has skylight
                false, // has ceiling
                false, // ultra warm
                true,  // natural
                1.0,   // coordinate scale
                true,  // beds work
                false, // respawn anchors work
                0,     // minY
                256,   // height
                256,   // logical height
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.0f,  // ambient light
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
        ));
    }


    // Dimension terrain + biomes
    public static void bootstrapStem(BootstapContext<LevelStem> context) {

        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator generator = new NoiseBasedChunkGenerator(

                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(

                                Pair.of(
                                        Climate.parameters(0f,0f,0f,0f,0f,0f,0f),
                                        biomes.getOrThrow(ModBiomes.TEST_BIOME)
                                ),

                                Pair.of(
                                        Climate.parameters(0.3f,0.2f,0f,0.2f,0f,0f,0f),
                                        biomes.getOrThrow(ModBiomes.TEST_BIOME_2)
                                )

                        ))
                ),

                noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD)
        );

        LevelStem stem = new LevelStem(
                dimTypes.getOrThrow(TEST2_DIM_TYPE),
                generator
        );

        context.register(TEST2_KEY, stem);
    }
}