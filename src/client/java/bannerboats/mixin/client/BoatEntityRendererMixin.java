package bannerboats.mixin.client;

import bannerboats.attachment.ModAttachments;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

@Mixin(net.minecraft.client.render.entity.BoatEntityRenderer.class)
public class BoatEntityRendererMixin {

    @Unique
    private static final Supplier<BannerBlockEntity> BANNER_BLOCK_ENTITY = Suppliers.memoize(
            () -> new BannerBlockEntity(
                    BlockPos.ORIGIN,
                    Blocks.WHITE_BANNER.getDefaultState()
            )
    );
    @Unique
    private static final Field POS;
    @Unique
    private static final BiConsumer<BlockEntity, BlockPos> SET_POS;

    static {
        try {
            POS = BlockEntity.class.getDeclaredField("pos");
            POS.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        SET_POS = (be, pos) -> {
            try {
                POS.set(be, pos);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/CompositeEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBoat(BoatEntity boatEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!ModAttachments.hasBanner(boatEntity))
            return;
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f).mul(RotationAxis.POSITIVE_Y.rotationDegrees(90f)));
        matrixStack.translate(-0.5F, 0.15F, -1.45F);
        var be = BANNER_BLOCK_ENTITY.get();
        be.setWorld(boatEntity.getWorld());
        SET_POS.accept(be, boatEntity.getBlockPos());
        ModAttachments.getBanner(boatEntity)
                .map(
                        it -> it.getItem() instanceof BlockItem blockItem
                              ? new Pair<>(it, blockItem.getBlock())
                              : null
                )
                .map(
                        it -> it.getRight() instanceof AbstractBannerBlock abb
                              ? new Pair<>(it.getLeft(), abb.getColor())
                              : null
                ).ifPresent(it -> {
                    var stack = it.getLeft();
                    var color = it.getRight();
                    be.readFrom(stack, color);
                    MinecraftClient.getInstance().getBlockEntityRenderDispatcher().render(
                            be,
                            tickDelta,
                            matrixStack,
                            vertexConsumerProvider
                    );
                });
        be.setWorld(null);
        matrixStack.pop();

    }
}
