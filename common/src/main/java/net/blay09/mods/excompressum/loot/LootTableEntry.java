package net.blay09.mods.excompressum.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class LootTableEntry {
    private final ItemStack itemStack;
    private final NumberProvider countRange;
    private final NumberProvider baseChance;

    public LootTableEntry(ItemStack itemStack, NumberProvider countRange, NumberProvider baseChance) {
        this.itemStack = itemStack;
        this.countRange = countRange;
        this.baseChance = baseChance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public NumberProvider getCountRange() {
        return countRange;
    }

    public NumberProvider getBaseChance() {
        return baseChance;
    }
}
