package net.blay09.mods.excompressum.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.client.ExCompressumClient;
import net.blay09.mods.excompressum.forge.compat.top.TheOneProbeAddon;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExCompressum.MOD_ID)
public class ForgeExCompressum {

    public ForgeExCompressum() {
        Balm.initialize(ExCompressum.MOD_ID, EmptyLoadContext.INSTANCE, ExCompressum::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(ExCompressum.MOD_ID, EmptyLoadContext.INSTANCE, ExCompressumClient::initialize));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imc);

        ExCompressum.lootTableLoader = (gson, resourceLocation, jsonElement) -> gson.fromJson(jsonElement, LootTable.class);
    }

    private void imc(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            TheOneProbeAddon.register();
        }
    }
}
