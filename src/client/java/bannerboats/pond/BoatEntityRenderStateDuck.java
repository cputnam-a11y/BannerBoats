package bannerboats.pond;

import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.util.DyeColor;

public interface BoatEntityRenderStateDuck {
    DyeColor bannerBoats$bannerColor();

    BannerPatternsComponent bannerBoats$patterns();

    void bannerBoats$setBannerColor(DyeColor color);

    void bannerBoats$setPatterns(BannerPatternsComponent patterns);
}
