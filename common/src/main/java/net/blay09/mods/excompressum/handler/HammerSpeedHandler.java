package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;

public class HammerSpeedHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(DigSpeedEvent.class, HammerSpeedHandler::onDigSpeed);
    }

    public static void onDigSpeed(DigSpeedEvent event) {
        final var heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        if ((heldItem.is(ModItemTags.HAMMERS) || heldItem.is(ModItemTags.COMPRESSED_HAMMERS)) && event.getState().is(ModBlockTags.MINEABLE_WITH_HAMMER)) {
            float newSpeed = 2f;
            final var tool = heldItem.get(DataComponents.TOOL);
            if (tool != null) {
                newSpeed = tool.defaultMiningSpeed();
            }
            event.setSpeedOverride(newSpeed);
        }
    }

}
