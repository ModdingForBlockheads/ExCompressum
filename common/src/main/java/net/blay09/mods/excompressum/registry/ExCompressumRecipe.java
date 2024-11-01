package net.blay09.mods.excompressum.registry;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public abstract class ExCompressumRecipe<T extends RecipeInput> implements Recipe<T> {

    @Override
    public boolean matches(T inv, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(T container, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

}
