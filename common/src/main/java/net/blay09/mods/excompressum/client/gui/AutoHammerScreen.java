package net.blay09.mods.excompressum.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.menu.AutoHammerMenu;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AutoHammerScreen extends AbstractContainerScreen<AutoHammerMenu> {

    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "textures/gui/auto_hammer.png");

    public AutoHammerScreen(AutoHammerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(RenderType::guiTextured, texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        AutoHammerBlockEntity blockEntity = menu.getAutoHammer();

        if (blockEntity.isProcessing()) {
            guiGraphics.blit(RenderType::guiTextured, texture, leftPos + 32, topPos + 36, 176, 0, (int) (blockEntity.getProgress() * 15f), 14, 256, 256);
        }
        if (blockEntity.isDisabledByRedstone()) {
            guiGraphics.blit(RenderType::guiTextured, texture, leftPos + 44, topPos + 48, 176, 14, 15, 16, 256, 256);
        }

        float energyPercentage = blockEntity.getEnergyPercentage();
        guiGraphics.blit(RenderType::guiTextured, texture, leftPos + 152, topPos + 8 + (70 - (int) (energyPercentage * 70)), 176 + 15, 0, 16, (int) (energyPercentage * 70), 256, 256);
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (mouseX >= leftPos + 152 && mouseX <= leftPos + 167 && mouseY >= topPos + 8 && mouseY <= topPos + 77) {
            AutoHammerBlockEntity blockEntity = menu.getAutoHammer();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("tooltip.excompressum.energyStored", blockEntity.getEnergyStorage().getEnergy()));
            tooltip.add(Component.translatable("tooltip.excompressum.consumingEnergy", blockEntity.getEffectiveEnergy()));
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX - leftPos, mouseY - topPos);
        }
    }

}
