package net.ririfa.mcefal;

import net.ririfa.beacon.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CefBrowser implements IEventHandler {
    private final List<CefTab<?>> tabs = new ArrayList<>();
    private final BrowserDrawer drawer;
    private CefTab<?> activeTab;

    public CefBrowser() {
        this.drawer = new BrowserDrawer(() -> this);
    }

    public <T extends Category> CefTab<T> createTab(String url, TabDim dim, int frameRate, String name, T category) {
        var tab = new CefTab<>(this, url, dim, frameRate, name, category);
        tabs.add(tab);
        if (activeTab == null) {
            activeTab = tab;
        }
        return tab;
    }

    void removeTab(CefTab<?> tab) {
        tabs.remove(tab);

        if (activeTab == tab) {
            activeTab = tabs.isEmpty() ? null : tabs.getLast();
        }
    }

    public void bringToFront(CefTab<?> tab) {
        if (tabs.contains(tab)) {
            tabs.remove(tab);
            tabs.add(tab);
        }
    }

    public boolean isMouseOverTab(double mouseX, double mouseY) {
        return getHoveredTab(mouseX, mouseY) != null;
    }

    public CefTab<?> getHoveredTab(double mouseX, double mouseY) {
        for (int i = tabs.size() - 1; i >= 0; i--) {
            CefTab<?> tab = tabs.get(i);
            if (isMouseOverTab(tab, mouseX, mouseY)) {
                return tab;
            }
        }
        return null;
    }

    private boolean isMouseOverTab(@NotNull CefTab<?> tab, double mouseX, double mouseY) {
        return mouseX >= tab.dim.x() && mouseX <= tab.dim.x() + tab.dim.width()
                && mouseY >= tab.dim.y() && mouseY <= tab.dim.y() + tab.dim.height();
    }

    public void setActiveTab(CefTab<?> tab) {
        if (tabs.contains(tab)) {
            activeTab = tab;
        } else {
            throw new IllegalArgumentException("Tab not found in browser");
        }
    }

    public CefTab<?> getTabByName(String name) {
        for (CefTab<?> tab : tabs) {
            if (tab.getName().equals(name)) {
                return tab;
            }
        }
        return null;
    }

    public CefTab<?> getActiveTab() {
        return activeTab;
    }

    public List<CefTab<?>> getTabs() {
        return new ArrayList<>(tabs);
    }

    public BrowserDrawer getDrawer() {
        return drawer;
    }

    @Override
    public void initHandlers() {
        drawer.initHandlers();
    }
}