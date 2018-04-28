package net.rodepanda.holograms.Util;

import org.bukkit.util.Vector;

public class VectorCalc {

    public static float vectorToYaw(Vector vec){
        double dx = vec.normalize().getX();
        double dz = vec.normalize().getZ();
        double yaw = 0;
        if (dx != 0) {
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        // I don't know why it works, but it works so don't EVER touch this!
        return (float) ((-yaw * 180 / Math.PI - 90)) * 360.F / 256.0F / 2.0F + 61;
    }

    /**
     * Sorts two vectors and returns an array with 0 being the smallest vector and 1 being the biggest.
     * @param minLoc
     * @param maxLoc
     * @return
     */
    public static Vector[] orderVectorPoints(Vector minLoc, Vector maxLoc){
        double minX, maxX, minY,maxY,minZ,maxZ;
        if(minLoc.getX() < maxLoc.getX()){
            minX = minLoc.getX();
            maxX = maxLoc.getX();
        }else {
            minX = maxLoc.getX();
            maxX = minLoc.getX();
        }
        if(minLoc.getY() < maxLoc.getY()){
            minY = minLoc.getY();
            maxY = maxLoc.getY();
        }else {
            minY = maxLoc.getY();
            maxY = minLoc.getY();
        }
        if(minLoc.getZ() < maxLoc.getZ()){
            minZ = minLoc.getZ();
            maxZ = maxLoc.getZ();
        }else {
            minZ = maxLoc.getZ();
            maxZ = minLoc.getZ();
        }
        return new Vector[]{new Vector(minX, minY, minZ), new Vector(maxX, maxY,maxZ)};
    }

}
