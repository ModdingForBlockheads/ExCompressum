package net.blay09.mods.excompressum.registry.chickenstick;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumSerializers;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootTable;

public class ChickenStickRecipe extends ExCompressumRecipe<RecipeInput> {

    private final Ingredient ingredient;
    private final LootTable lootTable;

    public ChickenStickRecipe(Ingredient ingredient, LootTable lootTable) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.chickenStickRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.chickenStickRecipeType;
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
