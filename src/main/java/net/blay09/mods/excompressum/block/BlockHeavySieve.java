package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Locale;

public class BlockHeavySieve extends BlockContainer {

	public static final String name = "heavy_sieve";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	public static final SieveModelBounds SIEVE_BOUNDS = new SieveModelBounds(0.5625f, 0.0625f, 0.88f, 0.5f);

	public enum Type implements IStringSerializable {
		OAK,
		SPRUCE,
		BIRCH,
		JUNGLE,
		ACACIA,
		DARK_OAK;

		public static Type[] values = values();

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.75f, 1);
	public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);
	public static final PropertyBool WITH_MESH = PropertyBool.create("with_mesh");

	public BlockHeavySieve() {
		super(Material.WOOD);
		setCreativeTab(ExCompressum.creativeTab);
		setHardness(2f);
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, WITH_MESH);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 0 || meta >= Type.values.length) {
			return getDefaultState();
		}
		return getDefaultState().withProperty(VARIANT, Type.values[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < BlockWoodenCrucible.Type.values.length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHeavySieve();
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(WITH_MESH, false); // Property is inventory-only
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileHeavySieve tileEntity = (TileHeavySieve) world.getTileEntity(pos);
		if (tileEntity != null) {
			ItemStack heldItem = player.getHeldItem(hand);
			if (!heldItem.isEmpty()) {
				SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(heldItem);
				if (sieveMesh != null && tileEntity.getMeshStack().isEmpty()) {
					tileEntity.setMeshStack(player.capabilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(heldItem, 1) : heldItem.splitStack(1));
					return true;
				}

				if (tileEntity.addSiftable(player, heldItem)) {
					int posX = (int) Math.ceil(pos.getX());
					int posZ = (int) Math.ceil(pos.getZ());
					for (int x = posX - 2; x <= posX + 2; x++) {
						for (int z = posZ - 2; z <= posZ + 2; z++) {
							BlockPos testPos = new BlockPos(x, pos.getY(), z);
							TileHeavySieve otherEntity = (TileHeavySieve) world.getTileEntity(testPos);
							if (otherEntity != null && !heldItem.isEmpty()) {
								otherEntity.addSiftable(player, heldItem);
							}
						}
					}
					world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_STEP, SoundCategory.BLOCKS, 0.5f, 1f);
					return true;
				}
			} else {
				if(!world.isRemote && player.isSneaking()) {
					ItemStack meshStack = tileEntity.getMeshStack();
					if(!meshStack.isEmpty()) {
						if (player.inventory.addItemStackToInventory(meshStack)) {
							world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, meshStack));
						}
						tileEntity.setMeshStack(ItemStack.EMPTY);
					}
				}
			}

			if (ModConfig.automation.allowHeavySieveAutomation || !(player instanceof FakePlayer)) {
				if (tileEntity.processContents(player)) {
					world.playSound(null, pos, SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 0.3f, 0.6f);
				}
				int posX = (int) Math.ceil(pos.getX());
				int posZ = (int) Math.ceil(pos.getZ());
				for (int x = posX - 2; x <= posX + 2; x++) {
					for (int z = posZ - 2; z <= posZ + 2; z++) {
						BlockPos testPos = new BlockPos(x, pos.getY(), z);
						TileHeavySieve otherEntity = (TileHeavySieve) world.getTileEntity(testPos);
						if (otherEntity != null  && !testPos.equals(pos)) {
							otherEntity.processContents(player);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileHeavySieve) {
			TileHeavySieve tileHeavySieve = (TileHeavySieve) tileEntity;
			if(!tileHeavySieve.getMeshStack().isEmpty()) {
				world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tileHeavySieve.getMeshStack()));
			}
		}
		super.breakBlock(world, pos, state);
	}

}
