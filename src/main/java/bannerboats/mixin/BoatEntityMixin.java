package bannerboats.mixin;

import bannerboats.attachment.ModAttachments;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends VehicleEntityMixin {
    @Unique
    private static final ThreadLocal<ItemStack> BANNER = ThreadLocal.withInitial(() -> null);

    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void afterDropSelf(Item item, CallbackInfo ci) {
        super.afterDropSelf(item, ci);
        var stack = BANNER.get();
        if (stack != null)
            dropStack(stack);
    }

    @Override
    protected void wrapDropSelf(VehicleEntity instance, Item item, Operation<Void> original) {
        ModAttachments.getBanner(instance).ifPresent(BANNER::set);
        original.call(instance, item);
        BANNER.remove();
    }
}
