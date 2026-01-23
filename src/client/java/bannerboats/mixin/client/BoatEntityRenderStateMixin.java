package bannerboats.mixin.client;

import bannerboats.pond.BoatEntityRenderStateDuck;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatRenderState.class)
public class BoatEntityRenderStateMixin implements BoatEntityRenderStateDuck {
    @Unique
    private DyeColor bannerBoats$bannerColor;

    @Unique
    private BannerPatternLayers bannerBoats$patterns;

    @Override
    public DyeColor bannerBoats$bannerColor() {
        return bannerBoats$bannerColor;
    }

    @Override
    public BannerPatternLayers bannerBoats$patterns() {
        return bannerBoats$patterns;
    }

    @Override
    public void bannerBoats$setBannerColor(DyeColor color) {
        this.bannerBoats$bannerColor = color;
    }

    @Override
    public void bannerBoats$setPatterns(BannerPatternLayers patterns) {
        this.bannerBoats$patterns = patterns;
    }
}
