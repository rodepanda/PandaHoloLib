package net.rodepanda.holograms.Components.GuiComponents;

import net.rodepanda.holograms.Components.EntityComponents.EntityComponent;
import net.rodepanda.holograms.Components.EntityComponents.EntityItemComponent;
import net.rodepanda.holograms.PlaceHolders.PlaceHolderHandler;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A component which displays an item and possibly text on top.
 */
public class GuiItemComponent extends GuiComponent {

    private EntityComponent ec;
    private ItemStack item;
    public final boolean fluidTitle;
    public final int updateTime;
    private int updateCounter = 0;

    /**
     *
     * @param x
     * @param y
     * @param nameVisible
     * @param name
     * @param item
     * @param updateTime
     */
    public GuiItemComponent(double x, double y, boolean nameVisible, String name, ItemStack item, int updateTime) {
        super(x, y, nameVisible, name);
        this.item = item;
        this.updateTime = updateTime;
        fluidTitle = (updateTime > 0 && nameVisible);
    }

    public GuiItemComponent(double x, double y, boolean nameVisible, String name, Material item, int updateTime) {
        this(x,y,nameVisible,name,new ItemStack(item), updateTime);
    }

    @Override
    public void init(Screen s) {
        this.s = s;
        name = PlaceHolderHandler.parse(originalTitle, s.owner);
        ec = new EntityItemComponent(s.getEntityId(), s, name, nameVisible, item);
        ec.init(s.getPoint(x, y));
    }

    @Override
    public void update() {
        if(!fluidTitle)return;
        if(updateCounter >= updateTime){
            updateCounter = 0;
            String newTitle = PlaceHolderHandler.parse(originalTitle, s.owner);
            if(!name.equals(newTitle)) {
                name = newTitle;
                ec.updateTitle(newTitle);
            }
        }
        updateCounter += s.updateTime;
    }

    @Override
    public void destroy(Page p) {
        ec.destroy(p);
    }

}
