package bannerboats.attachment;

import bannerboats.BannerBoats;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class ModAttachments {
    public static final AttachmentType<@NotNull ItemStack> BOAT_BANNER = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(BannerBoats.MOD_ID, "boat_banner"),
            builder -> builder
                    .persistent(ItemStack.OPTIONAL_CODEC)
                    .syncWith(
                            ItemStack.OPTIONAL_STREAM_CODEC,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static void init() {
    }

    public static boolean hasBanner(AbstractBoat boat) {
        return boat.hasAttached(BOAT_BANNER);
    }

    public static Optional<ItemStack> getBanner(VehicleEntity vehicle) {
        return vehicle instanceof AbstractBoat boat
               ? getBanner(boat)
               : Optional.empty();
    }

    public static Optional<ItemStack> getBanner(AbstractBoat boat) {
        return Optional.ofNullable(boat.getAttached(BOAT_BANNER));
    }

    public static void setBanner(AbstractBoat boat, ItemStack banner) {
        boat.setAttached(BOAT_BANNER, banner);
    }

    public static void removeBanner(AbstractBoat boat) {
        boat.removeAttached(BOAT_BANNER);
    }
}
