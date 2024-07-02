package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.api.HammerRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootTable;

public class HammerRecipeImpl extends ExCompressumRecipe implements HammerRecipe {

    private Ingredient input;
    private LootTable lootTable;

    public HammerRecipeImpl(ResourceLocation id, Ingredient input, LootTable lootTable) {
        super(id, ModRecipeTypes.hammerRecipeType);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.hammerRecipeSerializer;
    }

    @Override
    public Ingredient getInput() {
        return input;
    }

    @Override
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
