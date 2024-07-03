package net.blay09.mods.excompressum.item;

import net.minecraft.world.item.Item;

public class IronMeshItem extends Item {

    public IronMeshItem(Item.Properties properties) {
        super(properties.durability(256));
    }

    @Override
    public int getEnchantmentValue() {
        return 30;
    }

}
