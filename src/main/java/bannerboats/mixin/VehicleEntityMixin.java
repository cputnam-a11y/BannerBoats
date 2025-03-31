package bannerboats.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VehicleEntity.class)
public abstract class VehicleEntityMixin extends Entity {
    public VehicleEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(
            method = "killAndDropItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/vehicle/VehicleEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;",
                    shift = At.Shift.AFTER
            )
    )
    protected void afterDropSelf(ServerWorld world, Item item, CallbackInfo ci) {
    }

    @WrapOperation(
            method = "killAndDropSelf",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/vehicle/VehicleEntity;killAndDropItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/Item;)V"
            )
    )
    protected void wrapDropSelf(VehicleEntity instance, ServerWorld world, Item item, Operation<Void> original) {

    }
}
