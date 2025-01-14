package net.blay09.mods.excompressum.menu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class AutoHammerUpgradeSlot extends Slot {

    private final ResourceLocation noItemIcon;

    public AutoHammerUpgradeSlot(Container container, int index, int xPosition, int yPosition, boolean isCompressed) {
        super(container, index, xPosition, yPosition);
        noItemIcon = ResourceLocation.withDefaultNamespace(isCompressed ? "container/slot/compressed_hammer" : "container/slot/hammer");
    }

    @Nullable
    @Override
    public ResourceLocation getNoItemIcon() {
        return noItemIcon;
    }
}
