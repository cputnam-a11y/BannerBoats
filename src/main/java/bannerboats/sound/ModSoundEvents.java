package bannerboats.sound;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import static bannerboats.BannerBoats.MOD_ID;

public class ModSoundEvents {
    public static final Holder<@NotNull SoundEvent> ENTITY_BOAT_ADD_ITEM = register(
            "bannerboats.entity.boat.add_item"
    );

    public static Holder<@NotNull SoundEvent> register(String name) {
        var id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        return Registry.registerForHolder(
                BuiltInRegistries.SOUND_EVENT,
                id,
                SoundEvent.createVariableRangeEvent(id)
        );
    }

    public static void init() {

    }
}
