package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record CompressedRecipe(ResourceLocation id, Ingredient ingredient, int count, ItemStack resultStack) {
}
