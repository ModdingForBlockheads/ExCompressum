package net.blay09.mods.excompressum.forge.compat.exdeorum;

import com.google.common.collect.Maps;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;

import java.util.*;

public class ExDeorumAddon implements ExNihiloProvider {

    private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

    public ExDeorumAddon() {
        ExNihilo.setInstance(this);

        itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("wooden_hammer"));
        itemMap.put(NihiloItems.HAMMER_STONE, findItem("stone_hammer"));
        itemMap.put(NihiloItems.HAMMER_IRON, findItem("iron_hammer"));
        itemMap.put(NihiloItems.HAMMER_GOLD, findItem("golden_hammer"));
        itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("diamond_hammer"));
        itemMap.put(NihiloItems.HAMMER_NETHERITE, findItem("netherite_hammer"));
        itemMap.put(NihiloItems.IRON_MESH, findItem("iron_mesh"));

        itemMap.put(NihiloItems.SIEVE, findBlock("oak_sieve"));
        itemMap.put(NihiloItems.DUST, findBlock("dust"));
        itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("infested_leaves"));
        itemMap.put(NihiloItems.CRUSHED_NETHERRACK, findBlock("crushed_netherrack"));
        itemMap.put(NihiloItems.CRUSHED_END_STONE, findBlock("crushed_end_stone"));
        itemMap.put(NihiloItems.DIORITE_GRAVEL, findBlock("crushed_diorite"));
        itemMap.put(NihiloItems.ANDESITE_GRAVEL, findBlock("crushed_andesite"));
        itemMap.put(NihiloItems.GRANITE_GRAVEL, findBlock("crushed_granite"));

        final var stringMeshItem = findItem("string_mesh");
        if (!stringMeshItem.isEmpty()) {
            SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(CommonMeshType.STRING, stringMeshItem, stringMeshItem.getItem());
            stringMesh.setMeshLevel(1);
            stringMesh.setModelName("string");
            SieveMeshRegistry.add(stringMesh);
        }

        ItemStack flintMeshItem = findItem("flint_mesh");
        if (!flintMeshItem.isEmpty()) {
            SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(CommonMeshType.FLINT, flintMeshItem, flintMeshItem.getItem());
            flintMesh.setMeshLevel(2);
            flintMesh.setModelName("flint");
            SieveMeshRegistry.add(flintMesh);
        }

        ItemStack ironMeshItem = findItem("iron_mesh");
        if (!ironMeshItem.isEmpty()) {
            SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(CommonMeshType.IRON, ironMeshItem, ironMeshItem.getItem());
            ironMesh.setMeshLevel(3);
            ironMesh.setHeavy(true);
            ironMesh.setModelName("iron");
            SieveMeshRegistry.add(ironMesh);
            SieveMeshRegistry.registerDefaults(ironMeshItem.getItem());
        }

        ItemStack goldMeshItem = findItem("golden_mesh");
        if (!goldMeshItem.isEmpty()) {
            SieveMeshRegistryEntry goldMesh = new SieveMeshRegistryEntry(CommonMeshType.GOLD, goldMeshItem, goldMeshItem.getItem());
            goldMesh.setMeshLevel(4);
            goldMesh.setHeavy(true);
            goldMesh.setModelName("gold");
            SieveMeshRegistry.add(goldMesh);
        }

        ItemStack diamondMeshItem = findItem("diamond_mesh");
        if (!diamondMeshItem.isEmpty()) {
            SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(CommonMeshType.DIAMOND, diamondMeshItem, diamondMeshItem.getItem());
            diamondMesh.setMeshLevel(4);
            diamondMesh.setHeavy(true);
            diamondMesh.setModelName("diamond");
            SieveMeshRegistry.add(diamondMesh);
        }

        ItemStack netheriteMeshItem = findItem("netherite_mesh");
        if (!netheriteMeshItem.isEmpty()) {
            SieveMeshRegistryEntry mesh = new SieveMeshRegistryEntry(CommonMeshType.NETHERITE, netheriteMeshItem, netheriteMeshItem.getItem());
            mesh.setMeshLevel(6);
            mesh.setHeavy(true);
            mesh.setModelName("netherite");
            SieveMeshRegistry.add(mesh);
        }
    }

    private ItemStack findItem(String name) {
        ResourceLocation location = new ResourceLocation(Compat.EX_DEORUM, name);
        Item item = Balm.getRegistries().getItem(location);
        return new ItemStack(item);
    }

    private ItemStack findBlock(String name) {
        ResourceLocation location = new ResourceLocation(Compat.EX_DEORUM, name);
        Block block = Balm.getRegistries().getBlock(location);
        return new ItemStack(block);
    }


    @Override
    public ItemStack getNihiloItem(NihiloItems type) {
        ItemStack itemStack = itemMap.get(type);
        return itemStack != null ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public boolean isHammerable(BlockState state) {
        return RecipeUtil.getHammerRecipe(StupidUtils.getItemStackFromState(state).getItem()) != null;
    }

    @Override
    public boolean isHammerableCompressed(ItemStack itemStack) {
        return RecipeUtil.getCompressedHammerRecipe(itemStack.getItem()) != null;
    }

    @Override
    public List<ItemStack> rollHammerRewards(Level level, BlockState state, ItemStack toolItem, RandomSource rand) {
        List<ItemStack> drops = new ArrayList<>();
        final var recipe = RecipeUtil.getHammerRecipe(StupidUtils.getItemStackFromState(state).getItem());
        if (recipe != null) {
            drops.add(recipe.getResultItem(level.registryAccess()));
        }

        return drops;
    }

    @Override
    public boolean isSiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        if (sieveMesh == null) {
            return false;
        }

        return !RecipeUtil.getSieveRecipes((Item) sieveMesh.getBackingMesh(), StupidUtils.getItemStackFromState(state)).isEmpty();
    }

    @Override
    public boolean isHeavySiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        if (sieveMesh == null) {
            return false;
        }

        return !RecipeUtil.getCompressedSieveRecipes((Item) sieveMesh.getBackingMesh(), StupidUtils.getItemStackFromState(state)).isEmpty();
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        final var sourceStack = StupidUtils.getItemStackFromState(state);
        final var recipes = RecipeUtil.getSieveRecipes((Item) sieveMesh.getBackingMesh(), sourceStack);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            LootContext lootContext = LootTableUtils.buildLootContext((ServerLevel) level, sourceStack);
            final var amount = recipe.resultAmount.getInt(lootContext);
            if (amount > 0) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
        }
        return list;
    }

    @Override
    public Collection<ItemStack> rollHeavySieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        final var sourceStack = StupidUtils.getItemStackFromState(state);
        final var recipes = RecipeUtil.getCompressedSieveRecipes((Item) sieveMesh.getBackingMesh(), sourceStack);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            LootContext lootContext = LootTableUtils.buildLootContext((ServerLevel) level, sourceStack);
            final var amount = recipe.resultAmount.getInt(lootContext);
            if (amount > 0) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
        }
        return list;
    }

    @Override
    public Collection<ItemStack> rollCompressedHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        final var recipe = RecipeUtil.getCompressedHammerRecipe(itemStack.getItem());
        if (recipe != null) {
            List<ItemStack> list = new ArrayList<>();
            LootContext lootContext = LootTableUtils.buildLootContext((ServerLevel) level, itemStack);
            final var amount = recipe.resultAmount.getInt(lootContext);
            if (amount > 0) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand) {
        final float luck = getLuckFromTool(tool);
        final var recipes = RecipeUtil.getCrookRecipes(state);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            float fortuneChanceBonus = 0.1f;
            if (rand.nextFloat() <= recipe.chance() + fortuneChanceBonus * luck) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
        }
        return list;
    }

    private float getLuckFromTool(ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
    }

    @Override
    public LootTable generateHeavySieveLootTable(Level level, BlockState sieveState, ItemLike source, int times, SieveMeshRegistryEntry mesh) {
        return LootTable.EMPTY;
        /*if (!(mesh.getBackingMesh() instanceof Item)) {
            return LootTable.EMPTY;
        }

        LootTable.Builder tableBuilder = LootTable.lootTable();
        final var recipes = RecipeUtil.getSieveRecipes((Item) mesh.getBackingMesh(), new ItemStack(source));
        for (final var recipe : recipes) {
            if (recipe.mesh != mesh.getBackingMesh()) {
                continue;
            }

            final var poolBuilder = LootPool.lootPool();
            poolBuilder.name("excompressum-heavysieve-" + Balm.getRegistries()
                    .getKey(source.asItem())
                    .toString()
                    .replace(':', '-') + "-" + UUID.randomUUID());
            poolBuilder.setRolls(ConstantValue.exactly(times));
            LootPoolSingletonContainer.Builder<?> entryBuilder = buildLootEntry(recipe.getResultItem(level.registryAccess()));
            entryBuilder.apply(SetItemCountFunction.setCount(recipe.resultAmount));
            poolBuilder.add(entryBuilder);
            tableBuilder.withPool(poolBuilder);
        }
        return tableBuilder.build();*/
    }

    @Override
    public boolean doMeshesHaveDurability() {
        return false;
    }

    @Override
    public boolean doMeshesSplitLootTables() {
        return true;
    }

    @Override
    public int getMeshFortune(ItemStack meshStack) {
        return 0;
    }

    @Override
    public int getMeshEfficiency(ItemStack meshStack) {
        return 0;
    }

    @Override
    public boolean isCompressableOre(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isHammerableOre(ItemStack itemStack) {
        return false;
    }

    @Override
    public List<IHammerRecipe> getHammerRecipes() {
        List<IHammerRecipe> result = new ArrayList<>();

        /*ArrayListMultimap<IntList, CrushingRecipe> groupedRecipes = ArrayListMultimap.create();
        for (final var hammerRecipe : ExNihiloRegistries.HAMMER_REGISTRY.getRecipeList()) {
            groupedRecipes.put(hammerRecipe.getInput().getStackingIds(), hammerRecipe);
        }

        for (IntList packedStacks : groupedRecipes.keySet()) {
            LootTable.Builder tableBuilder = LootTable.lootTable();
            for (final var hammerRecipe : groupedRecipes.get(packedStacks)) {
                for (ItemStackWithChance itemStackWithChance : hammerRecipe.getDrops()) {
                    LootPool.Builder poolBuilder = LootPool.lootPool();
                    LootPoolSingletonContainer.Builder<?> entryBuilder = buildLootEntry(itemStackWithChance);
                    poolBuilder.add(entryBuilder);
                    tableBuilder.withPool(poolBuilder);
                }
            }

            final var firstRecipe = groupedRecipes.get(packedStacks).get(0);
            Ingredient input = firstRecipe.getInput();
            LootTableProvider lootTableProvider = new LootTableProvider(tableBuilder.build());
            result.add(new net.blay09.mods.excompressum.registry.hammer.HammerRecipe(firstRecipe.getId(), input, lootTableProvider));
        }*/

        return result;
    }

    private LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStack outputItem) {
        return LootTableUtils.buildLootEntry(outputItem, -1f);
    }

    /* private LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStackWithChance outputItem) {
        return LootTableUtils.buildLootEntry(outputItem.getStack(), outputItem.getChance());
    }*/

}
