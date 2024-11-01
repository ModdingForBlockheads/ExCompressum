package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class CompressedHammerItem extends DiggerItem {

    public CompressedHammerItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(toolMaterial, ModBlockTags.MINEABLE_WITH_HAMMER, attackDamage, attackSpeed, properties);
    }

}
