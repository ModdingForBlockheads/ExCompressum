package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.recipeviewers.ExpandedHeavySieveRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class HeavySieveJeiRecipeCategory implements IRecipeCategory<ExpandedHeavySieveRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "heavy_sieve");
    public static final RecipeType<ExpandedHeavySieveRecipe> TYPE = new RecipeType<>(UID, ExpandedHeavySieveRecipe.class);
    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "textures/gui/jei_heavy_sieve.png");

    private final IDrawable background;
    private final IDrawable icon;

    public HeavySieveJeiRecipeCategory(IJeiHelpers jeiHelpers) {
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.heavySieves[0]));
    }

    @Override
    public RecipeType<ExpandedHeavySieveRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(UID.toString());
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ExpandedHeavySieveRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 62, 10).addIngredients(recipe.getIngredient());
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 88, 10).addItemStacks(recipe.getMeshItems());
        final var outputItems = recipe.getOutputItems();
        for (int i = 0; i < outputItems.size(); i++) {
            final int slotX = 3 + (i % 9 * 18);
            final int slotY = 37 + (i / 9 * 18);
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(outputItems.get(i));
        }
    }

}
