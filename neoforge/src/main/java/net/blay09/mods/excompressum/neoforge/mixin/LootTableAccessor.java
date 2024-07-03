package net.blay09.mods.excompressum.neoforge.mixin;

import net.blay09.mods.excompressum.CommonLootTableAccessor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTable.class)
public abstract class LootTableAccessor implements CommonLootTableAccessor {
    @Accessor
    protected abstract List<LootPool> getPools();

    @Override
    public LootPool[] balm_getPools() {
        return getPools().toArray(LootPool[]::new);
    }
}
