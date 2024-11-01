package net.blay09.mods.excompressum.block;

import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModBlocks {

    public static Block[] compressedBlocks;
    public static Block[] heavySieves;
    public static Block[] woodenCrucibles;
    public static Block[] baits;
    public static Block autoHammer;
    public static Block autoCompressedHammer;
    public static Block autoHeavySieve;
    public static Block autoSieve;
    public static Block autoCompressor;
    public static Block rationingAutoCompressor;

    public static void initialize(BalmBlocks blocks) {
        blocks.register((identifier) -> autoHammer = new AutoHammerBlock(defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id("auto_hammer"));
        blocks.register((identifier) -> autoSieve = new AutoSieveBlock(defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id("auto_sieve"));
        blocks.register((identifier) -> autoCompressedHammer = new AutoCompressedHammerBlock(defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id("auto_compressed_hammer"));
        blocks.register((identifier) -> autoHeavySieve = new AutoHeavySieveBlock(defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id("auto_heavy_sieve"));
        blocks.register((identifier) -> autoCompressor = new AutoCompressorBlock(defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id("auto_compressor"));
        blocks.register((identifier) -> rationingAutoCompressor = new RationingAutoCompressorBlock(defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id("rationing_auto_compressor"));

        compressedBlocks = registerEnumBlock(blocks, CompressedBlockType.values(), it -> "compressed_" + it, CompressedBlock::new);
        heavySieves = registerEnumBlock(blocks, HeavySieveType.values(), it -> it + "_heavy_sieve", HeavySieveBlock::new);
        woodenCrucibles = registerEnumBlock(blocks, WoodenCrucibleType.values(), it -> it + "_crucible", WoodenCrucibleBlock::new);
        baits = registerEnumBlock(blocks, BaitType.values(), it -> it + "_bait", BaitBlock::new);
    }

    private static <T extends Enum<T> & StringRepresentable> Block[] registerEnumBlock(BalmBlocks blocks, T[] types, Function<String, String> nameFactory, BiFunction<T, BlockBehaviour.Properties, Block> factory) {
        Block[] blockArray = new Block[types.length];
        for (T type : types) {
            blocks.register((identifier) -> blockArray[type.ordinal()] = factory.apply(type, defaultProperties(identifier)), (block, identifier) -> new BlockItem(block, itemProperties(identifier)), id(nameFactory.apply(type.getSerializedName())));
        }

        return blockArray;
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }

    private static BlockBehaviour.Properties defaultProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.of().setId(blockId(identifier));
    }

    private static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }

    private static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }

    private static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }
}
