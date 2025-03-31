package bannerboats.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static bannerboats.BannerBoats.MOD_ID;

public class ModSoundEvents {
    public static final RegistryEntry<SoundEvent> ENTITY_BOAT_ADD_ITEM = register(
            "bannerboats.entity.boat.add_item"
    );

    public static RegistryEntry<SoundEvent> register(String name) {
        var id = Identifier.of(MOD_ID, name);
        return Registry.registerReference(
                Registries.SOUND_EVENT,
                id,
                SoundEvent.of(id)
        );
    }

    public static void init() {

    }
}
