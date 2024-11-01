package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public class HeavySieveRegistry {

    private static boolean testRecipe(SieveMeshRegistryEntry mesh, ItemStack itemStack, boolean waterlogged, HeavySieveRecipe recipe) {
        if (recipe.isWaterlogged() != waterlogged) {
            return false;
        }

        if (!recipe.getMeshes().isEmpty() && !recipe.getMeshes().contains(mesh.getMeshType())) {
            return false;
        }

        return recipe.getIngredient().test(itemStack);
    }

    private static boolean testGeneratedRecipe(Level level, ItemStack itemStack, GeneratedHeavySieveRecipe generatedRecipe, BlockState sieve, SieveMeshRegistryEntry sieveMesh) {
        Block sourceBlock = Balm.getRegistries().getBlock(generatedRecipe.getSourceItem());
        return generatedRecipe.getIngredient().test(itemStack) && ExNihilo.isSiftableWithMesh(level, sieve, new ItemStack(sourceBlock), sieveMesh);
    }

    public static List<ItemStack> rollSieveRewards(Level level, LootContext context, BlockState sieve, SieveMeshRegistryEntry mesh, ItemStack itemStack) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        final var recipeManager = context.getLevel().getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.heavySieveRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(mesh, itemStack, waterlogged, recipe)) {
                LootTable lootTable = recipe.getLootTable();
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        final var generatedRecipes = recipeMap.byType(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (final var recipeHolder : generatedRecipes) {
            final var recipe = recipeHolder.value();
            if (testGeneratedRecipe(level, itemStack, recipe, sieve, mesh)) {
                int rolls = getGeneratedRollCount(recipe);
                ItemLike source = Balm.getRegistries().getItem(recipe.getSourceItem());
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(level, sieve, source, rolls, mesh);
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        final var inputState = StupidUtils.getStateFromItemStack(itemStack);
        results.addAll(ExNihilo.getInstance().rollHeavySieveRewards(level, sieve, inputState, mesh, context.getLuck(), level.random));

        return results;
    }

    public static Integer getGeneratedRollCount(GeneratedHeavySieveRecipe generatedRecipe) {
        return generatedRecipe.getRolls() > 0 ? generatedRecipe.getRolls() : ExCompressumConfig.getActive().general.heavySieveDefaultRolls;
    }

    public boolean isSiftable(Level level, BlockState sieve, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        final var recipeManager = level.getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.heavySieveRecipeType);
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(sieveMesh, itemStack, waterlogged, recipe)) {
                return true;
            }
        }

        final var generatedRecipes = recipeMap.byType(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (final var recipeHolder : generatedRecipes) {
            final var recipe = recipeHolder.value();
            if (testGeneratedRecipe(level, itemStack, recipe, sieve, sieveMesh)) {
                return true;
            }
        }

        final var state = StupidUtils.getStateFromItemStack(itemStack);
        return ExNihilo.getInstance().isHeavySiftableWithMesh(sieve, state, sieveMesh);
    }

}
