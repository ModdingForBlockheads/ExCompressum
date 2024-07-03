package net.blay09.mods.excompressum.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.NeoForgeBalm;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.energy.NeoForgeEnergyStorage;
import net.blay09.mods.balm.neoforge.fluid.NeoForgeFluidTank;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.neoforge.compat.top.TheOneProbeAddon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ExCompressum.MOD_ID)
public class NeoForgeExCompressum {

    private static final Logger logger = LoggerFactory.getLogger(NeoForgeExCompressum.class);

    public NeoForgeExCompressum(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        Balm.initialize(ExCompressum.MOD_ID, context, ExCompressum::initialize);

        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::imc);
    }

    private void imc(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            TheOneProbeAddon.register();
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.woodenCrucible.get(),
                (blockEntity, context) -> new NeoForgeFluidTank(blockEntity.getFluidTank()));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.autoCompressor.get(),
                (blockEntity, context) -> new NeoForgeEnergyStorage(blockEntity.getEnergyStorage()));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.rationingAutoCompressor.get(),
                (blockEntity, context) -> new NeoForgeEnergyStorage(blockEntity.getEnergyStorage()));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.autoSieve.get(),
                (blockEntity, context) -> new NeoForgeEnergyStorage(blockEntity.getEnergyStorage()));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.autoHeavySieve.get(),
                (blockEntity, context) -> new NeoForgeEnergyStorage(blockEntity.getEnergyStorage()));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.autoHammer.get(),
                (blockEntity, context) -> new NeoForgeEnergyStorage(blockEntity.getEnergyStorage()));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.autoCompressedHammer.get(),
                (blockEntity, context) -> new NeoForgeEnergyStorage(blockEntity.getEnergyStorage()));

        event.registerBlockEntity(NeoForgeBalm.FLUID_TANK_CAPABILITY,
                ModBlockEntities.woodenCrucible.get(),
                (blockEntity, context) -> blockEntity.getFluidTank());
        event.registerBlockEntity(NeoForgeBalm.ENERGY_STORAGE_CAPABILITY,
                ModBlockEntities.autoCompressor.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
        event.registerBlockEntity(NeoForgeBalm.ENERGY_STORAGE_CAPABILITY,
                ModBlockEntities.rationingAutoCompressor.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
        event.registerBlockEntity(NeoForgeBalm.ENERGY_STORAGE_CAPABILITY,
                ModBlockEntities.autoSieve.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
        event.registerBlockEntity(NeoForgeBalm.ENERGY_STORAGE_CAPABILITY,
                ModBlockEntities.autoHeavySieve.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
        event.registerBlockEntity(NeoForgeBalm.ENERGY_STORAGE_CAPABILITY,
                ModBlockEntities.autoHammer.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
        event.registerBlockEntity(NeoForgeBalm.ENERGY_STORAGE_CAPABILITY,
                ModBlockEntities.autoCompressedHammer.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
    }
}
