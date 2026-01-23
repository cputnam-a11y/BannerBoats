package bannerboats.pond;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public interface BoatEntityRenderStateDuck {
    DyeColor bannerBoats$bannerColor();

    BannerPatternLayers bannerBoats$patterns();

    void bannerBoats$setBannerColor(DyeColor color);

    void bannerBoats$setPatterns(BannerPatternLayers patterns);
}
