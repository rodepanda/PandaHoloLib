package net.rodepanda.holograms.Components.GuiComponents;

import com.comphenix.protocol.utility.MinecraftVersion;
import net.rodepanda.holograms.Components.EntityComponents.EntityItemComponent;
import net.rodepanda.holograms.Components.Helpers.ButtonClick;
import net.rodepanda.holograms.PlaceHolders.PlaceHolderHandler;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Util.Ray3D;
import net.rodepanda.holograms.Components.EntityComponents.EntityComponent;
import net.rodepanda.holograms.Holo;
import net.rodepanda.holograms.Projector.Screen;
import net.rodepanda.holograms.Util.AABB;
import net.rodepanda.holograms.Util.Vec3D;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public abstract class GuiButtonComponent extends GuiComponent {

    public final boolean glowOnSelect;
    public final boolean fluidTitle;
    public final int updateTime;
    public final double boxWidth, boxHeight;


    EntityComponent ec;
    AABB box;
    private boolean active;
    private ButtonClick action;
    private int updateCounter = 0;

    public GuiButtonComponent(double x, double y, boolean nameVisible, String name, double boxWidth, double boxHeight, ButtonClick action,int updateTime, boolean glowOnSelect) {
        super(x, y, nameVisible, name);
        this.action = action;
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;
        this.updateTime = updateTime;
        fluidTitle = (updateTime > 0 && nameVisible);
        this.glowOnSelect = (MinecraftVersion.getCurrentVersion().isAtLeast(MinecraftVersion.COMBAT_UPDATE)) && glowOnSelect;
    }

    public GuiButtonComponent(double x, double y, boolean nameVisible, String name, double boxRadius, ButtonClick action, int updateTime, boolean glowOnSelect) {
        this(x ,y, nameVisible, name, boxRadius*2, boxRadius*2, action, updateTime, glowOnSelect);
    }

    public GuiButtonComponent(double x, double y, boolean nameVisible, String name, double boxRadius, ButtonClick action) {
        this(x ,y, nameVisible, name, boxRadius, action,0, false);
    }

    public GuiButtonComponent(double x, double y, boolean nameVisible, String name, double boxRadius) {
        this(x ,y, nameVisible, name, boxRadius, null);
    }


    public void interact(){
        if(!active)return;
        if(action != null) {
            action.run(s.owner);
        }
    }

    @Override
    public void init(Screen s) {
        this.s = s;
        name = PlaceHolderHandler.parse(originalTitle, s.owner);
        Vector startLoc = s.getPoint(x, y).add(new Vector(0, 0.0, 0));
        Vector dir = s.getScreenDir();
        Vector minLoc = startLoc.clone().subtract(dir.clone().multiply(boxWidth/2)).subtract(new Vector(0, boxHeight/2, 0));
        Vector maxLoc = startLoc.clone().add(dir.clone().multiply(boxWidth/2)).add(new Vector(0, boxHeight/2, 0));
        box = new AABB(minLoc, maxLoc);
    }

    @Override
    public void update() {
        Ray3D ray = new Ray3D(s.owner.getLocation().add(new Vector(0, 1.6, 0)));
        boolean intersects = (box.intersectsRay(ray, 0, 8) != null);
        if(!active && intersects){
            if(glowOnSelect && ec instanceof EntityItemComponent)
                ((EntityItemComponent)ec).setGlowing(true);
            active = true;
            ec.updateLocation(ec.getLoc().clone().add(s.getScreenNormal().normalize().multiply(.5)));
        } else if(active && ! intersects){
            if(glowOnSelect && ec instanceof EntityItemComponent)
                ((EntityItemComponent)ec).setGlowing(false);
            active = false;
            ec.updateLocation(ec.getLoc());
        }
        if(fluidTitle) {
            if (updateCounter >= updateTime) {
                updateCounter = 0;
                String newTitle = PlaceHolderHandler.parse(originalTitle, s.owner);
                if (!name.equals(newTitle)) {
                    name = newTitle;
                    ec.updateTitle(newTitle);
                }
            }
            updateCounter += s.updateTime;
        }
        if(Holo.debug){
            debug();
        }
    }

    @Override
    public void destroy(Page p) {
        ec.destroy(p);
    }

    void debug(){
        Vec3D min = box.getMin();
        Vec3D max = box.getMax();
        Location lt = new Location(s.owner.getWorld(), max.x, max.y, max.z), rt  = new Location(s.owner.getWorld(), max.x, max.y, min.z),
                lb  = new Location(s.owner.getWorld(), max.x, min.y, max.z), rb  = new Location(s.owner.getWorld(), min.x, max.y, max.z),
                lt2 = new Location(s.owner.getWorld(), min.x, min.y, max.z), rt2 = new Location(s.owner.getWorld(), min.x, max.y, min.z),
                lb2 = new Location(s.owner.getWorld(), max.x, min.y, min.z), rb2 = new Location(s.owner.getWorld(), min.x, min.y, min.z);
        s.owner.spawnParticle(Particle.REDSTONE, lt , 0, 0, 0, active ? 255 : 0 );
        s.owner.spawnParticle(Particle.REDSTONE, rt , 0, 0, 0, active ? 255 : 0 );
        s.owner.spawnParticle(Particle.REDSTONE, lb , 0, 0, 0, active ? 255 : 0 );
        s.owner.spawnParticle(Particle.REDSTONE, rb , 0, 0, 0, active ? 255 : 0 );
        s.owner.spawnParticle(Particle.REDSTONE, lt2, 0, 0, 0, active ? 255 : 0);
        s.owner.spawnParticle(Particle.REDSTONE, rt2, 0, 0, 0, active ? 255 : 0);
        s.owner.spawnParticle(Particle.REDSTONE, lb2, 0, 0, 0, active ? 255 : 0);
        s.owner.spawnParticle(Particle.REDSTONE, rb2, 0, 0, 0, active ? 255 : 0);
    }
}
