package net.rodepanda.holograms.Projector;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import net.rodepanda.holograms.Holo;
import net.rodepanda.holograms.Util.AABB;
import net.rodepanda.holograms.Util.Ray3D;
import net.rodepanda.holograms.Util.Vec3D;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Screen {

    private Vector minLoc, maxLoc;
    public final double width, height;
    private Page activePage;
    private int entityIdCounter, originalCounterValue;
    public final Player owner;
    public final int updateTime;

    /**
     * Creates a new Screen. Only one screen should be active at a time per player. To destroy the screen just unregister the screen in the main class.
     * @param owner The player who will own the screen.
     * @param page
     * @param minLoc The bottom (or top) left side of the screen.
     * @param maxLoc The top (or bottom) right side of the screen.
     * @param updateTime The amount of ticks it will take for a screen to do an update tick. A higher value will result in a slower button response time.
     */
    public Screen(Player owner, Page page, Vector minLoc, Vector maxLoc, int updateTime) {
//        Vector[] sorted = VectorCalc.orderVectorPoints(minLoc, maxLoc);
//        this.minLoc = sorted[0];
//        this.maxLoc = sorted[1];
        this.minLoc = minLoc;
        this.maxLoc = maxLoc;
        this.updateTime = updateTime;
        Vector size = maxLoc.clone().subtract(minLoc);
        width = new Vector(size.getX(), 0, size.getZ()).length();
        height = size.getY();
        activePage = page;
        this.owner = owner;
        int ec = (Integer) Accessors.getFieldAccessor(MinecraftReflection.getEntityClass(), "entityCount", true).get(null);
        entityIdCounter = ((ec + 2000 > Integer.MAX_VALUE - 1000) && (ec + 2000 < 10)) ? 10 : ec + 2000;
        originalCounterValue = entityIdCounter;
        Holo.registerScreen(owner, this);
        initSlide();

    }

    /**
     * Converts a point on the screen to a Vector in the world.
     * @param x x on a scale from 0.0 to 1.0
     * @param y y on a scale from 0.0 to 1.0
     * @return
     */
    public Vector getPoint(double x, double y) {
        x = (x > 1) ? 1 : x;
        x = (x < 0) ? 0 : x;
        y = (y > 1) ? 1 : y;
        y = (y < 0) ? 0 : y;
        Vector dir = maxLoc.clone().subtract(minLoc);
        Vector horizontal = new Vector(dir.getX(), 0, dir.getZ()).multiply(x);
        Vector vertical = new Vector(0, dir.getY(), 0).multiply(y);
        return minLoc.clone().add(vertical).add(horizontal);
    }

    /**
     *
     * @return The direction a screen is facing.
     */
    public Vector getScreenNormal() {
        Vector dir = maxLoc.clone().subtract(minLoc);
        return new Vector(dir.getX(), 0, dir.getZ()).crossProduct(new Vector(0, 1f, 0).normalize());
    }

    /**
     *
     * @return The horizontal Vector of the screen.
     */
    public Vector getScreenDir(){
        Vector loc =  maxLoc.clone().subtract(minLoc);
        Vector v = new Vector(loc.getX(), 0, loc.getZ());
        return v.normalize();
    }

    /**
     * Internal use. Reserves an available entity Id.
     * @return
     */
    public int getEntityId() {
        return entityIdCounter++;
    }

    /**
     * Internal use.
     */
    public void releaseEnityityIds() {
        entityIdCounter = originalCounterValue;
    }

    /**
     * Sets a new Slide and disables the old one if not yet disabled.
     * @param page
     */
    public void setSlide(Page page) {
        if(activePage != null)
            activePage.destroy();
        activePage = page;
        initSlide();
    }

    private void initSlide() {
        if (Holo.debug) debug();
        activePage.init(this);
        createTimer();
    }

    /**
     * Disables a slide and cancels all internal update timers.
     */
    public void unloadSlide() {
        if (Holo.debug && runnable != null) {
            runnable.cancel();
            runnable = null;
        }
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        if(activePage != null) {
            activePage.destroy();
            activePage = null;
        }
        releaseEnityityIds();
    }

    /**
     * Internal use.
     */
    public void interact(){
        if(activePage != null){
            activePage.interact();
        }
    }

    BukkitRunnable timer;

    private void createTimer() {
        timer = new BukkitRunnable() {

            @Override
            public void run () {
                if(activePage == null)cancel();
                activePage.update();
            }
        };
        timer.runTaskTimer(Holo.get(), updateTime, updateTime);
    }

    BukkitRunnable runnable;

    private void debug(){
        if(runnable != null)
            runnable.cancel();
        AABB box = new AABB(minLoc, maxLoc);
        runnable = new BukkitRunnable() {

            Vec3D min = Vec3D.fromVector(minLoc);
            Vec3D max = Vec3D.fromVector(maxLoc);
            Location lt = new Location(owner.getWorld(), min.x, min.y, min.z), rt = new Location(owner.getWorld(), max.x, max.y, max.z),
                    lb = new Location(owner.getWorld(), min.x, max.y, min.z), rb = new Location(owner.getWorld(), max.x, min.y, max.z);

            @Override
            public void run() {
                Ray3D ray = new Ray3D(owner.getLocation().add(new Vector(0, 1.5, 0)));
                boolean intersects = (box.intersectsRay(ray, 0, 10) == null);
                owner.spawnParticle(Particle.REDSTONE, lt, 0, 0, 0,intersects ? 255 : 0 );
                owner.spawnParticle(Particle.REDSTONE, rt, 0, 0, 0,intersects ? 255 : 0 );
                owner.spawnParticle(Particle.REDSTONE, lb, 0, 0, 0,intersects ? 255 : 0 );
                owner.spawnParticle(Particle.REDSTONE, rb, 0, 0, 0,intersects ? 255 : 0 );
            }
        };
        runnable.runTaskTimer(Holo.get(), 1, 5);
    }

}
