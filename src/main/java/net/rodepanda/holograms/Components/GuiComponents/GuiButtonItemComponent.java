package net.rodepanda.holograms.Components.GuiComponents;

import net.rodepanda.holograms.Components.EntityComponents.EntityItemComponent;
import net.rodepanda.holograms.Components.Helpers.ButtonClick;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A component with an interactive button players can click on.
 */
public class GuiButtonItemComponent extends GuiButtonComponent {

    public final ItemStack material;

    /**
     * Creates a new Button component players can click on.
     * @param x Screen x
     * @param y Screen y
     * @param nameVisible Is the title visible.
     * @param name The title the component will have.
     * @param width The width of the hitbox in Minecraft units.
     * @param height The height of the hitbox in Minecraft units.
     * @param item The displayed item.
     * @param action The action that will be executed on click.
     * @param updateTime The time in ticks it will take for the title to update the placeholders. Set to 0 to disable.
     * @param glowOnSelect Gives the item a glowing effect when a player hovers his cursor over the item.
     */
    public GuiButtonItemComponent(double x, double y, boolean nameVisible, String name, double width, double height, ItemStack item, ButtonClick action, int updateTime, boolean glowOnSelect) {
        super(x, y, nameVisible, name, width, height, action, updateTime, glowOnSelect);
        material = item;
    }

    /**
     *
     * @param boxRadius The radius of the hitbox. So with 0.5 the width will be 1.
     */
    public GuiButtonItemComponent(double x, double y, boolean nameVisible, String name, double boxRadius, Material mat, ButtonClick action, int updateTime, boolean glowOnSelect) {
        this(x,y, nameVisible, name, boxRadius*2, boxRadius * 2, new ItemStack(mat), action, updateTime, glowOnSelect);
    }
    public GuiButtonItemComponent(double x, double y, boolean nameVisible, String name, double boxRadius, Material mat, ButtonClick action) {
        this(x, y, nameVisible, name, boxRadius, mat,action, 0, false);

    }
    public GuiButtonItemComponent(double x, double y, boolean nameVisible, String name, double boxRadius, Material mat) {
        this(x, y, nameVisible, name, boxRadius, mat,null);
    }

    @Override
    public void init(Screen s) {
        super.init(s);
        ec = new EntityItemComponent(s.getEntityId(), s, name, nameVisible, material);
        ec.init(s.getPoint(x,y));
    }
}
