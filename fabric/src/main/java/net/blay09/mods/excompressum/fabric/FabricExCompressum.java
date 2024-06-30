package net.blay09.mods.excompressum.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.ExCompressum;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.storage.loot.LootTable;

public class FabricExCompressum implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(ExCompressum.MOD_ID, ExCompressum::initialize);
        ExCompressum.lootTableLoader = (gson, resourceLocation, jsonElement) -> gson.fromJson(jsonElement, LootTable.class);
    }
}
