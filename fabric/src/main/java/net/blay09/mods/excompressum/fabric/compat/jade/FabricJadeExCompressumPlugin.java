package net.blay09.mods.excompressum.fabric.compat.jade;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(ExCompressum.MOD_ID)
public class FabricJadeExCompressumPlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new AutoSieveDataProvider(), AutoSieveBlock.class);
        registration.registerBlockComponent(new AutoHammerDataProvider(), AutoHammerBlock.class);
        registration.registerBlockComponent(new BaitDataProvider(), BaitBlock.class);
        registration.registerBlockComponent(new WoodenCrucibleDataProvider(), WoodenCrucibleBlock.class);
        registration.registerBlockComponent(new HeavySieveDataProvider(), HeavySieveBlock.class);
    }
}
