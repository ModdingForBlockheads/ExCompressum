package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(ModItemTags.SIEVES)
                .addOptional(sequentia("acacia_sieve"))
                .addOptional(sequentia("birch_sieve"))
                .addOptional(sequentia("dark_oak_sieve"))
                .addOptional(sequentia("jungle_sieve"))
                .addOptional(sequentia("oak_sieve"))
                .addOptional(sequentia("spruce_sieve"))
                .addOptional(sequentia("cherry_sieve"))
                .addOptional(sequentia("mangrove_sieve"))
                .addOptional(sequentia("warped_sieve"))
                .addOptional(sequentia("crimson_sieve"))
                .addOptional(deorum("acacia_sieve"))
                .addOptional(deorum("birch_sieve"))
                .addOptional(deorum("dark_oak_sieve"))
                .addOptional(deorum("jungle_sieve"))
                .addOptional(deorum("oak_sieve"))
                .addOptional(deorum("spruce_sieve"))
                .addOptional(deorum("cherry_sieve"))
                .addOptional(deorum("mangrove_sieve"))
                .addOptional(deorum("warped_sieve"))
                .addOptional(deorum("crimson_sieve"));

        final var heavySieves = getOrCreateTagBuilder(ModItemTags.HEAVY_SIEVES);
        for (Block heavySieve : ModBlocks.heavySieves) {
            heavySieves.add(heavySieve.asItem());
        }
        heavySieves.addOptional(deorum("acacia_compressed_sieve"))
                .addOptional(deorum("birch_compressed_sieve"))
                .addOptional(deorum("dark_oak_compressed_sieve"))
                .addOptional(deorum("jungle_compressed_sieve"))
                .addOptional(deorum("oak_compressed_sieve"))
                .addOptional(deorum("spruce_compressed_sieve"))
                .addOptional(deorum("cherry_compressed_sieve"))
                .addOptional(deorum("mangrove_compressed_sieve"))
                .addOptional(deorum("warped_compressed_sieve"))
                .addOptional(deorum("crimson_compressed_sieve"));

        getOrCreateTagBuilder(ModItemTags.HAMMERS).addTag(ModItemTags.WOODEN_HAMMERS)
                .addTag(ModItemTags.STONE_HAMMERS)
                .addTag(ModItemTags.IRON_HAMMERS)
                .addTag(ModItemTags.COPPER_HAMMERS)
                .addTag(ModItemTags.GOLDEN_HAMMERS)
                .addTag(ModItemTags.DIAMOND_HAMMERS)
                .addTag(ModItemTags.NETHERITE_HAMMERS)
                .addTag(ModItemTags.EXOTIC_HAMMERS);
        getOrCreateTagBuilder(ModItemTags.WOODEN_HAMMERS).addOptional(sequentia("wooden_hammer")).addOptional(deorum("wooden_hammer"));
        getOrCreateTagBuilder(ModItemTags.STONE_HAMMERS).addOptional(sequentia("stone_hammer")).addOptional(deorum("stone_hammer"));
        getOrCreateTagBuilder(ModItemTags.IRON_HAMMERS).addOptional(sequentia("iron_hammer")).addOptional(deorum("iron_hammer"));
        getOrCreateTagBuilder(ModItemTags.COPPER_HAMMERS).addOptional(sequentia("copper_hammer"));
        getOrCreateTagBuilder(ModItemTags.GOLDEN_HAMMERS).addOptional(sequentia("golden_hammer")).addOptional(deorum("golden_hammer"));
        getOrCreateTagBuilder(ModItemTags.DIAMOND_HAMMERS).addOptional(sequentia("diamond_hammer")).addOptional(deorum("diamond_hammer"));
        getOrCreateTagBuilder(ModItemTags.NETHERITE_HAMMERS).addOptional(sequentia("netherite_hammer")).addOptional(deorum("netherite_hammer"));
        getOrCreateTagBuilder(ModItemTags.EXOTIC_HAMMERS).addOptional(sequentia("bamboo_hammer"))
                .addOptional(sequentia("andesite_hammer"))
                .addOptional(sequentia("basalt_hammer"))
                .addOptional(sequentia("blackstone_hammer"))
                .addOptional(sequentia("bone_hammer"))
                .addOptional(sequentia("calcite_hammer"))
                .addOptional(sequentia("cherry_hammer"))
                .addOptional(sequentia("deepslate_hammer"))
                .addOptional(sequentia("diorite_hammer"))
                .addOptional(sequentia("dripstone_hammer"))
                .addOptional(sequentia("granite_hammer"))
                .addOptional(sequentia("nether_brick_hammer"))
                .addOptional(sequentia("red_nether_brick_hammer"))
                .addOptional(sequentia("terracotta_hammer"))
                .addOptional(sequentia("tuff_hammer"));

        final var woodenCrucibles = getOrCreateTagBuilder(ModItemTags.WOODEN_CRUCIBLES);
        for (final var woodenCrucible : ModBlocks.woodenCrucibles) {
            woodenCrucibles.add(woodenCrucible.asItem());
        }
        woodenCrucibles.addOptional(sequentia("acacia_crucible"))
                .addOptional(sequentia("birch_crucible"))
                .addOptional(sequentia("cherry_crucible"))
                .addOptional(sequentia("dark_oak_crucible"))
                .addOptional(sequentia("jungle_crucible"))
                .addOptional(sequentia("mangrove_crucible"))
                .addOptional(sequentia("oak_crucible"))
                .addOptional(sequentia("spruce_crucible"))
                .addOptional(sequentia("crimson_crucible"))
                .addOptional(sequentia("warped_crucible"));
        woodenCrucibles.addOptional(deorum("acacia_crucible"))
                .addOptional(deorum("birch_crucible"))
                .addOptional(deorum("cherry_crucible"))
                .addOptional(deorum("dark_oak_crucible"))
                .addOptional(deorum("jungle_crucible"))
                .addOptional(deorum("mangrove_crucible"))
                .addOptional(deorum("oak_crucible"))
                .addOptional(deorum("spruce_crucible"))
                .addOptional(deorum("crimson_crucible"))
                .addOptional(deorum("warped_crucible"));

        getOrCreateTagBuilder(ModItemTags.WOODEN_CROOKS).addOptional(sequentia("wooden_crook")).addOptional(deorum("crook"));

        getOrCreateTagBuilder(ModItemTags.COMPRESSED_HAMMERS)
                .add(ModItems.compressedWoodenHammer,
                        ModItems.compressedStoneHammer,
                        ModItems.compressedIronHammer,
                        ModItems.compressedDiamondHammer,
                        ModItems.compressedNetheriteHammer)
                .addOptional(deorum("compressed_wooden_hammer"))
                .addOptional(deorum("compressed_stone_hammer"))
                .addOptional(deorum("compressed_iron_hammer"))
                .addOptional(deorum("compressed_golden_hammer"))
                .addOptional(deorum("compressed_diamond_hammer"))
                .addOptional(deorum("compressed_netherite_hammer"));

        getOrCreateTagBuilder(ModItemTags.COMPRESSED_CROOKS).addTag(ModItemTags.WOODEN_COMPRESSED_CROOKS);
        getOrCreateTagBuilder(ModItemTags.WOODEN_COMPRESSED_CROOKS).add(ModItems.compressedCrook);

        getOrCreateTagBuilder(ModItemTags.CHICKEN_STICKS).add(ModItems.chickenStick);

        final var baits = getOrCreateTagBuilder(ModItemTags.BAITS);
        for (Block bait : ModBlocks.baits) {
            baits.add(bait.asItem());
        }

        getOrCreateTagBuilder(ModItemTags.CRUSHED_ANDESITES).addOptional(sequentia("crushed_andesite"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_DIORITES).addOptional(sequentia("crushed_diorite"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_GRANITES).addOptional(sequentia("crushed_granite"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_NETHERRACKS).addOptional(sequentia("crushed_netherrack")).addOptional(deorum("crushed_netherrack"));
        getOrCreateTagBuilder(ModItemTags.CRUSHED_END_STONES).addOptional(sequentia("crushed_end_stone")).addOptional(deorum("crushed_end_stone"));
        getOrCreateTagBuilder(ModItemTags.DUSTS).addOptional(sequentia("dust")).addOptional(deorum("dust"));
    }

    private static ResourceLocation sequentia(String name) {
        return ResourceLocation.fromNamespaceAndPath(Compat.EXNIHILO_SEQUENTIA, name);
    }

    private static ResourceLocation deorum(String name) {
        return ResourceLocation.fromNamespaceAndPath(Compat.EX_DEORUM, name);
    }
}
