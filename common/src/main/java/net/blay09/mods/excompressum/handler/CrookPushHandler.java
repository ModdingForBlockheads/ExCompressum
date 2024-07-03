package net.blay09.mods.excompressum.handler;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.PlayerAttackEvent;
import net.blay09.mods.excompressum.item.CompressedCrookItem;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.world.InteractionHand;

public class CrookPushHandler {
    public static void initialize() {
        Balm.getEvents().onEvent(PlayerAttackEvent.class, event -> {
            final var itemStack = event.getPlayer().getMainHandItem();
            if (itemStack.is(ModItems.compressedCrook)) {
                CompressedCrookItem.pushEntity(itemStack, event.getPlayer(), event.getTarget(), InteractionHand.MAIN_HAND);
                event.setCanceled(true);
            }
        });
    }
}
