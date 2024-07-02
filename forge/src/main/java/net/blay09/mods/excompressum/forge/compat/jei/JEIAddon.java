package net.blay09.mods.excompressum.forge.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<JeiHeavySieveRecipe> jeiHeavySieveRecipes = new ArrayList<>();

        final var level = Minecraft.getInstance().level;
        final var recipeManager = level.getRecipeManager();

        final var heavySieveRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        for (final var recipe : heavySieveRecipes) {
            jeiHeavySieveRecipes.add(new JeiHeavySieveRecipe(recipe));
        }

        final var generatedHeavySieveRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (final var recipe : generatedHeavySieveRecipes) {
            loadGeneratedHeavySieveRecipe(level, false, recipe, jeiHeavySieveRecipes);
            loadGeneratedHeavySieveRecipe(level, true, recipe, jeiHeavySieveRecipes);
        }

        registry.addRecipes(HeavySieveRecipeCategory.TYPE, jeiHeavySieveRecipes);

        List<JeiCompressedHammerRecipe> jeiCompressedHammerRecipes = new ArrayList<>();
        final var compressedHammerRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        for (final var recipe : compressedHammerRecipes) {
            jeiCompressedHammerRecipes.add(new JeiCompressedHammerRecipe(recipe));
        }
        registry.addRecipes(CompressedHammerRecipeCategory.TYPE, jeiCompressedHammerRecipes);

        List<JeiHammerRecipe> jeiHammerRecipes = new ArrayList<>();
        final var hammerRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.hammerRecipeType);
        for (final var recipe : hammerRecipes) {
            jeiHammerRecipes.add(new JeiHammerRecipe(recipe));
        }
        for (final var recipe : ExNihilo.getInstance().getHammerRecipes()) {
            jeiHammerRecipes.add(new JeiHammerRecipe(recipe));
        }
        registry.addRecipes(HammerRecipeCategory.TYPE, jeiHammerRecipes);

        List<JeiChickenStickRecipe> jeiChickenStickRecipes = new ArrayList<>();
        final var chickenStickRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.chickenStickRecipeType);
        for (final var recipe : chickenStickRecipes) {
            jeiChickenStickRecipes.add(new JeiChickenStickRecipe(recipe));
        }
        registry.addRecipes(ChickenStickJeiRecipeCategory.TYPE, jeiChickenStickRecipes);

        ArrayListMultimap<ResourceLocation, WoodenCrucibleRecipe> fluidOutputMap = ArrayListMultimap.create();
        final var woodenCrucibleRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.woodenCrucibleRecipeType);
        for (final var entry : woodenCrucibleRecipes) {
            fluidOutputMap.put(entry.getFluidId(), entry);
        }

        List<JeiWoodenCrucibleRecipe> jeiWoodenCrucibleRecipes = new ArrayList<>();
        for (final var fluidName : fluidOutputMap.keySet()) {
            final var fluid = Balm.getRegistries().getFluid(fluidName);
            if (fluid == null) {
                continue;
            }

            final var recipes = fluidOutputMap.get(fluidName);
            List<Pair<WoodenCrucibleRecipe, ItemStack>> inputs = new ArrayList<>();
            for (final var meltable : recipes) {
                for (ItemStack matchingStack : meltable.getInput().getItems()) {
                    inputs.add(Pair.of(meltable, matchingStack));
                }
            }

            inputs.sort(Comparator.comparingInt((Pair<WoodenCrucibleRecipe, ItemStack> pair) -> pair.getFirst().getAmount()).reversed());

            final int pageSize = 45;
            final var pages = Lists.partition(inputs, pageSize);
            for (final var page : pages) {
                jeiWoodenCrucibleRecipes.add(new JeiWoodenCrucibleRecipe(fluid, page));
            }
        }

        registry.addRecipes(WoodenCrucibleRecipeCategory.TYPE, jeiWoodenCrucibleRecipes);

        registry.addRecipes(CraftChickenStickRecipeCategory.TYPE, Lists.newArrayList(new CraftChickenStickRecipe()));
    }

    private void loadGeneratedHeavySieveRecipe(Level level, boolean waterlogged, GeneratedHeavySieveRecipe generatedRecipe, List<JeiHeavySieveRecipe> outRecipes) {
        final var waterLoggedState = ModBlocks.heavySieves[0].defaultBlockState().setValue(HeavySieveBlock.WATERLOGGED, waterlogged);
        for (final var mesh : SieveMeshRegistry.getEntries().values()) {
            final var rolls = HeavySieveRegistry.getGeneratedRollCount(generatedRecipe);
            final var source = Balm.getRegistries().getItem(generatedRecipe.getSource());
            final var lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(level, waterLoggedState, source, rolls, mesh);
            if (!LootTableUtils.isLootTableEmpty(lootTable)) {
                final var recipe = new HeavySieveRecipe(generatedRecipe.getRecipeId(), generatedRecipe.getInput(), lootTable, waterlogged, null, Sets.newHashSet(mesh.getMeshType()));
                outRecipes.add(new JeiHeavySieveRecipe(recipe));
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        for (final var heavySieve : ModBlocks.heavySieves) {
            registry.addRecipeCatalyst(new ItemStack(heavySieve), HeavySieveRecipeCategory.TYPE);
        }
        for (final var woodenCrucible : ModBlocks.woodenCrucibles) {
            registry.addRecipeCatalyst(new ItemStack(woodenCrucible), WoodenCrucibleRecipeCategory.TYPE);
        }
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoCompressedHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedNetheriteHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedDiamondHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedGoldenHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedIronHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedStoneHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.compressedWoodenHammer), CompressedHammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(new ItemStack(ModItems.chickenStick), ChickenStickJeiRecipeCategory.TYPE);

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoHammer), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_NETHERITE), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_GOLD), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_IRON), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_STONE), HammerRecipeCategory.TYPE);
        registry.addRecipeCatalyst(ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_WOODEN), HammerRecipeCategory.TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExCompressum.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new HeavySieveRecipeCategory(registry.getJeiHelpers()),
                new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CompressedHammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new WoodenCrucibleRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CraftChickenStickRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ChickenStickJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

}
