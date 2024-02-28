package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class WoodenCrucibleBlockEntity extends BalmBlockEntity {

    private static final int RAIN_FILL_INTERVAL = 20;
    private static final int MELT_INTERVAL = 20;
    private static final int RAIN_FILL_SPEED = 8;
    private static final int SYNC_INTERVAL = 10;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack copy = stack.copy();
            if (addItem(copy, true, simulate)) {
                copy.shrink(1);
                return copy.isEmpty() ? ItemStack.EMPTY : copy;
            }
            return stack;
        }
    };

    private final FluidTank fluidTank = new FluidTank(1999, it -> itemHandler.getStackInSlot(0).isEmpty() && isValidFluid(it.getFluid())) {

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int result = super.fill(resource, action);
            if (fluid.getAmount() > 1000) {
                fluid.setAmount(1000);
            }
            return result;
        }

        @Override
        public int getCapacity() {
            return 1000;
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            isDirty = true;
        }
    };

    private boolean isValidFluid(Fluid fluid) {
        // TODO return fluid.getTemperature(it) <= 300
        return true;
    }

    private final LazyOptional<FluidTank> fluidTankCap = LazyOptional.of(() -> fluidTank);
    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);

    private int ticksSinceSync;
    private boolean isDirty;
    private int ticksSinceRain;
    private int ticksSinceMelt;
    private Fluid currentTargetFluid;
    private int solidVolume;

    public WoodenCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.woodenCrucible.get(), pos, state);
    }

    public boolean addItem(ItemStack itemStack, boolean isAutomated, boolean simulate) {
        // When inserting dust, turn it into clay if we have enough liquid
        if (fluidTank.getFluidAmount() >= 1000 && ExNihilo.isNihiloItem(itemStack, ExNihiloProvider.NihiloItems.DUST)) {
            if (!simulate) {
                itemHandler.setStackInSlot(0, new ItemStack(Blocks.CLAY));
                fluidTank.setFluid(FluidStack.EMPTY);
                sync();
            }
            return true;
        }

        // Otherwise, try to add it as a recipe
        WoodenCrucibleRecipe recipe = ExRegistries.getWoodenCrucibleRegistry().getRecipe(level, itemStack);
        if (recipe != null) {
            if (fluidTank.getFluid().isEmpty() || fluidTank.getFluidAmount() == 0 || recipe.matchesFluid(fluidTank.getFluid())) {
                int capacityLeft = fluidTank.getCapacity() - fluidTank.getFluidAmount() - solidVolume;
                if ((isAutomated && capacityLeft >= recipe.getAmount()) || (!isAutomated && capacityLeft > 0)) {
                    if (!simulate) {
                        currentTargetFluid = recipe.getFluidStack().getFluid();
                        solidVolume += Math.min(capacityLeft, recipe.getAmount());
                        sync();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WoodenCrucibleBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void serverTick() {
        // Fill the crucible from rain
        if (level.getLevelData().isRaining() && level.canSeeSkyFromBelowWater(worldPosition) && level.getBiome(worldPosition).value().hasPrecipitation()) {
            ticksSinceRain++;
            if (ticksSinceRain >= RAIN_FILL_INTERVAL) {
                fluidTank.fill(new FluidStack(Fluids.WATER, RAIN_FILL_SPEED), IFluidHandler.FluidAction.EXECUTE);
                ticksSinceRain = 0;
            }
        }

        // Melt down content
        if (currentTargetFluid != null) {
            ticksSinceMelt++;
            if (ticksSinceMelt >= MELT_INTERVAL && fluidTank.getFluidAmount() < fluidTank.getCapacity()) {
                int amount = Math.min(ExCompressumConfig.getActive().automation.woodenCrucibleSpeed, solidVolume);
                fluidTank.fill(new FluidStack(currentTargetFluid, amount), IFluidHandler.FluidAction.EXECUTE);
                solidVolume = Math.max(0, solidVolume - amount);
                ticksSinceMelt = 0;
                isDirty = true;
            }
        }

        // Sync to clients
        ticksSinceSync++;
        if (ticksSinceSync >= SYNC_INTERVAL) {
            ticksSinceSync = 0;
            if (isDirty) {
                sync();
                isDirty = false;
            }
        }
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        solidVolume = tagCompound.getInt("SolidVolume");
        fluidTank.readFromNBT(tagCompound.getCompound("FluidTank"));
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        if (tagCompound.contains("TargetFluid")) {
            currentTargetFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tagCompound.getString("TargetFluid")));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tagCompound) {
        super.saveAdditional(tagCompound);
        if (currentTargetFluid != null) {
            final var fluidId = Balm.getRegistries().getKey(currentTargetFluid);
            tagCompound.putString("TargetFluid", Objects.toString(fluidId));
        }
        tagCompound.putInt("SolidVolume", solidVolume);
        tagCompound.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return (LazyOptional<T>) fluidTankCap;
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return (LazyOptional<T>) itemHandlerCap;
        }
        return super.getCapability(cap, side);
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public int getSolidVolume() {
        return solidVolume;
    }

    public int getSolidCapacity() {
        return fluidTank.getCapacity();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
