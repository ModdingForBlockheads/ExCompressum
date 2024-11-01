package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.component.ModComponents;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ChickenStickItem extends DiggerItem {

    public static final ToolMaterial CHICKEN_STICK_TIER = new ToolMaterial(ModBlockTags.INCORRECT_FOR_CHICKEN_STICK,
            0,
            ToolMaterial.DIAMOND.speed(),
            0f,
            1,
            ModItemTags.CHICKEN_STICK_TOOL_MATERIALS);

    public ChickenStickItem(Item.Properties properties) {
        super(CHICKEN_STICK_TIER, ModBlockTags.MINEABLE_WITH_HAMMER, 6f, -3.2f, properties.fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        tryPlayChickenSound(attacker.level(), attacker.blockPosition());
        return super.hurtEnemy(itemStack, attacker, target);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        tryPlayChickenSound(level, player.blockPosition());
        player.swing(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack itemStack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.get().getRecipeManager(null);
        return ExRegistries.getChickenStickRegistry().isHammerable(recipeManager, new ItemStack(state.getBlock()));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        RecipeManager recipeManager = ExCompressum.proxy.get().getRecipeManager(null);
        if ((ExRegistries.getChickenStickRegistry().isHammerable(recipeManager, new ItemStack(state.getBlock())))) {
            if (isAngry(stack)) {
                return CHICKEN_STICK_TIER.speed() * 1.5f;
            }
            return CHICKEN_STICK_TIER.speed();
        }
        return 0.8f;
    }

    public void tryPlayChickenSound(LevelAccessor level, BlockPos pos) {
        if (level.getRandom().nextFloat() <= ExCompressumConfig.getActive().tools.chickenStickSoundChance) {
            ResourceLocation location = null;
            final List<? extends String> chickenStickSounds = ExCompressumConfig.getActive().tools.chickenStickSounds;
            if (!chickenStickSounds.isEmpty()) {
                location = ResourceLocation.parse(chickenStickSounds.get(level.getRandom().nextInt(chickenStickSounds.size())));
            }
            if (location != null) {
                final var soundEvent = BuiltInRegistries.SOUND_EVENT.getValue(location);
                if (soundEvent != null) {
                    level.playSound(null, pos, soundEvent, SoundSource.PLAYERS, 1f, level.getRandom().nextFloat() * 0.1f + 0.9f);
                } else {
                    ExCompressum.logger.warn("Chicken Stick tried to play a sound that does not exist: {}", location);
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isAngry(stack);
    }

    public boolean isAngry(ItemStack itemStack) {
        return itemStack.has(ModComponents.angry.get());
    }

}
