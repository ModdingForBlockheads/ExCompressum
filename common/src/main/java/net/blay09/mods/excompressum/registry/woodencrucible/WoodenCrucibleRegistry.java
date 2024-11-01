package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(Level level, ItemStack itemStack) {
        final var recipes = level.getServer().getRecipeManager().getAllRecipesFor(ModRecipeTypes.woodenCrucibleRecipeType);
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
