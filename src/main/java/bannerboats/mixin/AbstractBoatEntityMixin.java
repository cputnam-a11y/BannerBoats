package bannerboats.mixin;

import bannerboats.attachment.ModAttachments;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatEntity.class)
public abstract class AbstractBoatEntityMixin extends VehicleEntityMixin {
    @Unique
    private static final ThreadLocal<ItemStack> BANNER = ThreadLocal.withInitial(() -> null);

    public AbstractBoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void afterDropSelf(ServerWorld world, Item item, CallbackInfo ci) {
        super.afterDropSelf(world, item, ci);
        var stack = BANNER.get();
        if (stack != null)
            dropStack(world, stack);
    }

    @Override
    protected void wrapDropSelf(VehicleEntity instance, ServerWorld world, Item item, Operation<Void> original) {
        ModAttachments.getBanner(instance).ifPresent(BANNER::set);
        original.call(instance, world, item);
        BANNER.remove();
    }
}
