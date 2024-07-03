package net.blay09.mods.excompressum.registry;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class ExCompressumRecipe<T extends RecipeInput> implements Recipe<T> {

    private final ResourceLocation id;
    private final RecipeType<?> type;

    public ExCompressumRecipe(ResourceLocation id, RecipeType<?> type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public boolean matches(T inv, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(T container, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public ResourceLocation getRecipeId() {
        return id;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public RecipeType<?> getType() {
        return type;
    }

}
