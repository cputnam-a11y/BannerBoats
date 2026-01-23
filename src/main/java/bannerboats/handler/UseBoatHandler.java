package bannerboats.handler;

import bannerboats.attachment.ModAttachments;
import bannerboats.sound.ModSoundEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;


public class UseBoatHandler implements UseEntityCallback {

    @Override
    @SuppressWarnings("UnnecessaryDefault")
    public InteractionResult interact(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!(entity instanceof AbstractBoat boat))
            return InteractionResult.PASS;
        if (ModAttachments.hasBanner(boat))
            return InteractionResult.PASS;
        if (boat.hasPassenger(player))
            return InteractionResult.PASS;
        ItemStack stack = switch (hand) {
            case MAIN_HAND -> player.getMainHandItem();
            case OFF_HAND -> player.getOffhandItem();
            default -> ItemStack.EMPTY;
        };
        if (stack.isEmpty() || !stack.is(ItemTags.BANNERS)) {
            return InteractionResult.PASS;
        }
        ItemStack copy = stack.copyWithCount(1);
        if (!world.isClientSide())
            ModAttachments.setBanner(boat, copy);
        world.playSound(player, boat.getX(), boat.getY(), boat.getZ(), ModSoundEvents.ENTITY_BOAT_ADD_ITEM, SoundSource.PLAYERS, 1f, 1f);
        if (!world.isClientSide()) {
            stack.consume(1, player);
            player.setItemInHand(hand, stack);
        }
        return InteractionResult.SUCCESS;
    }

    public static void init() {
        UseEntityCallback.EVENT.register(new UseBoatHandler());
    }
}
