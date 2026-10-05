package net.minlauncher.client.cosmetics.wings;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class CosmeticWingsModel extends Model<AvatarRenderState> {
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public CosmeticWingsModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        CubeListBuilder leftBuilder = CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(0.0F, -6.0F, -1.0F, 3.0F, 14.0F, 3.0F);

        CubeListBuilder rightBuilder = CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-3.0F, -6.0F, -1.0F, 3.0F, 14.0F, 3.0F);

        root.addOrReplaceChild("left_wing", leftBuilder, PartPose.offsetAndRotation(1.0F, -2.0F, 2.0F, 0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("right_wing", rightBuilder, PartPose.offsetAndRotation(-1.0F, -2.0F, 2.0F, 0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(AvatarRenderState state) {
        super.setupAnim(state);
        float time = (float) System.currentTimeMillis() * 0.008f;
        float flap = 0.25f + Mth.sin(time) * 0.2f;

        if (state.isCrouching) {
            leftWing.xRot = 0.6f;
            leftWing.yRot = -0.5f + flap;
            rightWing.xRot = 0.6f;
            rightWing.yRot = 0.5f - flap;
        } else {
            leftWing.xRot = 0.15f;
            leftWing.yRot = -0.25f + flap;
            rightWing.xRot = 0.15f;
            rightWing.yRot = 0.25f - flap;
        }
    }
}
