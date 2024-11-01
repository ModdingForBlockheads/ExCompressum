package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class HammerSpeedHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(DigSpeedEvent.class, HammerSpeedHandler::onDigSpeed);
    }

    public static void onDigSpeed(DigSpeedEvent event) {
        ItemStack heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        final var targetItem = StupidUtils.getItemStackFromState(event.getState());
        final var level = event.getPlayer().level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return; // TODO I believe this needs to run on client too though... might have to upgrade more stuff to tags and to be less dependent on recipe existence
        }

        if ((heldItem.is(ModItemTags.HAMMERS) || heldItem.is(ModItemTags.COMPRESSED_HAMMERS)) && (ExRegistries.getHammerRegistry()
                .isHammerable(serverLevel, targetItem) || ExRegistries.getCompressedHammerRegistry().isHammerable(serverLevel, targetItem))) {
            float newSpeed = 2f;
            final var tool = heldItem.get(DataComponents.TOOL);
            if (tool != null) {
                newSpeed = tool.defaultMiningSpeed();
            }
            event.setSpeedOverride(newSpeed);
        }
    }

}
