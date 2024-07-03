package net.blay09.mods.excompressum.forge.compat.top;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.block.entity.*;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;

import org.jetbrains.annotations.Nullable;

public class TheOneProbeAddon  {

    public static void register() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", TopInitializer::new);
    }

    public static class TopInitializer implements Function<ITheOneProbe, Void> {
        @Nullable
        @Override
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider(new ProbeInfoProvider());
            }
            return null;
        }
    }

    public static class ProbeInfoProvider implements IProbeInfoProvider {

        @Override
        public ResourceLocation getID() {
            return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, ExCompressum.MOD_ID);
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
            if (state.getBlock() instanceof AutoSieveBlock) {
                AbstractAutoSieveBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), AbstractAutoSieveBlockEntity.class);
                if (tileEntity != null) {
                    addAutoSieveInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof AutoHammerBlock) {
                AutoHammerBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), AutoHammerBlockEntity.class);
                if (tileEntity != null) {
                    addAutoHammerInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof BaitBlock) {
                BaitBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), BaitBlockEntity.class);
                if (tileEntity != null) {
                    addBaitInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof WoodenCrucibleBlock) {
                WoodenCrucibleBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), WoodenCrucibleBlockEntity.class);
                if (tileEntity != null) {
                    addWoodenCrucibleInfo(tileEntity, mode, info);
                }
            } else if (state.getBlock() instanceof HeavySieveBlock) {
                HeavySieveBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), HeavySieveBlockEntity.class);
                if (tileEntity != null) {
                    addHeavySieveInfo(tileEntity, mode, info);
                }
            }
        }

        private void addAutoSieveInfo(AbstractAutoSieveBlockEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if (tileEntity.getSkinProfile() != null) {
                info.text(Component.translatable("tooltip.excompressum.sieveSkin", tileEntity.getSkinProfile().gameProfile().getName()));
            }
            if (tileEntity.getFoodBoost() > 1f) {
                info.text(Component.translatable("tooltip.excompressum.speedBoost", tileEntity.getFoodBoost()));
            }
            if (tileEntity.getEffectiveLuck() > 1) {
                info.text(Component.translatable("tooltip.excompressum.luckBonus", tileEntity.getEffectiveLuck() - 1));
            }
        }

        private void addAutoHammerInfo(AutoHammerBlockEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if (tileEntity.getEffectiveLuck() > 1) {
                info.text(Component.translatable("tooltip.excompressum.luckBonus", tileEntity.getEffectiveLuck() - 1));
            }
        }

        private void addBaitInfo(BaitBlockEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            EnvironmentalConditionResult environmentalStatus = tileEntity.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalConditionResult.CanSpawn) {
                info.text(Component.translatable("tooltip.excompressum.baitTooClose"));
                info.text(Component.translatable("tooltip.excompressum.baitTooClose2"));
            } else {
                final var statusText = Component.translatable(environmentalStatus.langKey, environmentalStatus.params);
                statusText.withStyle(ChatFormatting.RED);
                info.text(statusText);
            }
        }

        private void addWoodenCrucibleInfo(WoodenCrucibleBlockEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if (tileEntity.getSolidVolume() > 0f) {
                info.text(Component.translatable("tooltip.excompressum.solidVolume", tileEntity.getSolidVolume()));
            }
            if (tileEntity.getFluidTank().getAmount() > 0f) {
                info.text(Component.translatable("tooltip.excompressum.fluidVolume", tileEntity.getFluidTank().getAmount()));
            }
        }

        private void addHeavySieveInfo(HeavySieveBlockEntity tileEntity, ProbeMode mode, IProbeInfo info) {
            if(tileEntity.getProgress() > 0f) {
                info.text(Component.translatable("tooltip.excompressum.sieveProgress", (int) (tileEntity.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = tileEntity.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExNihilo.getInstance().doMeshesHaveDurability()) {
                    info.text(Component.translatable("tooltip.excompressum.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamageValue(), meshStack.getMaxDamage()));
                } else {
                    info.text(meshStack.getDisplayName());
                }
            } else {
                info.text(Component.translatable("tooltip.excompressum.sieveNoMesh"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends BlockEntity> T tryGetTileEntity(Level level, BlockPos pos, Class<T> tileClass) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null && tileClass.isAssignableFrom(blockEntity.getClass())) {
            return (T) blockEntity;
        }
        return null;
    }

}
