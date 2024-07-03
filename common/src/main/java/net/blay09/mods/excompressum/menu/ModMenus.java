package net.blay09.mods.excompressum.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import java.util.Objects;

public class ModMenus {
    public static DeferredObject<MenuType<AutoCompressorMenu>> autoCompressor;
    public static DeferredObject<MenuType<AutoHammerMenu>> autoHammer;
    public static DeferredObject<MenuType<AutoSieveMenu>> autoSieve;

    public static void initialize(BalmMenus menus) {
        autoCompressor = menus.registerMenu(id("auto_compressor"), new BalmMenuFactory<AutoCompressorMenu, BlockPos>() {
                    @Override
                    public AutoCompressorMenu create(int windowId, Inventory inventory, BlockPos pos) {
                        final var blockEntity = inventory.player.level().getBlockEntity(pos);
                        return new AutoCompressorMenu(windowId, inventory, (AutoCompressorBlockEntity) Objects.requireNonNull(blockEntity));
                    }

                    @Override
                    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                        return BlockPos.STREAM_CODEC.cast();
                    }
                }
        );

        autoHammer = menus.registerMenu(id("auto_hammer"), new BalmMenuFactory<AutoHammerMenu, BlockPos>() {
                    @Override
                    public AutoHammerMenu create(int windowId, Inventory inventory, BlockPos pos) {
                        final var blockEntity = inventory.player.level().getBlockEntity(pos);
                        return new AutoHammerMenu(windowId, inventory, (AutoHammerBlockEntity) Objects.requireNonNull(blockEntity));
                    }

                    @Override
                    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                        return BlockPos.STREAM_CODEC.cast();
                    }
                }
        );

        autoSieve = menus.registerMenu(id("auto_sieve"), new BalmMenuFactory<AutoSieveMenu, BlockPos>() {
                    @Override
                    public AutoSieveMenu create(int windowId, Inventory inventory, BlockPos pos) {
                        final var blockEntity = inventory.player.level().getBlockEntity(pos);
                        return new AutoSieveMenu(autoSieve.get(), windowId, inventory, (AbstractAutoSieveBlockEntity) Objects.requireNonNull(blockEntity));
                    }

                    @Override
                    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                        return BlockPos.STREAM_CODEC.cast();
                    }
                }
        );
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }
}
