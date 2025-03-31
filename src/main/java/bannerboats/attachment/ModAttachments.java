package bannerboats.attachment;

import bannerboats.BannerBoats;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Optional;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class ModAttachments {
    public static final AttachmentType<ItemStack> BOAT_BANNER = AttachmentRegistry.create(
            Identifier.of(BannerBoats.MOD_ID, "boat_banner"),
            builder -> builder
                    .persistent(ItemStack.OPTIONAL_CODEC)
                    .syncWith(
                            ItemStack.OPTIONAL_PACKET_CODEC,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static void init() {
    }

    public static boolean hasBanner(AbstractBoatEntity boat) {
        return boat.hasAttached(BOAT_BANNER);
    }

    public static Optional<ItemStack> getBanner(VehicleEntity vehicle) {
        return vehicle instanceof AbstractBoatEntity boat
               ? getBanner(boat)
               : Optional.empty();
    }

    public static Optional<ItemStack> getBanner(AbstractBoatEntity boat) {
        return Optional.ofNullable(boat.getAttached(BOAT_BANNER));
    }

    public static void setBanner(AbstractBoatEntity boat, ItemStack banner) {
        boat.setAttached(BOAT_BANNER, banner);
    }

    public static void removeBanner(AbstractBoatEntity boat) {
        boat.removeAttached(BOAT_BANNER);
    }
}
