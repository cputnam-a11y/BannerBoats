package bannerboats.mixin;

import bannerboats.attachment.ModAttachments;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

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
		var o = BANNER.get();
		try {
			ModAttachments.getBanner(instance).ifPresent(BANNER::set);
			original.call(instance, world, item);
		} finally {
			if (o != null) BANNER.set(o);
			else BANNER.remove();
		}
	}
}
