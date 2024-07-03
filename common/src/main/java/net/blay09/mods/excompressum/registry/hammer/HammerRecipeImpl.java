package net.blay09.mods.excompressum.registry.hammer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumSerializers;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootTable;

public class HammerRecipeImpl extends ExCompressumRecipe<RecipeInput> implements HammerRecipe {

    private final Ingredient ingredient;
    private final LootTable lootTable;

    public HammerRecipeImpl(Ingredient ingredient, LootTable lootTable) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.hammerRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.hammerRecipeType;
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    public static class Serializer implements RecipeSerializer<HammerRecipeImpl> {
        private static final MapCodec<HammerRecipeImpl> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
                LootTable.DIRECT_CODEC.fieldOf("lootTable").forGetter(recipe -> recipe.lootTable)
        ).apply(instance, HammerRecipeImpl::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, HammerRecipeImpl> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, HammerRecipeImpl::getIngredient,
                ExCompressumSerializers.LOOT_TABLE_STREAM_CODEC, HammerRecipeImpl::getLootTable,
                HammerRecipeImpl::new);

        @Override
        public MapCodec<HammerRecipeImpl> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HammerRecipeImpl> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
