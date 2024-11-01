package net.blay09.mods.excompressum.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WoodenCrucibleBlock extends BaseEntityBlock {

    public static final MapCodec<WoodenCrucibleBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(WoodenCrucibleType.CODEC.fieldOf("type").forGetter(
                    WoodenCrucibleBlock::getType),
            propertiesCodec()).apply(it, WoodenCrucibleBlock::new));

    private static final VoxelShape BOUNDING_BOX = Shapes.or(
            Shapes.box(0, 0.1875f, 0, 1, 1f, 1),
            Shapes.box(0.0625f, 0.125f, 0.0625f, 1 - 0.0625f, 0.1875f, 1 - 0.0625f),
            Shapes.box(0.125f, 0.0625f, 0.125f, 1 - 0.125f, 0.125f, 1 - 0.125f),
            Shapes.box(0, 0, 0, 0.0625f, 0.1875f, 0.0625f),
            Shapes.box(1 - 0.0625f, 0, 0, 1, 0.1875f, 0.0625f),
            Shapes.box(0, 0, 1 - 0.0625f, 0.0625f, 0.1875f, 1),
            Shapes.box(1 - 0.0625f, 0, 1 - 0.0625f, 1, 0.1875f, 1)
    );

    private final WoodenCrucibleType type;

    public WoodenCrucibleBlock(WoodenCrucibleType type, Properties properties) {
        super(properties.strength(2f));
        this.type = type;
    }

    public WoodenCrucibleType getType() {
        return type;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenCrucibleBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!(level.getBlockEntity(pos) instanceof WoodenCrucibleBlockEntity woodenCrucible)) {
            return InteractionResult.FAIL;
        }

        final var outputStack = ContainerUtils.extractItem(woodenCrucible, 0, 64, false);
        if (!outputStack.isEmpty()) {
            if (!player.getInventory().add(outputStack)) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, outputStack));
            }

            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            if (woodenCrucible.addItem(serverLevel, itemStack, false, false)) {
                return InteractionResult.CONSUME;
            }
        }

        Balm.getHooks().useFluidTank(state, level, pos, player, hand, blockHitResult);

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (!(level.getBlockEntity(pos) instanceof WoodenCrucibleBlockEntity woodenCrucible)) {
            return InteractionResult.FAIL;
        }

        final var outputStack = ContainerUtils.extractItem(woodenCrucible, 0, 64, false);
        if (!outputStack.isEmpty()) {
            if (!player.getInventory().add(outputStack)) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, outputStack));
            }

            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, blockHitResult);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? createTickerHelper(type, ModBlockEntities.woodenCrucible.get(), WoodenCrucibleBlockEntity::serverTick) : null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
