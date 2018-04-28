package net.rodepanda.holograms.Components.GuiComponents;

import net.rodepanda.holograms.PlaceHolders.PlaceHolderHandler;
import net.rodepanda.holograms.Components.EntityComponents.EntityTitleComponent;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * A text component is a component made up of multiple lines.
 */
public class GuiTextComponent extends GuiComponent {

    HashMap<EntityTitleComponent, String> cedb = new HashMap<>();
    public final boolean fluidTitle;
    public final int updateTime;
    private int updateCounter = 0;

    /**
     * Creates a component made of multiple text lines. You can split a line with "\n"
     * @param x Screen x
     * @param y Screen y
     * @param name The title the component will have.
     * @param updateTime The time in ticks it will take for the title to update the placeholders. Set to 0 to disable.
     */
    public GuiTextComponent(double x, double y, String name, int updateTime) {
        super(x, y, true, name);
        this.name = name;
        this.updateTime = updateTime;
        fluidTitle = (updateTime > 0 && nameVisible);
    }

    @Override
    public void init(Screen s) {
        if(name.isEmpty())return;
        String[] text = name.split("\n");
        double counter = s.getPoint(x,y).getY()-0.6;
        for(String line : text){
            EntityTitleComponent ec = new EntityTitleComponent(s.getEntityId(), s, PlaceHolderHandler.parse(line, s.owner));
            cedb.put(ec, line);
            Vector v = s.getPoint(x,y);
            ec.init(new Vector(v.getX(), counter, v.getZ()));
            counter -= .25;
        }
    }

    @Override
    public void update() {
        if(!fluidTitle)return;
        if(updateCounter >= updateTime) {
            updateCounter = 0;
            for(EntityTitleComponent c : cedb.keySet()) {
                String newTitle = PlaceHolderHandler.parse(cedb.get(c), s.owner);
                if (c.getTitle() != newTitle) {
                    c.updateTitle(newTitle);
                }
            }
        }
        updateCounter += s.updateTime;
    }

    @Override
    public void destroy(Page p) {
        for(EntityTitleComponent c : cedb.keySet())
            c.destroy(p);
    }
}
