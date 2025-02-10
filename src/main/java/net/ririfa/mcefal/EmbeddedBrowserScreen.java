package net.ririfa.mcefal;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * {@code EmbeddedBrowserScreen} is an abstract class that provides a browser-based screen
 * using MCEF (Minecraft Chromium Embedded Framework). This class allows for managing multiple
 * browser tabs within a Minecraft GUI.
 * <p>
 * Extend this class to implement a custom web-based GUI.
 */
public abstract class EmbeddedBrowserScreen extends Screen {
    protected final CefBrowser browser;
    protected final BrowserDrawer drawer;

    /**
     * Constructs an {@code EmbeddedBrowserScreen} with the given title.
     *
     * @param title The title of the screen.
     */
    public EmbeddedBrowserScreen(@NotNull String title) {
        super(Text.of(title));
        this.browser = new CefBrowser();
        this.drawer = browser.getDrawer();
    }

    /**
     * Initializes the browser tabs.
     * This method should be overridden to define the tabs that will be created in the screen.
     */
    public abstract void initTab();

    /**
     * Handles mouse click events and forwards them to the active browser tab if applicable.
     *
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     * @param button The mouse button that was clicked.
     * @return {@code true} if the event was handled, otherwise calls the superclass method.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CefTab<?> clickedTab = browser.getHoveredTab(mouseX, mouseY);
        if (clickedTab != null) {
            browser.setActiveTab(clickedTab);
            browser.bringToFront(clickedTab);
            clickedTab.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Handles mouse release events and forwards them to the active browser tab if applicable.
     *
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     * @param button The mouse button that was released.
     * @return {@code true} if the event was handled, otherwise calls the superclass method.
     */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        CefTab<?> activeTab = browser.getActiveTab();
        if (activeTab != null) {
            activeTab.mouseReleased(mouseX, mouseY, button);
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * Handles mouse scroll events and forwards them to the hovered browser tab if applicable.
     *
     * @param mouseX  The X-coordinate of the mouse.
     * @param mouseY  The Y-coordinate of the mouse.
     * @param amount  The scroll amount.
     * @param deltaX  The horizontal scroll delta.
     * @return {@code true} if the event was handled, otherwise calls the superclass method.
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount, double deltaX) {
        CefTab<?> hoveredTab = browser.getHoveredTab(mouseX, mouseY);
        if (hoveredTab != null) {
            hoveredTab.mouseScrolled(mouseX, mouseY, amount);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount, deltaX);
    }

    /**
     * Determines if the mouse is currently over a browser tab.
     *
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     * @return {@code true} if the mouse is over a tab, otherwise {@code false}.
     */
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return browser.isMouseOverTab(mouseX, mouseY);
    }

    /**
     * Handles mouse movement events and forwards them to the hovered browser tab if applicable.
     *
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     */
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        CefTab<?> hoveredTab = browser.getHoveredTab(mouseX, mouseY);
        if (hoveredTab != null) {
            hoveredTab.mouseMoved(mouseX, mouseY);
        }
    }

    /**
     * Handles keyboard key press events and forwards them to the active browser tab if applicable.
     *
     * @param key       The key code of the pressed key.
     * @param scancode  The scancode of the key.
     * @param modifiers The modifier flags.
     * @return {@code true} if the event was handled, otherwise calls the superclass method.
     */
    @Override
    public boolean keyPressed(int key, int scancode, int modifiers) {
        CefTab<?> activeTab = browser.getActiveTab();
        if (activeTab != null) {
            activeTab.keyPressed(key, scancode, modifiers);
            return true;
        }
        return super.keyPressed(key, scancode, modifiers);
    }

    /**
     * Handles keyboard key release events and forwards them to the active browser tab if applicable.
     *
     * @param key       The key code of the released key.
     * @param scancode  The scancode of the key.
     * @param modifiers The modifier flags.
     * @return {@code true} if the event was handled, otherwise calls the superclass method.
     */
    @Override
    public boolean keyReleased(int key, int scancode, int modifiers) {
        CefTab<?> activeTab = browser.getActiveTab();
        if (activeTab != null) {
            activeTab.keyReleased(key, scancode, modifiers);
            return true;
        }
        return super.keyReleased(key, scancode, modifiers);
    }

    /**
     * Handles character input events and forwards them to the active browser tab if applicable.
     *
     * @param chr       The character typed.
     * @param modifiers The modifier flags.
     * @return {@code true} if the event was handled, otherwise calls the superclass method.
     */
    @Override
    public boolean charTyped(char chr, int modifiers) {
        CefTab<?> activeTab = browser.getActiveTab();
        if (activeTab != null) {
            activeTab.charTyped(chr, modifiers);
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    /**
     * Called when the screen is closed.
     */
    @Override
    public void close() {
        super.close();
    }
}
