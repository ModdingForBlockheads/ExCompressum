package net.blay09.mods.excompressum.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HeavySieveBlock extends BaseEntityBlock {

    public static final MapCodec<HeavySieveBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(HeavySieveType.CODEC.fieldOf("type").forGetter(
                    HeavySieveBlock::getType),
            propertiesCodec()).apply(it, HeavySieveBlock::new));

    private static final VoxelShape BOUNDING_BOX = Shapes.or(
            Shapes.box(0, 0.5f, 0, 1, 0.75f, 1),
            Shapes.box(0.0625f, 0, 0.0625f, 0.125f, 0.5, 0.125f),
            Shapes.box(0.0625f, 0, 1 - 0.125f, 0.125f, 0.5, 1 - 0.0625f),
            Shapes.box(1 - 0.125f, 0, 0.0625f, 1 - 0.0625f, 0.5, 0.125f),
            Shapes.box(1 - 0.125f, 0, 1 - 0.125f, 1 - 0.0625f, 0.5, 1 - 0.0625f)
    );

    public static final BooleanProperty WITH_MESH = BooleanProperty.create("with_mesh");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final HeavySieveType type;

    public HeavySieveBlock(HeavySieveType type, Properties properties) {
        super(properties.strength(2f));
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        this.type = type;
    }

    public HeavySieveType getType() {
        return type;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WITH_MESH, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeavySieveBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!(level.getBlockEntity(pos) instanceof HeavySieveBlockEntity heavySieve)) {
            return InteractionResult.FAIL;
        }

        final var sieveMesh = SieveMeshRegistry.getEntry(itemStack);
        if (sieveMesh != null && heavySieve.getMeshStack().isEmpty()) {
            heavySieve.setMeshStack(player.getAbilities().instabuild ? ContainerUtils.copyStackWithSize(itemStack, 1) : itemStack.split(1));
            return InteractionResult.SUCCESS;
        }

        if (heavySieve.addSiftable(player, itemStack)) {
            level.playSound(null, pos, SoundEvents.GRAVEL_STEP, SoundSource.BLOCKS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (!(level.getBlockEntity(pos) instanceof HeavySieveBlockEntity heavySieve)) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide && player.isShiftKeyDown()) {
            ItemStack meshStack = heavySieve.getMeshStack();
            if (!meshStack.isEmpty() && heavySieve.getCurrentStack().isEmpty()) {
                if (player.getInventory().add(meshStack)) {
                    level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, meshStack));
                }
                heavySieve.setMeshStack(ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }

        if (ExCompressumConfig.getActive().automation.allowHeavySieveAutomation || !Balm.getHooks().isFakePlayer(player)) {
            if (heavySieve.processContents(player)) {
                level.playSound(null, pos, SoundEvents.SAND_STEP, SoundSource.BLOCKS, 0.3f, 0.6f);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useWithoutItem(state, level, pos, player, blockHitResult);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof HeavySieveBlockEntity heavySieve) {
            if (!heavySieve.getMeshStack().isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, heavySieve.getMeshStack()));
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource randomSource) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, facingPos, facingState, randomSource);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, ModBlockEntities.heavySieve.get(), HeavySieveBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.heavySieve.get(), HeavySieveBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
