package net.blay09.mods.excompressum.mixin;

import net.blay09.mods.excompressum.CommonLootTableAccessor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootTable.class)
public abstract class LootTableAccessor implements CommonLootTableAccessor {
    @Accessor
    protected abstract LootPool[] getPools();

    @Override
    public LootPool[] balm_getPools() {
        return getPools();
    }
}
