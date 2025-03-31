package bannerboats.mixin.client;

import bannerboats.attachment.ModAttachments;
import bannerboats.pond.BoatEntityRenderStateDuck;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.AbstractBoatEntityRenderer;
import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(AbstractBoatEntityRenderer.class)
public class AbstractBoatEntityRendererMixin {
    @Unique
    private static final Supplier<Map<Item, DyeColor>> BANNER_COLORS = Suppliers.memoize(
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
    private static final Supplier<BannerBlockEntityRenderer> BANNER_BLOCK_ENTITY_RENDERER = Suppliers.memoize(
            () -> new BannerBlockEntityRenderer(
                    MinecraftClient.getInstance().getLoadedEntityModels()
            )
    );

    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/vehicle/AbstractBoatEntity;Lnet/minecraft/client/render/entity/state/BoatEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void onUpdateRenderState(AbstractBoatEntity abstractBoatEntity, BoatEntityRenderState boatEntityRenderState, float f, CallbackInfo ci) {
        if (boatEntityRenderState instanceof BoatEntityRenderStateDuck duck) {
            var stack = ModAttachments.getBanner(abstractBoatEntity);
            stack.ifPresentOrElse(stack1 -> {
                duck.bannerBoats$setBannerColor(BANNER_COLORS.get().getOrDefault(stack1.getItem(), DyeColor.WHITE));
                duck.bannerBoats$setPatterns(stack1.get(DataComponentTypes.BANNER_PATTERNS));
            }, () -> {
                duck.bannerBoats$setBannerColor(null);
                duck.bannerBoats$setPatterns(null);
            });
        }

    }

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/BoatEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBoat(BoatEntityRenderState boatEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!(boatEntityRenderState instanceof BoatEntityRenderStateDuck duck))
            return;
        if (duck.bannerBoats$patterns() == null || duck.bannerBoats$bannerColor() == null)
            return;
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f).mul(RotationAxis.POSITIVE_Y.rotationDegrees(90f)));
        matrixStack.translate(-0.5F, 0.15F, -1.45F);
        BANNER_BLOCK_ENTITY_RENDERER.get().renderAsItem(
                matrixStack,
                vertexConsumerProvider,
                i,
                OverlayTexture.DEFAULT_UV,
                duck.bannerBoats$bannerColor(),
                duck.bannerBoats$patterns()
        );
        matrixStack.pop();
    }
}
