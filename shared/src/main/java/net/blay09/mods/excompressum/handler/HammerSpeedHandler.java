package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;

public class HammerSpeedHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(DigSpeedEvent.class, HammerSpeedHandler::onDigSpeed);
    }

    public static void onDigSpeed(DigSpeedEvent event) {
        ItemStack heldItem = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        if (heldItem.is(ModItemTags.HAMMERS) && event.getState().is(BlockTags.LOGS)) {
            float newSpeed = 2f;
            if (heldItem.getItem() instanceof DiggerItem) {
                newSpeed = ((DiggerItem) heldItem.getItem()).getTier().getSpeed();
            }
            event.setSpeedOverride(newSpeed);
        }
    }

}
