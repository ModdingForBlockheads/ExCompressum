package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public class ModItems {
    public static DeferredObject<CreativeModeTab> creativeModeTab;

    public static Item chickenStick;
    public static Item compressedWoodenHammer;
    public static Item compressedStoneHammer;
    public static Item compressedIronHammer;
    public static Item compressedGoldenHammer;
    public static Item compressedDiamondHammer;
    public static Item compressedNetheriteHammer;
    public static Item compressedCrook;
    public static Item ironMesh;
    public static Item woodChippings;
    public static Item uncompressedCoal;
    public static Item batZapper;
    public static Item oreSmasher;
    public static Item uglySteelPlating;

    public static void initialize(BalmItems items) {
        items.registerItem((identifier) -> chickenStick = new ChickenStickItem(itemProperties(identifier)), id("chicken_stick"));
        items.registerItem((identifier) -> compressedWoodenHammer = new CompressedHammerItem(ToolMaterial.WOOD, 6f, -3.2f, itemProperties(identifier)), id("compressed_wooden_hammer"));
        items.registerItem((identifier) -> compressedStoneHammer = new CompressedHammerItem(ToolMaterial.STONE, 7f, -3.2f, itemProperties(identifier)), id("compressed_stone_hammer"));
        items.registerItem((identifier) -> compressedIronHammer = new CompressedHammerItem(ToolMaterial.IRON, 6f, -3.1f, itemProperties(identifier)), id("compressed_iron_hammer"));
        items.registerItem((identifier) -> compressedGoldenHammer = new CompressedHammerItem(ToolMaterial.GOLD, 6f, -3f, itemProperties(identifier)), id("compressed_golden_hammer"));
        items.registerItem((identifier) -> compressedDiamondHammer = new CompressedHammerItem(ToolMaterial.DIAMOND, 5f, -3f, itemProperties(identifier)), id("compressed_diamond_hammer"));
        items.registerItem((identifier) -> compressedNetheriteHammer = new CompressedHammerItem(ToolMaterial.NETHERITE, 5f, -3f, itemProperties(identifier)),
                id("compressed_netherite_hammer"));
        items.registerItem((identifier) -> compressedCrook = new CompressedCrookItem(itemProperties(identifier)), id("compressed_crook"));
        items.registerItem((identifier) -> ironMesh = new IronMeshItem(itemProperties(identifier)), id("iron_mesh"));
        items.registerItem((identifier) -> woodChippings = new WoodChippingItem(itemProperties(identifier)), id("wood_chippings"));
        items.registerItem((identifier) -> {
            uncompressedCoal = new UncompressedCoalItem(itemProperties(identifier));
            Balm.getHooks().setBurnTime(uncompressedCoal, 200);
            return uncompressedCoal;
        }, id("uncompressed_coal"));
        items.registerItem((identifier) -> batZapper = new BatZapperItem(itemProperties(identifier)), id("bat_zapper"));
        items.registerItem((identifier) -> oreSmasher = new OreSmasherItem(itemProperties(identifier)), id("ore_smasher"));
        items.registerItem((identifier) -> uglySteelPlating = new UglySteelPlatingItem(itemProperties(identifier)), id("ugly_steel_plating"));

        creativeModeTab = items.registerCreativeModeTab(() -> new ItemStack(ModItems.compressedDiamondHammer), id("excompressum"));
    }

    private static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }

    private static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }
    
    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }

}
