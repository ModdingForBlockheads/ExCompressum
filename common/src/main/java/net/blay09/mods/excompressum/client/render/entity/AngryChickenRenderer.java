package net.blay09.mods.excompressum.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.ResourceLocation;

public class AngryChickenRenderer extends MobRenderer<AngryChickenEntity, ChickenRenderState, ChickenModel> {
    private static final ResourceLocation chickenTextures = ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, "textures/entity/angry_chicken.png");

    public AngryChickenRenderer(EntityRendererProvider.Context context, ChickenModel model, float shadowSize) {
        super(context, model, shadowSize);
    }

    @Override
    public ResourceLocation getTextureLocation(ChickenRenderState entity) {
        return chickenTextures;
    }

    @Override
    public void render(ChickenRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        poseStack.pushPose();
        float scale = renderState.scale;
        poseStack.scale(scale, scale, scale);
        super.render(renderState, poseStack, buffer, packedLightIn);
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(AngryChickenEntity angryChicken, ChickenRenderState renderState, float delta) {
        super.extractRenderState(angryChicken, renderState, delta);
        renderState.scale = angryChicken.getAngryScale();
    }

    @Override
    public ChickenRenderState createRenderState() {
        return new ChickenRenderState();
    }


}
