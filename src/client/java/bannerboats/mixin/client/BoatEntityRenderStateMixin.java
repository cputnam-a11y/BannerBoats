package bannerboats.mixin.client;

import bannerboats.pond.BoatEntityRenderStateDuck;
import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatEntityRenderState.class)
public class BoatEntityRenderStateMixin implements BoatEntityRenderStateDuck {
    @Unique
    private DyeColor bannerBoats$bannerColor;

    @Unique
    private BannerPatternsComponent bannerBoats$patterns;

    @Override
    public DyeColor bannerBoats$bannerColor() {
        return bannerBoats$bannerColor;
    }

    @Override
    public BannerPatternsComponent bannerBoats$patterns() {
        return bannerBoats$patterns;
    }

    @Override
    public void bannerBoats$setBannerColor(DyeColor color) {
        this.bannerBoats$bannerColor = color;
    }

    @Override
    public void bannerBoats$setPatterns(BannerPatternsComponent patterns) {
        this.bannerBoats$patterns = patterns;
    }
}
