package net.blay09.mods.excompressum.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantable;

public class IronMeshItem extends Item {

    public IronMeshItem(Item.Properties properties) {
        super(properties.durability(256).component(DataComponents.ENCHANTABLE, new Enchantable(30)));
    }

}
