package net.blay09.mods.excompressum.forge.compat.jade;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class WoodenCrucibleDataProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof WoodenCrucibleBlockEntity woodenCrucible) {
            if (woodenCrucible.getSolidVolume() > 0f) {
                tooltip.add(Component.translatable("tooltip.excompressum.solidVolume", woodenCrucible.getSolidVolume()));
            }
            if (woodenCrucible.getFluidTank().getAmount() > 0f) {
                tooltip.add(Component.translatable("tooltip.excompressum.fluidVolume", woodenCrucible.getFluidTank().getAmount()));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible");
    }

}
