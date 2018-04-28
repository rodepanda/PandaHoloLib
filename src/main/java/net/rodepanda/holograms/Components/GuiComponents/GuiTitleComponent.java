package net.rodepanda.holograms.Components.GuiComponents;

import net.rodepanda.holograms.Components.EntityComponents.EntityTitleComponent;
import net.rodepanda.holograms.PlaceHolders.PlaceHolderHandler;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Projector.Screen;

/**
 * A component which displays a line of text.
 */
public class GuiTitleComponent extends GuiComponent {

    private Screen screen;
    EntityTitleComponent tc;
    public final boolean fluidTitle;
    public final int updateTime;
    private int updateCounter = 0;

    /**
     * Creates a new one line title.
     * @param x Screen x
     * @param y Screen y
     * @param title The text to display.
     * @param updateTime The time in ticks it takes to update the placeholders. Set to 0 for static text.
     */
    public GuiTitleComponent(double x, double y, String title, int updateTime) {
        super(x, y, true, title);
        this.updateTime = updateTime;
        fluidTitle = (updateTime > 0 && nameVisible);
    }
    public GuiTitleComponent(double x, double y, String title) {
        this(x, y, title, 0);
    }

    @Override
    public void init(Screen s) {
        screen = s;
        this.s = screen;
        name = PlaceHolderHandler.parse(originalTitle, s.owner);
        tc = new EntityTitleComponent(screen.getEntityId(), screen, name);
        tc.init(s.getPoint(x,y));
    }

    @Override
    public void update() {
        if(originalTitle == null || !fluidTitle)return;
        if(updateCounter >= updateTime) {
            updateCounter = 0;
            String newTitle = PlaceHolderHandler.parse(originalTitle, s.owner);
            if(!name.equals(newTitle)) {
                name = newTitle;
                tc.updateTitle(newTitle);
            }
        }
        updateCounter += s.updateTime;
    }

    @Override
    public void destroy(Page p) {
        tc.destroy(p);
    }
}
