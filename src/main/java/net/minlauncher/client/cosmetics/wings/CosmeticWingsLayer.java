package net.minlauncher.client.cosmetics.wings;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minlauncher.client.MinLauncher;
import net.minlauncher.client.cosmetics.Cosmetic;
import net.minlauncher.client.cosmetics.CosmeticType;

public class CosmeticWingsLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final CosmeticWingsModel wingsModel;

    public CosmeticWingsLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
        this.wingsModel = new CosmeticWingsModel(CosmeticWingsModel.createLayer().bakeRoot());
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, AvatarRenderState state, float yRot, float xRot) {
        if (MinLauncher.getInstance() == null || MinLauncher.getInstance().getCosmeticsManager() == null) return;
        Cosmetic equippedWings = MinLauncher.getInstance().getCosmeticsManager().getEquipped(CosmeticType.WINGS);
        if (equippedWings == null) return;

        poseStack.pushPose();
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.translate(0.0F, 0.1F, 0.15F);

        this.wingsModel.setupAnim(state);
        submitNodeCollector.submitModel(
                this.wingsModel,
                state,
                poseStack,
                RenderTypes.entityTranslucent(equippedWings.getTexture()),
                packedLight,
                0,
                0,
                null
        );

        poseStack.popPose();
    }
}
