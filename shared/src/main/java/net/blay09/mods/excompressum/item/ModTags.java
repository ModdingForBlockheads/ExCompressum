package net.blay09.mods.excompressum.item;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static TagKey<Block> MINEABLE_WITH_HAMMER;
    public static TagKey<Block> MINEABLE_WITH_CROOK;
    public static TagKey<Item> HAMMERS;
    public static TagKey<Item> COMPRESSED_HAMMERS;
    public static TagKey<Item> CHICKEN_STICKS;
    public static TagKey<Item> COMPRESSED_CROOKS;

    public static void initialize(BalmRegistries registries) {
        MINEABLE_WITH_HAMMER = TagKey.create(Registries.BLOCK, new ResourceLocation("excompressum", "mineable/hammer"));
        MINEABLE_WITH_CROOK = TagKey.create(Registries.BLOCK, new ResourceLocation("excompressum", "mineable/crook"));
        HAMMERS = registries.getItemTag(new ResourceLocation("excompressum", "hammers"));
        COMPRESSED_HAMMERS = registries.getItemTag(new ResourceLocation("excompressum", "compressed_hammers"));
        CHICKEN_STICKS = registries.getItemTag(new ResourceLocation("excompressum", "chicken_sticks"));
        COMPRESSED_CROOKS = registries.getItemTag(new ResourceLocation("excompressum", "compressed_crooks"));
    }
}
