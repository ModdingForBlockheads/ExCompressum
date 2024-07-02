package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootTable;

public class CompressedHammerRecipe extends ExCompressumRecipe {


    private Ingredient input;
    private LootTable lootTable;

    public CompressedHammerRecipe(ResourceLocation id, Ingredient input, LootTable lootTable) {
        super(id, ModRecipeTypes.compressedHammerRecipeType);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.compressedHammerRecipeSerializer;
    }

    public Ingredient getInput() {
        return input;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }
}
