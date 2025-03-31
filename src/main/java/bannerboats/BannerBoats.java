package bannerboats;

import bannerboats.attachment.ModAttachments;
import bannerboats.handler.UseBoatHandler;
import bannerboats.sound.ModSoundEvents;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class BannerBoats implements ModInitializer {
    public static final String MOD_ID = "bannerboats";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModAttachments.init();
        ModSoundEvents.init();
        UseBoatHandler.init();
    }
}