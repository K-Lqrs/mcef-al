package net.ririfa.mcefal;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.ririfa.beacon.IEventHandler;
import net.ririfa.beacon.javaExtension.HandlerUtil;
import net.ririfa.mcefal.events.FrameBufferResizeEvent;
import net.ririfa.mcefal.events.ScreenRenderEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.ccbluex.liquidbounce.mcef.MCEF.mc;

public class BrowserDrawer implements IEventHandler {
    public final Supplier<CefBrowser> browser;
    private final List<CefTab<?>> tabs;

    private final RenderPhase.Transparency BROWSER_TRANSPARENCY = new RenderPhase.Transparency(
            "browser_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            },
            () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            }
    );

    public final Function<Identifier, RenderLayer> BROWSER_TEXTURE_LAYER = Util.memoize(texture ->
            RenderLayer.of(
                    "browser_textured",
                    VertexFormats.POSITION_TEXTURE_COLOR,
                    VertexFormat.DrawMode.QUADS,
                    786432,
                    RenderLayer.MultiPhaseParameters.builder()
                            .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
                            .program(RenderPhase.POSITION_TEXTURE_COLOR_PROGRAM)
                            .transparency(BROWSER_TRANSPARENCY)
                            .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                            .target(RenderPhase.MAIN_TARGET)
                            .build(false)
            )
    );

    public BrowserDrawer(
            @NotNull Supplier<CefBrowser> browser
    ) {
        this.browser = browser;
        tabs = browser.get().getTabs();
    }

    public void initHandlers() {
        HandlerUtil.handler(
                this,
                ScreenRenderEvent.class,
                event -> {
                    for (CefTab<?> tab : tabs) {
                        var scaleFactor = (float) mc.getWindow().getScaleFactor();
                        var x = tab.dim.x() / scaleFactor;
                        var y = tab.dim.y() / scaleFactor;
                        var width = tab.dim.width() / scaleFactor;
                        var height = tab.dim.height() / scaleFactor;

                        renderTexture(
                                event.context,
                                tab.getTexture(),
                                x,
                                y,
                                width,
                                height
                        );
                    }
                }
        );
        HandlerUtil.handler(
                this,
                FrameBufferResizeEvent.class,
                event -> {
                    for (CefTab<?> tab : tabs) {
                        tab.resize(event.width, event.height);
                    }
                }
        );
    }

    private void renderTexture(
            @NotNull DrawContext context,
            Identifier texture,
            float x,
            float y,
            float width,
            float height
    ) {
        context.drawTexture(
                BROWSER_TEXTURE_LAYER,
                texture,
                Math.round(x),
                Math.round(y),
                0.0F,
                0.0F,
                Math.round(width),
                Math.round(height),
                Math.round(width),
                Math.round(height)
        );
    }

    public CefBrowser getBrowser() {
        return browser.get();
    }
}
