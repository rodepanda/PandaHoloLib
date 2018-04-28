package net.rodepanda.holograms.Util;

import org.bukkit.Location;


public class Ray3D extends Vec3D {
    public final Vec3D dir;

    public Ray3D(Vec3D origin, Vec3D direction) {
        super(origin);
        dir = direction.normalize();
    }

    /**
     * Construct a 3D ray from a location.
     * @param loc - the Bukkit location.
     */
    public Ray3D(Location loc) {
        this(fromLocation(loc), fromVector(loc.getDirection()));
    }

    public Vec3D getDirection() {
        return dir;
    }

    public Vec3D getPointAtDistance(double dist) {
        return add(dir.scale(dist));
    }

    public String toString() {
        return "origin: " + super.toString() + " dir: " + dir;
    }
}
