package net.ririfa.mcefal;

import net.ccbluex.liquidbounce.mcef.MCEF;
import net.ccbluex.liquidbounce.mcef.MCEFBrowser;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.ccbluex.liquidbounce.mcef.MCEF.mc;

@SuppressWarnings("unused")
public class CefTab<T extends Category> {
    private final MCEFBrowser mcefBrowser;
    final CefBrowser browser;
    TabDim dim;
    final String name;
    private final T category;

    private final Identifier texture;

    public CefTab(
            CefBrowser browser,
            String url,
            @NotNull TabDim dim,
            int frameRate,
            String name,
            T category
    ) {
        this.name = name;
        this.browser = browser;
        this.dim = dim;
        this.category = category;
        this.mcefBrowser = MCEF.INSTANCE.createBrowser(
                url,
                true,
                dim.width(),
                dim.height(),
                frameRate
        );

        mcefBrowser.setZoomLevel(1.0);

        this.texture = Identifier.of("mcefal", "browser/tab/" + UUID.randomUUID());

        TextureManager textureManager = mc.getTextureManager();
        textureManager.registerTexture(texture, new AbstractTexture() {
            @Override
            public int getGlId() {
                return mcefBrowser.getRenderer().getTextureID();
            }
        });
    }

    public void closeTab() {
        mcefBrowser.close();
        browser.removeTab(this);
        mc.getTextureManager().destroyTexture(texture);
    }

    public void setPosition(@NotNull TabDim dim) {
        this.dim = dim;
        mcefBrowser.resize(
                Math.max(dim.width(), 1),
                Math.max(dim.height(), 1)
        );
    }

    //region Browser
    public void forceReload() {
        mcefBrowser.reloadIgnoreCache();
    }

    public void reload() {
        mcefBrowser.reload();
    }

    public void goForward() {
        mcefBrowser.goForward();
    }

    public void goBack() {
        mcefBrowser.goBack();
    }

    public void loadUrl(String url) {
        mcefBrowser.loadURL(url);
    }

    public void resize(int width, int height) {
        if (!dim.fullscreen()) {
            return;
        }

        dim = new TabDim(
                dim.x(),
                dim.y(),
                width,
                height,
                true
        );
    }
    //endregion

    //region Input
    public void mouseClicked(double x, double y, int button) {
        browser.setActiveTab(this);
        mcefBrowser.setFocus(true);
        mcefBrowser.sendMousePress((int) x, (int) y, button);
    }

    public void mouseReleased(double x, double y, int button) {
        mcefBrowser.setFocus(true);
        mcefBrowser.sendMouseRelease((int) x, (int) y, button);
    }

    public void mouseMoved(double x, double y) {
        mcefBrowser.sendMouseMove((int) x, (int) y);
    }

    public void mouseScrolled(double x, double y, double amount) {
        mcefBrowser.sendMouseWheel((int) x, (int) y, (int) amount);
    }

    public void keyPressed(int key, int scancode, int mods) {
        mcefBrowser.setFocus(true);
        mcefBrowser.sendKeyPress(key, scancode, mods);
    }

    public void keyReleased(int key, int scancode, int mods) {
        mcefBrowser.setFocus(true);
        mcefBrowser.sendKeyRelease(key, scancode, mods);
    }

    public void charTyped(char character, int mods) {
        mcefBrowser.setFocus(true);
        mcefBrowser.sendKeyTyped(character, mods);
    }
    //endregion

    public String getName() {
        return name;
    }

    public TabDim getDim() {
        return dim;
    }

    public String getUrl() {
        return mcefBrowser.getURL();
    }

    public Identifier getTexture() {
        return texture;
    }

    public T getCategory() {
        return category;
    }
}
