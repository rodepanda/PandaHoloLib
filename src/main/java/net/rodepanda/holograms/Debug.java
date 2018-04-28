package net.rodepanda.holograms;

import net.rodepanda.holograms.Components.GuiComponents.GuiButtonItemComponent;
import net.rodepanda.holograms.Components.Helpers.ButtonClick;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Util.AABB;
import net.rodepanda.holograms.Util.Ray3D;
import net.rodepanda.holograms.Util.Vec3D;
import net.rodepanda.holograms.Util.VectorCalc;
import net.rodepanda.holograms.Components.GuiComponents.GuiItemComponent;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Debug implements CommandExecutor{

    public Location p1, p2, old;
    private AABB box;
    BukkitRunnable runnable;
    Screen h;

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length == 0)
            return false;
        Player p = (Player)commandSender;
        if(strings[0].equals("stop")){
            if(runnable != null){
                runnable.cancel();
                runnable = null;
            }
        }
        if(strings[0].equals("p1")){
            p1 = new Location(p.getWorld(), Double.parseDouble(strings[1]),Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            return true;
        }
        if(strings[0].equals("p2")){
            p2 = new Location(p.getWorld(), Double.parseDouble(strings[1]),Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            return true;
        }
        if(strings[0].equals("holo")){
            Page page = new Page();
//            page.addComponent(new GuiTitleComponent(0.25, 0.3, "%player_x% Test", 20));
            ButtonClick bc = new ButtonClick(){

                @Override
                public void run(Player p) {
                    Screen s = Holo.getPlayerScreen(p);
                    s.unloadSlide();
                    s.setSlide(newPage());
                }
            };
            page.addComponent(new GuiButtonItemComponent(0.5, 0.5, true,"%player_z% Test", 0.3, Material.REDSTONE, pl -> p.sendMessage("CLICK"), 20, true ));
//            page.addComponent(new GuiItemComponent(0.75, 0.5, true,"%player_time% Test2", Material.SEA_LANTERN, 20));
            h = new Screen(p, page, p1.toVector(), p2.toVector(), 2);
            return true;
        }
        if(strings[0].equals("destroy")){
            h.unloadSlide();
            return true;
        }
        if(strings[0].equals("test")){
            if(runnable != null){
                runnable.cancel();
                runnable = null;
            }
            Vector[] vc = VectorCalc.orderVectorPoints(p1.toVector(), p2.toVector());
            box = new AABB(vc[0], vc[1]);
            runnable = new BukkitRunnable() {

                Vec3D min = box.getMin();
                Vec3D max = box.getMax();
                Location lt = new Location(p.getWorld(), max.x, max.y, max.z), rt = new Location(p.getWorld(), max.x, max.y, min.z),
                        lb = new Location(p.getWorld(), max.x, min.y, max.z), rb = new Location(p.getWorld(), min.x, max.y, max.z),
                        lt2 = new Location(p.getWorld(), min.x, min.y, max.z), rt2 = new Location(p.getWorld(), min.x, max.y, min.z),
                        lb2 = new Location(p.getWorld(), max.x, min.y, min.z), rb2 = new Location(p.getWorld(), min.x, min.y, min.z);


                @Override
                public void run() {
                    Ray3D ray = new Ray3D(p.getLocation().add(new Vector(0, 1.5, 0)));
                    boolean intersects = (box.intersectsRay(ray, 0, 10) == null);
                    p.spawnParticle(Particle.REDSTONE, lt, 0, 0, 0,intersects ? 255 : 0 );
                    p.spawnParticle(Particle.REDSTONE, rt, 0, 0, 0,intersects ? 255 : 0 );
                    p.spawnParticle(Particle.REDSTONE, lb, 0, 0, 0,intersects ? 255 : 0 );
                    p.spawnParticle(Particle.REDSTONE, rb, 0, 0, 0,intersects ? 255 : 0 );
                    p.spawnParticle(Particle.REDSTONE, lt2, 0,0,0, intersects ? 255 : 0);
                    p.spawnParticle(Particle.REDSTONE, rt2, 0,0,0, intersects ? 255 : 0);
                    p.spawnParticle(Particle.REDSTONE, lb2, 0,0,0, intersects ? 255 : 0);
                    p.spawnParticle(Particle.REDSTONE, rb2, 0,0,0, intersects ? 255 : 0);
//                    p.sendMessage(intersects + "");
//                    for(int i = 0; i < 10; i+=2) {
//                        Location loc = new Location(p.getWorld(), ray.x + ray.dir.x * i, ray.y + ray.dir.y * i, ray.z + ray.dir.z * i);
//                        p.spawnParticle(Particle.REDSTONE, loc, 0, 255, 255, 0);
//                    }
                }
            };
            runnable.runTaskTimer(Holo.get(), 1, 1);
        }
        return  true;
    }

    static Page newPage(){
        Page p = new Page();
        p.addComponent(new GuiItemComponent(0.5, 0.5, true, "Hi", Material.CARROT_ITEM, 0));
        return p;
    }

}
