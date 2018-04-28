package net.rodepanda.holograms.Components.GuiComponents;

import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Projector.Screen;

public abstract class GuiComponent {

    protected double x, y;
    protected boolean nameVisible;
    protected String name;
    public final String originalTitle;
    protected Screen s;

    public GuiComponent(double x, double y, boolean nameVisible, String name){
        this.x = x;
        this.y = y;
        this.nameVisible = nameVisible;
        this.originalTitle = name;
    }

    public GuiComponent(double x, double y){
        this(x, y, false, null);
    }

    public abstract void init(Screen s);
    public abstract void update();
    public abstract void destroy(Page p);

}
