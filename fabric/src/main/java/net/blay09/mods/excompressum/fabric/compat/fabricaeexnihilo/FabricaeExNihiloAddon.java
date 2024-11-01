package net.blay09.mods.excompressum.fabric.compat.fabricaeexnihilo;

import com.google.common.collect.ArrayListMultimap;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.sieve.SieveRecipeImpl;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;

import java.util.*;

public class FabricaeExNihiloAddon implements ExNihiloProvider {

    private final Map<CommonMeshType, ResourceLocation> meshTypeToMeshId = new HashMap<>();
    private final Map<ResourceLocation, CommonMeshType> meshIdToMeshType = new HashMap<>();

    public FabricaeExNihiloAddon() {
        ExNihilo.setInstance(this);

        final var stringMeshItem = findItem("string_mesh");
        if (!stringMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "string_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.STRING, stringMeshItem, backingMesh);
            mesh.setModelName("string");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.STRING);
            meshTypeToMeshId.put(CommonMeshType.STRING, backingMesh);
        }

        final var flintMeshItem = findItem("flint_mesh");
        if (!flintMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "flint_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.FLINT, flintMeshItem, backingMesh);
            mesh.setModelName("flint");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.FLINT);
            meshTypeToMeshId.put(CommonMeshType.FLINT, backingMesh);
        }

        final var ironMeshItem = findItem("iron_mesh");
        if (!ironMeshItem.isEmpty()) {
            ResourceLocation backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "iron_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.IRON, ironMeshItem, backingMesh);
            mesh.setHeavy(true);
            mesh.setModelName("iron");
            SieveMeshRegistry.registerDefaults(backingMesh);
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.IRON);
            meshTypeToMeshId.put(CommonMeshType.IRON, backingMesh);
        }

        final var copperMeshItem = findItem("copper_mesh");
        if (!copperMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "copper_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.COPPER, copperMeshItem, backingMesh);
            mesh.setHeavy(true);
            mesh.setModelName("copper");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.COPPER);
            meshTypeToMeshId.put(CommonMeshType.COPPER, backingMesh);
        }

        final var goldMeshItem = findItem("gold_mesh");
        if (!goldMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "gold_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.GOLD, goldMeshItem, backingMesh);
            mesh.setHeavy(true);
            mesh.setModelName("gold");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.GOLD);
            meshTypeToMeshId.put(CommonMeshType.GOLD, backingMesh);
        }

        final var diamondMeshItem = findItem("diamond_mesh");
        if (!diamondMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "diamond_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.DIAMOND, diamondMeshItem, backingMesh);
            mesh.setHeavy(true);
            mesh.setModelName("diamond");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.DIAMOND);
            meshTypeToMeshId.put(CommonMeshType.DIAMOND, backingMesh);
        }

        final var emeraldMeshItem = findItem("emerald_mesh");
        if (!emeraldMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "emerald_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.EMERALD, emeraldMeshItem, backingMesh);
            mesh.setHeavy(true);
            mesh.setModelName("emerald");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.EMERALD);
            meshTypeToMeshId.put(CommonMeshType.EMERALD, backingMesh);
        }

        final var netheriteMeshItem = findItem("netherite_mesh");
        if (!netheriteMeshItem.isEmpty()) {
            final var backingMesh = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, "netherite_mesh");
            final var mesh = new SieveMeshRegistryEntry(CommonMeshType.NETHERITE, netheriteMeshItem, backingMesh);
            mesh.setHeavy(true);
            mesh.setModelName("netherite");
            SieveMeshRegistry.add(mesh);
            meshIdToMeshType.put(backingMesh, CommonMeshType.NETHERITE);
            meshTypeToMeshId.put(CommonMeshType.NETHERITE, backingMesh);
        }
    }

    private ItemStack findItem(String name) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Compat.FABRICAE_EX_NIHILO, name);
        Item item = Balm.getRegistries().getItem(location);
        return new ItemStack(item);
    }

    @Override
    public boolean isHammerable(Level level, BlockState state) {
        final var recipes = ToolRecipe.find(ToolRecipe.ToolType.HAMMER, state, level);
        return !recipes.isEmpty();
    }

    @Override
    public boolean isHammerableCompressed(ItemStack itemStack) {
        return false;
    }

    @Override
    public List<ItemStack> rollHammerRewards(Level level, BlockState state, ItemStack toolItem, RandomSource rand) {
        List<ItemStack> drops = new ArrayList<>();
        final var recipes = ToolRecipe.find(ToolRecipe.ToolType.HAMMER, state, level);
        for (final var recipe : recipes) {
            drops.add(recipe.getResult().createStack(level.random));
        }

        return drops;
    }

    @Override
    public boolean isSiftableWithMesh(Level level, BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        if (sieveMesh == null) {
            return false;
        }

        final var itemStack = StupidUtils.getItemStackFromState(state);
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.getValue(BlockStateProperties.WATERLOGGED);
        final var recipes = wraith.fabricaeexnihilo.recipe.SieveRecipe.find(itemStack.getItem(),
                waterlogged,
                (ResourceLocation) sieveMesh.getBackingMesh(),
                level);
        return !recipes.isEmpty();
    }

    @Override
    public boolean isHeavySiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        return false;
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        final var sourceStack = StupidUtils.getItemStackFromState(state);
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.getValue(BlockStateProperties.WATERLOGGED);
        final var recipes = wraith.fabricaeexnihilo.recipe.SieveRecipe.find(sourceStack.getItem(),
                waterlogged,
                (ResourceLocation) sieveMesh.getBackingMesh(),
                level);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            list.add(recipe.createStack(level.random));
        }
        return list;
    }

    @Override
    public Collection<ItemStack> rollHeavySieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        return List.of();
    }

    @Override
    public Collection<ItemStack> rollCompressedHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        return List.of();
    }

    @Override
    public List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand) {
        final var recipes = ToolRecipe.find(ToolRecipe.ToolType.CROOK, state, level);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            list.add(recipe.getResult().createStack(level.random));
        }
        return list;
    }

    @Override
    public LootTable generateHeavySieveLootTable(Level level, BlockState sieveState, ItemLike source, int count, SieveMeshRegistryEntry mesh) {
        if (!(mesh.getBackingMesh() instanceof ResourceLocation)) {
            return LootTable.EMPTY;
        }

        LootTable.Builder tableBuilder = LootTable.lootTable();
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.getValue(BlockStateProperties.WATERLOGGED);
        final var recipes = wraith.fabricaeexnihilo.recipe.SieveRecipe.find(source.asItem(), waterlogged, (ResourceLocation) mesh.getBackingMesh(), level);
        for (final var recipe : recipes) {
            tableBuilder.withPool(buildLootPool(recipe).setRolls(ConstantValue.exactly(count)));
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
        List<HammerRecipe> result = new ArrayList<>();

        ArrayListMultimap<IntList, RecipeHolder<ToolRecipe>> groupedRecipes = ArrayListMultimap.create();
        // for (final var hammerRecipe : recipeManager.getAllRecipesFor(ModRecipes.HAMMER)) {
        //     groupedRecipes.put(fromBlockIngredient(hammerRecipe.getBlock()).getStackingIds(), hammerRecipe);
        // }

        for (final var packedStacks : groupedRecipes.keySet()) {
            final var tableBuilder = LootTable.lootTable();
            for (final var hammerRecipe : groupedRecipes.get(packedStacks)) {
                tableBuilder.withPool(buildLootPool(hammerRecipe.value().getResult()));
            }

            final var firstRecipe = groupedRecipes.get(packedStacks).get(0);
            final var input = fromBlockIngredient(firstRecipe.value().getBlock());
            final var lootTableProvider = tableBuilder.build();
            result.add(new HammerRecipeImpl(input, lootTableProvider));
        }

        return result;
    }

    private Ingredient fromBlockIngredient(BlockIngredient blockIngredient) {
        return blockIngredient.getValue().map(block -> Ingredient.of(block.asItem()), tag -> {
            final var items = new ArrayList<ItemLike>();
            for (final var blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
                final var item = blockHolder.value().asItem();
                if (item != Items.AIR) {
                    items.add(item);
                }
            }
            return Ingredient.of(items.toArray(new ItemLike[0]));
        });
    }

    @Override
    public List<CompressedHammerRecipe> getCompressedHammerRecipes() {
        return List.of();
    }

    @Override
    public List<SieveRecipe> getSieveRecipes() {
        List<SieveRecipe> result = new ArrayList<>();

        ArrayListMultimap<Pair<IntList, CommonMeshType>, RecipeHolder<wraith.fabricaeexnihilo.recipe.SieveRecipe>> groupedRecipes = ArrayListMultimap.create();
        // final var sieveRecipes = recipeManager.getAllRecipesFor(ModRecipes.SIEVE);
        // for (final var recipe : sieveRecipes) {
        //     for (final var meshId : recipe.getRolls().keySet()) {
        //         final var mesh = meshIdToMeshType.get(meshId);
        //         groupedRecipes.put(Pair.of(recipe.getInput().getStackingIds(), mesh), recipe);
        //     }
        // }

        for (final var packedStacks : groupedRecipes.keySet()) {
            final var tableBuilder = LootTable.lootTable();
            for (final var recipe : groupedRecipes.get(packedStacks)) {
                final var meshType = packedStacks.getSecond();
                final var meshId = meshTypeToMeshId.get(meshType);
                final var rolls = recipe.value().getRolls().get(meshId);
                if (rolls != null) {
                    tableBuilder.withPool(buildLootPool(new Loot(recipe.value().getResult(), rolls)));
                }
            }

            final var firstRecipe = groupedRecipes.get(packedStacks).get(0);
            final var ingredient = firstRecipe.value().getInput();
            final var lootTable = tableBuilder.build();
            result.add(new SieveRecipeImpl(ingredient, lootTable, firstRecipe.value().isWaterlogged(), Set.of(packedStacks.getSecond())));
        }

        return result;
    }

    @Override
    public List<HeavySieveRecipe> getHeavySieveRecipes() {
        return List.of();
    }

    private LootPool.Builder buildLootPool(Loot loot) {
        final var poolBuilder = LootPool.lootPool();
        for (final var chance : loot.chances()) {
            poolBuilder.add(LootTableUtils.buildLootEntry(loot.stack(), chance.floatValue()));
        }
        return poolBuilder;
    }

}
