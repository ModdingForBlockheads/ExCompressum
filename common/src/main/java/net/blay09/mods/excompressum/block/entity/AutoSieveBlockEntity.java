package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AutoSieveBlockEntity extends AbstractAutoSieveBlockEntity implements BalmEnergyStorageProvider {

    private final EnergyStorage energyStorage = new EnergyStorage(32000) {
        @Override
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                setChanged();
            }
            return super.fill(maxReceive, simulate);
        }
    };

    public AutoSieveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public AutoSieveBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.autoSieve.get(), pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        energyStorage.deserialize(tag.get("EnergyStorage"));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("EnergyStorage", energyStorage.serialize());
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergy();
    }

    @Override
    public void setEnergyStored(int energy) {
        energyStorage.setEnergy(energy);
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getCapacity();
    }

    @Override
    public int drainEnergy(int maxExtract, boolean simulate) {
        if (!simulate) {
            isDirty = true;
        }

        return energyStorage.drain(maxExtract, simulate);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

}
