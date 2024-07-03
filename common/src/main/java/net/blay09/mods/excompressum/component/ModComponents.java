package net.blay09.mods.excompressum.component;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

public class ModComponents {

    public static DeferredObject<DataComponentType<Integer>> energy;
    public static DeferredObject<DataComponentType<Unit>> angry;

    public static void initialize(BalmComponents components) {
        energy = components.registerComponent(() -> DataComponentType.<Integer>builder().persistent(Codec.INT).build(),
                ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "energy"));
        angry = components.registerComponent(() -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).build(),
                ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "angry"));
    }
}
