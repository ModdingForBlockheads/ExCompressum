package net.blay09.mods.excompressum.registry.woodencrucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Objects;

public class WoodenCrucibleRecipe extends ExCompressumRecipe<RecipeInput> {

    private final Ingredient ingredient;
    private final ResourceLocation fluid;
    private final int amount;

    public WoodenCrucibleRecipe(Ingredient ingredient, ResourceLocation fluid, Integer amount) {
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ResourceLocation getFluidId() {
        return fluid;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.woodenCrucibleRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.woodenCrucibleRecipeType;
    }

    public boolean matchesFluid(Fluid fluid) {
        final var fluidId = Balm.getRegistries().getKey(fluid);
        return Objects.equals(fluidId, this.fluid);
    }

    public Fluid getFluid() {
        final var fluid = Balm.getRegistries().getFluid(this.fluid);
        return fluid != null ? fluid : Fluids.EMPTY;
    }

    public static class Serializer implements RecipeSerializer<WoodenCrucibleRecipe> {
        private static final MapCodec<WoodenCrucibleRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.ingredient),
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
                Codec.INT.fieldOf("amount").forGetter(recipe -> recipe.amount)
        ).apply(instance, WoodenCrucibleRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WoodenCrucibleRecipe> STREAM_CODEC = StreamCodec.of(Serializer::encode, Serializer::decode);

        // public static final StreamCodec<RegistryFriendlyByteBuf, WoodenCrucibleRecipe> STREAM_CODEC = StreamCodec.composite(
        //         Ingredient.CONTENTS_STREAM_CODEC.cast(), WoodenCrucibleRecipe::getIngredient,
        //         ResourceLocation.STREAM_CODEC.cast(), WoodenCrucibleRecipe::getFluid,
        //         ByteBufCodecs.INT.cast(), WoodenCrucibleRecipe::getAmount,
        //         WoodenCrucibleRecipe::new);

        @Override
        public MapCodec<WoodenCrucibleRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WoodenCrucibleRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static WoodenCrucibleRecipe decode(RegistryFriendlyByteBuf buf) {
            final var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            final var fluidId = ResourceLocation.STREAM_CODEC.decode(buf);
            final var amount = ByteBufCodecs.INT.decode(buf);
            return new WoodenCrucibleRecipe(ingredient, fluidId, amount);
        }

        private static void encode(RegistryFriendlyByteBuf buf, WoodenCrucibleRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getIngredient());
            ResourceLocation.STREAM_CODEC.encode(buf, recipe.getFluidId());
            ByteBufCodecs.INT.encode(buf, recipe.getAmount());
        }
    }
}
