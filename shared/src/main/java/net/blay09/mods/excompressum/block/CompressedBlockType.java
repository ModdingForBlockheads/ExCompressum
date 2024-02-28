package net.blay09.mods.excompressum.block;

import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.Nullable;
import java.util.Locale;

public enum CompressedBlockType implements StringRepresentable {
    DUST,
    COBBLESTONE,
    GRAVEL,
    SAND,
    DIRT,
    FLINT,
    CRUSHED_NETHERRACK,
    CRUSHED_END_STONE,
    SOUL_SAND,
    NETHERRACK,
    END_STONE,
    DIORITE,
    ANDESITE,
    GRANITE,
    CRUSHED_DIORITE,
    CRUSHED_ANDESITE,
    CRUSHED_GRANITE;

    public static final CompressedBlockType[] values = values();

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Nullable
    public static CompressedBlockType fromId(int id) {
        return id >= 0 && id < values.length ? values[id] : null;
    }
}
