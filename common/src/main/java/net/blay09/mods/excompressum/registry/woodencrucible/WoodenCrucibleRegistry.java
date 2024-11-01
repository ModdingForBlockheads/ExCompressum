package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(Level level, ItemStack itemStack) {
        final var recipeManager = level.getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.woodenCrucibleRecipeType);
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
