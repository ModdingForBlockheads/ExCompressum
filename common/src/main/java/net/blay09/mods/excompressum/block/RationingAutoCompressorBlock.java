package net.blay09.mods.excompressum.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.block.entity.RationingAutoCompressorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class RationingAutoCompressorBlock extends AutoCompressorBlock {

    public static final MapCodec<RationingAutoCompressorBlock> CODEC = simpleCodec(RationingAutoCompressorBlock::new);

    public RationingAutoCompressorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RationingAutoCompressorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? createTickerHelper(type, ModBlockEntities.rationingAutoCompressor.get(), AutoCompressorBlockEntity::serverTick) : null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
