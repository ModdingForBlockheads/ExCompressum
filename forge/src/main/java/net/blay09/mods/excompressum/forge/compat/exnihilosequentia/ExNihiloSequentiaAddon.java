package net.blay09.mods.excompressum.forge.compat.exnihilosequentia;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.IntList;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import novamachina.exnihilosequentia.common.Config;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;
import novamachina.exnihilosequentia.world.item.MeshType;
import novamachina.exnihilosequentia.world.item.crafting.*;
import novamachina.exnihilosequentia.world.level.block.InfestedLeavesBlock;

import org.jetbrains.annotations.Nullable;
import java.util.*;

public class ExNihiloSequentiaAddon implements ExNihiloProvider {

    public ExNihiloSequentiaAddon() {
        ExNihilo.setInstance(this);

        SieveMeshRegistry.registerDefaults(MeshType.STRING);

        ItemStack stringMeshItem = findItem("string_mesh");
        if (!stringMeshItem.isEmpty()) {
            SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(CommonMeshType.STRING, stringMeshItem, MeshType.STRING);
            stringMesh.setMeshLevel(1);
            stringMesh.setModelName("string");
            SieveMeshRegistry.add(stringMesh);
        }

        ItemStack flintMeshItem = findItem("flint_mesh");
        if (!flintMeshItem.isEmpty()) {
            SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(CommonMeshType.FLINT, flintMeshItem, MeshType.FLINT);
            flintMesh.setMeshLevel(2);
            flintMesh.setModelName("flint");
            SieveMeshRegistry.add(flintMesh);
        }

        ItemStack ironMeshItem = findItem("iron_mesh");
        if (!ironMeshItem.isEmpty()) {
            SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(CommonMeshType.IRON, ironMeshItem, MeshType.IRON);
            ironMesh.setMeshLevel(3);
            ironMesh.setHeavy(true);
            ironMesh.setModelName("iron");
            SieveMeshRegistry.add(ironMesh);
        }

        ItemStack diamondMeshItem = findItem("diamond_mesh");
        if (!diamondMeshItem.isEmpty()) {
            SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(CommonMeshType.DIAMOND, diamondMeshItem, MeshType.DIAMOND);
            diamondMesh.setMeshLevel(4);
            diamondMesh.setHeavy(true);
            diamondMesh.setModelName("diamond");
            SieveMeshRegistry.add(diamondMesh);
        }

        ItemStack emeraldMeshItem = findItem("emerald_mesh");
        if (!emeraldMeshItem.isEmpty()) {
            SieveMeshRegistryEntry emeraldMesh = new SieveMeshRegistryEntry(CommonMeshType.EMERALD, emeraldMeshItem, MeshType.EMERALD);
            emeraldMesh.setMeshLevel(5);
            emeraldMesh.setHeavy(true);
            emeraldMesh.setModelName("emerald");
            SieveMeshRegistry.add(emeraldMesh);
        }

        ItemStack netheriteMeshItem = findItem("netherite_mesh");
        if (!netheriteMeshItem.isEmpty()) {
            SieveMeshRegistryEntry mesh = new SieveMeshRegistryEntry(CommonMeshType.NETHERITE, netheriteMeshItem, MeshType.NETHERITE);
            mesh.setMeshLevel(6);
            mesh.setHeavy(true);
            mesh.setModelName("netherite");
            SieveMeshRegistry.add(mesh);
        }
    }

