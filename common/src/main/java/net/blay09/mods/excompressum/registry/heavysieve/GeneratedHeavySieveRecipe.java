package net.blay09.mods.excompressum.registry.heavysieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

public class GeneratedHeavySieveRecipe extends ExCompressumRecipe<RecipeInput> {

    private final Ingredient ingredient;
    private final ResourceLocation sourceItem;
    private final int rolls;

    public GeneratedHeavySieveRecipe(Ingredient ingredient, ResourceLocation sourceItem, int rolls) {
        this.ingredient = ingredient;
        this.sourceItem = sourceItem;
        this.rolls = rolls;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ResourceLocation getSourceItem() {
        return sourceItem;
    }

    public int getRolls() {
        return rolls;
    }

    @Override
    public RecipeSerializer<GeneratedHeavySieveRecipe> getSerializer() {
        return ModRecipeTypes.generatedHeavySieveRecipeSerializer;
    }

    @Override
    public RecipeType<GeneratedHeavySieveRecipe> getType() {
        return ModRecipeTypes.generatedHeavySieveRecipeType;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.generatedHeavySieveRecipeBookCategory;
    }

    public static class Serializer implements RecipeSerializer<GeneratedHeavySieveRecipe> {
        private static final MapCodec<GeneratedHeavySieveRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
                ResourceLocation.CODEC.fieldOf("source").forGetter(recipe -> recipe.sourceItem),
                Codec.INT.fieldOf("rolls").orElse(-1).forGetter(recipe -> recipe.rolls)
        ).apply(instance, GeneratedHeavySieveRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, GeneratedHeavySieveRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, GeneratedHeavySieveRecipe::getIngredient,
                ResourceLocation.STREAM_CODEC, GeneratedHeavySieveRecipe::getSourceItem,
                ByteBufCodecs.INT, GeneratedHeavySieveRecipe::getRolls,
                GeneratedHeavySieveRecipe::new);

        @Override
        public MapCodec<GeneratedHeavySieveRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GeneratedHeavySieveRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
