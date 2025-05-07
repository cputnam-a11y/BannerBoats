package bannerboats.handler;

import bannerboats.attachment.ModAttachments;
import bannerboats.sound.ModSoundEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UseBoatHandler implements UseEntityCallback {

    @Override
    @SuppressWarnings("UnnecessaryDefault")
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!(entity instanceof BoatEntity boat))
            return ActionResult.PASS;
        if (ModAttachments.hasBanner(boat))
            return ActionResult.PASS;
        if (boat.hasPassenger(player))
            return ActionResult.PASS;
        ItemStack stack = switch (hand) {
            case MAIN_HAND -> player.getMainHandStack();
            case OFF_HAND -> player.getOffHandStack();
            default -> ItemStack.EMPTY;
        };
        if (stack.isEmpty() || !stack.isIn(ItemTags.BANNERS)) {
            return ActionResult.PASS;
        }
        ItemStack copy = stack.copyWithCount(1);
        if (!world.isClient())
            ModAttachments.setBanner(boat, copy);
        world.playSound(player, boat.getX(), boat.getY(), boat.getZ(), ModSoundEvents.ENTITY_BOAT_ADD_ITEM, SoundCategory.PLAYERS, 1f, 1f);
        if (!world.isClient()) {
            stack.decrementUnlessCreative(1, player);
            player.setStackInHand(hand, stack);
        }
        return ActionResult.SUCCESS;
    }

    public static void init() {
        UseEntityCallback.EVENT.register(new UseBoatHandler());
    }
}
