package net.blay09.mods.excompressum.registry.chickenstick;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumSerializers;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.storage.loot.LootTable;

public class ChickenStickRecipe extends ExCompressumRecipe<RecipeInput> {

    private final Ingredient ingredient;
    private final LootTable lootTable;

    public ChickenStickRecipe(Ingredient ingredient, LootTable lootTable) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<ChickenStickRecipe> getSerializer() {
        return ModRecipeTypes.chickenStickRecipeSerializer;
    }

    @Override
    public RecipeType<ChickenStickRecipe> getType() {
        return ModRecipeTypes.chickenStickRecipeType;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.chickenStickRecipeBookCategory;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public static class Serializer implements RecipeSerializer<ChickenStickRecipe> {
        private static final MapCodec<ChickenStickRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
                LootTable.DIRECT_CODEC.fieldOf("lootTable").forGetter(recipe -> recipe.lootTable)
        ).apply(instance, ChickenStickRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ChickenStickRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ChickenStickRecipe::getIngredient,
                ExCompressumSerializers.LOOT_TABLE_STREAM_CODEC, ChickenStickRecipe::getLootTable,
                ChickenStickRecipe::new);

        @Override
        public MapCodec<ChickenStickRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChickenStickRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
