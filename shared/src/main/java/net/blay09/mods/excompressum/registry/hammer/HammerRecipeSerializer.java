package net.blay09.mods.excompressum.registry.hammer;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

public class HammerRecipeSerializer extends ExCompressumRecipeSerializer<HammerRecipe> {

    @Override
    public HammerRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        LootTable lootTable = readLootTable(json, "lootTable", id);
        return new HammerRecipe(id, input, lootTable);
    }

    @Override
    public HammerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        LootTable lootTable = readLootTable(buffer, id);
        return new HammerRecipe(id, input, lootTable);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, HammerRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        writeLootTable(buffer, recipe.getLootTable());
    }

}
