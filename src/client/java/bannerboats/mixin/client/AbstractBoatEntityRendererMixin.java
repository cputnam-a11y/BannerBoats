package bannerboats.mixin.client;

import bannerboats.attachment.ModAttachments;
import bannerboats.pond.BoatEntityRenderStateDuck;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Util;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(AbstractBoatRenderer.class)
public class AbstractBoatEntityRendererMixin {
    @Unique
    private static final Supplier<@NotNull Map<Item, DyeColor>> BANNER_COLORS = Suppliers.memoize(
            () -> Util.make(new HashMap<>(), map -> {
                map.put(Items.WHITE_BANNER, DyeColor.WHITE);
                map.put(Items.ORANGE_BANNER, DyeColor.ORANGE);
                map.put(Items.MAGENTA_BANNER, DyeColor.MAGENTA);
                map.put(Items.LIGHT_BLUE_BANNER, DyeColor.LIGHT_BLUE);
                map.put(Items.YELLOW_BANNER, DyeColor.YELLOW);
                map.put(Items.LIME_BANNER, DyeColor.LIME);
                map.put(Items.PINK_BANNER, DyeColor.PINK);
                map.put(Items.GRAY_BANNER, DyeColor.GRAY);
                map.put(Items.LIGHT_GRAY_BANNER, DyeColor.LIGHT_GRAY);
                map.put(Items.CYAN_BANNER, DyeColor.CYAN);
                map.put(Items.PURPLE_BANNER, DyeColor.PURPLE);
                map.put(Items.BLUE_BANNER, DyeColor.BLUE);
                map.put(Items.BROWN_BANNER, DyeColor.BROWN);
                map.put(Items.GREEN_BANNER, DyeColor.GREEN);
                map.put(Items.RED_BANNER, DyeColor.RED);
                map.put(Items.BLACK_BANNER, DyeColor.BLACK);
            }));

    @Unique
    private static final Supplier<@NotNull BannerRenderer> BANNER_BLOCK_ENTITY_RENDERER = Suppliers.memoize(
            () -> new BannerRenderer(
                    Minecraft.getInstance().getEntityModels(),
                    Minecraft.getInstance().getAtlasManager()
            )
    );

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V",
            at = @At("TAIL")
    )
    private void onUpdateRenderState(AbstractBoat abstractBoatEntity, BoatRenderState boatEntityRenderState, float f, CallbackInfo ci) {
        if (boatEntityRenderState instanceof BoatEntityRenderStateDuck duck) {
            var stack = ModAttachments.getBanner(abstractBoatEntity);
            stack.ifPresentOrElse(stack1 -> {
                duck.bannerBoats$setBannerColor(BANNER_COLORS.get().getOrDefault(stack1.getItem(), DyeColor.WHITE));
                duck.bannerBoats$setPatterns(stack1.get(DataComponents.BANNER_PATTERNS));
            }, () -> {
                duck.bannerBoats$setBannerColor(null);
                duck.bannerBoats$setPatterns(null);
            });
        }

    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBoat(BoatRenderState boatRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!(boatRenderState instanceof BoatEntityRenderStateDuck duck))
            return;
        if (duck.bannerBoats$patterns() == null || duck.bannerBoats$bannerColor() == null)
            return;
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(180f).mul(Axis.YP.rotationDegrees(90f)));
        poseStack.translate(-0.5F, 0.15F, -1.45F);
        BANNER_BLOCK_ENTITY_RENDERER.get().submitSpecial(
                poseStack,
                submitNodeCollector,
                boatRenderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                duck.bannerBoats$bannerColor(),
                duck.bannerBoats$patterns(),
                boatRenderState.outlineColor
        );
        poseStack.popPose();
    }
}
