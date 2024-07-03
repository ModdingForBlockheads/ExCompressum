package net.blay09.mods.excompressum.neoforge.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.blay09.mods.excompressum.block.entity.EnvironmentalConditionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class BaitDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof BaitBlockEntity bait) {
            EnvironmentalConditionResult environmentalStatus = bait.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalConditionResult.CanSpawn) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.baitTooClose"));
                tooltip.addLine(Component.translatable("tooltip.excompressum.baitTooClose2"));
            } else {
                final var statusText = Component.translatable(environmentalStatus.langKey, environmentalStatus.params);
                statusText.withStyle(ChatFormatting.RED);
                tooltip.addLine(statusText);
            }
        }
    }

}
