package net.blay09.mods.excompressum.registry.compressedhammer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumSerializers;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.storage.loot.LootTable;

public class CompressedHammerRecipeImpl extends ExCompressumRecipe<RecipeInput> implements CompressedHammerRecipe {

    private final Ingredient ingredient;
    private final LootTable lootTable;

    public CompressedHammerRecipeImpl(Ingredient ingredient, LootTable lootTable) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeType<CompressedHammerRecipeImpl> getType() {
        return ModRecipeTypes.compressedHammerRecipeType;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.compressedHammerRecipeBookCategory;
    }

    @Override
    public RecipeSerializer<CompressedHammerRecipeImpl> getSerializer() {
        return ModRecipeTypes.compressedHammerRecipeSerializer;
    }



    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    public static class Serializer implements RecipeSerializer<CompressedHammerRecipeImpl> {
        private static final MapCodec<CompressedHammerRecipeImpl> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
                LootTable.DIRECT_CODEC.fieldOf("lootTable").forGetter(recipe -> recipe.lootTable)
        ).apply(instance, CompressedHammerRecipeImpl::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CompressedHammerRecipeImpl> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, CompressedHammerRecipeImpl::getIngredient,
                ExCompressumSerializers.LOOT_TABLE_STREAM_CODEC, CompressedHammerRecipeImpl::getLootTable,
                CompressedHammerRecipeImpl::new);

        @Override
        public MapCodec<CompressedHammerRecipeImpl> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CompressedHammerRecipeImpl> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