    private ItemStack findItem(String name) {
        ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_SEQUENTIA, name);
        Item item = Balm.getRegistries().getItem(location);
        return new ItemStack(item);
    }

    private ItemStack findBlock(String name) {
        ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_SEQUENTIA, name);
        Block block = Balm.getRegistries().getBlock(location);
        return new ItemStack(block);
    }


    @Override
    public boolean isHammerableCompressed(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isHammerable(BlockState state) {
        return ExNihiloRegistries.HAMMER_REGISTRY.isHammerable(state.getBlock());
    }

    @Override
    public List<ItemStack> rollHammerRewards(Level level, BlockState state, ItemStack toolItem, RandomSource rand) {
        List<ItemStackWithChance> possibleDrops = ExNihiloRegistries.HAMMER_REGISTRY.getResult(state.getBlock());
        List<ItemStack> drops = new ArrayList<>();
        for (ItemStackWithChance itemStackWithChance : possibleDrops) {
            if (rand.nextFloat() <= itemStackWithChance.getChance()) {
                drops.add(itemStackWithChance.getStack().copy());
            }
        }

        return drops;
    }

    @Override
    public boolean isSiftableWithMesh(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.getValue(BlockStateProperties.WATERLOGGED);
        MeshType mesh = sieveMesh != null ? (MeshType) sieveMesh.getBackingMesh() : MeshType.NONE;
        return ExNihiloRegistries.SIEVE_REGISTRY.isBlockSiftable(state.getBlock(), mesh, waterlogged);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.getValue(BlockStateProperties.WATERLOGGED);
        List<SiftingRecipe> recipes = ExNihiloRegistries.SIEVE_REGISTRY.getDrops(state.getBlock(), ((MeshType) sieveMesh.getBackingMesh()), waterlogged);
        if (recipes != null) {
            List<ItemStack> list = new ArrayList<>();
            for (SiftingRecipe recipe : recipes) {
                int tries = rand.nextInt((int) luck + 1) + 1;
                for (int i = 0; i < tries; i++) {
                    for (MeshWithChance roll : recipe.getRolls()) {
                        if (rand.nextDouble() < (double) roll.getChance()) {
                            ItemStack itemStack = recipe.getDrop();
                            list.add(itemStack.copy());
                        }
                    }
                }
            }
            return list;
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<ItemStack> rollHeavySieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        return Collections.emptyList();
    }

    @Override
    public Collection<ItemStack> rollCompressedHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        return Collections.emptyList();
    }

    @Override
    public List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand) {
        final float luck = getLuckFromTool(tool);
        if (state.getBlock() instanceof InfestedLeavesBlock) {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(Items.STRING, rand.nextInt(Config.getMaxBonusStringCount()) + Config.getMinStringCount()));
            if (rand.nextDouble() <= 0.8) {
                // list.add(getNihiloItem(NihiloItems.SILK_WORM).copy()); // TODO silk worm item not defined
            }
            return list;
        } else if (!state.is(BlockTags.LEAVES)) {
            return Collections.emptyList();
        }

        List<HarvestRecipe> recipes = ExNihiloRegistries.CROOK_REGISTRY.getDrops(state.getBlock());
        if (recipes != null) {
            List<ItemStack> list = new ArrayList<>();

            for (int i = 0; i < Config.getVanillaSimulateDropCount(); i++) {
                List<ItemStack> items = Block.getDrops(state, level, pos, null);
                list.addAll(items);
            }

            for (final var recipe : recipes) {
                for (ItemStackWithChance drop : recipe.getDrops()) {
                    float fortuneChanceBonus = 0.1f;
                    if (rand.nextFloat() <= drop.getChance() + fortuneChanceBonus * luck) {
                        list.add(drop.getStack().copy());
                    }
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    private float getLuckFromTool(ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
    }

    @Override
    public LootTable generateHeavySieveLootTable(Level level, BlockState sieveState, ItemLike source, int times, SieveMeshRegistryEntry mesh) {
        if (!(mesh.getBackingMesh() instanceof MeshType)) {
            return LootTable.EMPTY;
        }

        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.getValue(BlockStateProperties.WATERLOGGED);
        LootTable.Builder tableBuilder = LootTable.lootTable();
        List<SiftingRecipe> recipes = ExNihiloRegistries.SIEVE_REGISTRY.getDrops(source, ((MeshType) mesh.getBackingMesh()), waterlogged);
        for (final var recipe : recipes) {
            ItemStack itemStack = recipe.getDrop();
            for (MeshWithChance roll : recipe.getRolls()) {
                LootPool.Builder poolBuilder = LootPool.lootPool();
                poolBuilder.name("excompressum-heavysieve-" + Balm.getRegistries().getKey(source.asItem()).toString().replace(':', '-') + "-" + UUID.randomUUID());
                poolBuilder.setRolls(ConstantValue.exactly(times));
                LootPoolSingletonContainer.Builder<?> entryBuilder = buildLootEntry(itemStack);
                entryBuilder.when(LootItemRandomChanceCondition.randomChance(roll.getChance()));
                poolBuilder.add(entryBuilder);
                tableBuilder.withPool(poolBuilder);
            }
        }
        return tableBuilder.build();
    }

    @Override
    public boolean doMeshesHaveDurability() {
        return false;
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
    public List<HammerRecipe> getHammerRecipes() {
        ArrayListMultimap<IntList, CrushingRecipe> groupedRecipes = ArrayListMultimap.create();
        for (final var hammerRecipe : ExNihiloRegistries.HAMMER_REGISTRY.getRecipeList()) {
            groupedRecipes.put(hammerRecipe.getInput().getStackingIds(), hammerRecipe);
        }

        List<HammerRecipe> result = new ArrayList<>();
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
            final var lootTable = tableBuilder.build();
            result.add(new HammerRecipeImpl(firstRecipe.getId(), input, lootTable));
        }

        return result;
    }

    private LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStack outputItem) {
        return LootTableUtils.buildLootEntry(outputItem, -1f);
    }

    private LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStackWithChance outputItem) {
        return LootTableUtils.buildLootEntry(outputItem.getStack(), outputItem.getChance());
    }

    @Override
    public boolean isHeavySiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        return false;
    }

    @Override
    public List<CompressedHammerRecipe> getCompressedHammerRecipes() {
        return List.of();
    }

    @Override
    public List<SieveRecipe> getSieveRecipes() {
        return List.of();
    }

    @Override
    public List<HeavySieveRecipe> getHeavySieveRecipes() {
        return List.of();
    }
}
