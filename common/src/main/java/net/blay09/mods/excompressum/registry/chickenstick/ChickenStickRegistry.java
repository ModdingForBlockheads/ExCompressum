package net.blay09.mods.excompressum.registry.chickenstick;

import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class ChickenStickRegistry {

    public static List<ItemStack> rollHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        final var recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.chickenStickRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable();
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, ChickenStickRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(Level level, ItemStack itemStack) {
        return isHammerable(level.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        final var recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.chickenStickRecipeType);
        for (final var recipeHolder : recipes) {
            if (testRecipe(itemStack, recipeHolder.value())) {
                return true;
            }
        }

        return false;
    }

}
