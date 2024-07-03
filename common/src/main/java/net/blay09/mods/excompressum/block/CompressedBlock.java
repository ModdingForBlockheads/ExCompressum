package net.blay09.mods.excompressum.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class CompressedBlock extends Block {

    public static final MapCodec<CompressedBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(CompressedBlockType.CODEC.fieldOf("type").forGetter(
                    CompressedBlock::getType),
            propertiesCodec()).apply(it, CompressedBlock::new));

    private final CompressedBlockType type;

    public CompressedBlock(CompressedBlockType type, Properties properties) {
        super(properties.sound(SoundType.STONE).strength(4f, 6f));
        this.type = type;
    }

    public CompressedBlockType getType() {
        return type;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
