package net.blay09.mods.excompressum.registry;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeImpl;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {

    public static final ResourceLocation COMPRESSED_HAMMER = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "compressed_hammer");
    public static final ResourceLocation CHICKEN_STICK = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "chicken_stick");
    public static final ResourceLocation HAMMER = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "hammer");
    public static final ResourceLocation HEAVY_SIEVE_GENERATED = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "heavy_sieve_generated");
    public static final ResourceLocation HEAVY_SIEVE = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "heavy_sieve");
    public static final ResourceLocation WOODEN_CRUCIBLE = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "wooden_crucible");

    public static RecipeType<CompressedHammerRecipeImpl> compressedHammerRecipeType;
    public static RecipeType<ChickenStickRecipe> chickenStickRecipeType;
    public static RecipeType<HammerRecipeImpl> hammerRecipeType;
    public static RecipeType<GeneratedHeavySieveRecipe> generatedHeavySieveRecipeType;
    public static RecipeType<HeavySieveRecipeImpl> heavySieveRecipeType;
    public static RecipeType<WoodenCrucibleRecipe> woodenCrucibleRecipeType;

    public static RecipeSerializer<HeavySieveRecipeImpl> heavySieveRecipeSerializer;
    public static RecipeSerializer<GeneratedHeavySieveRecipe> generatedHeavySieveRecipeSerializer;
    public static RecipeSerializer<CompressedHammerRecipeImpl> compressedHammerRecipeSerializer;
    public static RecipeSerializer<HammerRecipeImpl> hammerRecipeSerializer;
    public static RecipeSerializer<ChickenStickRecipe> chickenStickRecipeSerializer;
    public static RecipeSerializer<WoodenCrucibleRecipe> woodenCrucibleRecipeSerializer;

    public static void initialize(BalmRecipes recipes) {
        recipes.registerRecipeType(() -> compressedHammerRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return COMPRESSED_HAMMER.getPath();
            }
        }, () -> compressedHammerRecipeSerializer = new CompressedHammerRecipeImpl.Serializer(), COMPRESSED_HAMMER);
        recipes.registerRecipeType(() -> chickenStickRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return CHICKEN_STICK.getPath();
            }
        }, () -> chickenStickRecipeSerializer = new ChickenStickRecipe.Serializer(), CHICKEN_STICK);
        recipes.registerRecipeType(() -> hammerRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return HAMMER.getPath();
            }
        }, () -> hammerRecipeSerializer = new HammerRecipeImpl.Serializer(), HAMMER);
        recipes.registerRecipeType(() -> generatedHeavySieveRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return HEAVY_SIEVE_GENERATED.getPath();
            }
        }, () -> generatedHeavySieveRecipeSerializer = new GeneratedHeavySieveRecipe.Serializer(), HEAVY_SIEVE_GENERATED);
        recipes.registerRecipeType(() -> heavySieveRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return HEAVY_SIEVE.getPath();
            }
        }, () -> heavySieveRecipeSerializer = new HeavySieveRecipeImpl.Serializer(), HEAVY_SIEVE);
        recipes.registerRecipeType(() -> woodenCrucibleRecipeType = new RecipeType<>() {
            @Override
            public String toString() {
                return WOODEN_CRUCIBLE.getPath();
            }
        }, () -> woodenCrucibleRecipeSerializer = new WoodenCrucibleRecipe.Serializer(), WOODEN_CRUCIBLE);

    }
}
