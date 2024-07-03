package net.blay09.mods.excompressum;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CommonProxy {
    public void preloadSkin(ResolvableProfile profile) {
    }

    public void spawnCrushParticles(Level level, BlockPos pos, BlockState state) {
    }

    public void spawnAutoSieveParticles(Level level, BlockPos pos, BlockState emitterState, BlockState particleState, int particleCount) {
    }

    public void spawnHeavySieveParticles(Level level, BlockPos pos, BlockState particleState, int particleCount) {
    }

    public RecipeManager getRecipeManager(@Nullable Level level) {
        if (level != null && level.getServer() != null) {
            return level.getServer().getRecipeManager();
        }

        return Balm.getHooks().getServer().getRecipeManager();
    }
}
