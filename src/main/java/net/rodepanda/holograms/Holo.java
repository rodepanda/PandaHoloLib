package net.rodepanda.holograms;

import net.rodepanda.holograms.Listeners.PlayerActionListener;
import net.rodepanda.holograms.PlaceHolders.PlaceHolderHandler;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Holo extends JavaPlugin {

    private static Plugin plugin;

    public static final boolean debug = false;

    private static HashMap<Player, Screen> screenDatabase = new HashMap<>();

    @Override
    public void onEnable(){
        plugin = this;
        PlaceHolderHandler.init();
        getServer().getPluginManager().registerEvents(new PlayerActionListener(), this);
//        getCommand("h").setExecutor(new Debug());
    }

    public static Plugin get(){
        return plugin;
    }

    /**
     * Get's the loaded screen of a player. Returns null if none is active.
     * @param p
     * @return
     */
    public static Screen getPlayerScreen(Player p){
        if(!screenDatabase.containsKey(p))return null;
        return screenDatabase.get(p);
    }

    /**
     * Internal Method do not Use
     * @param p
     * @param s
     */
    public static void registerScreen(Player p, Screen s){
        if(screenDatabase.containsKey(p))screenDatabase.get(p).unloadSlide();
        screenDatabase.put(p, s);
    }

    /**
     * Disables a screen and removes it from the database.
     * @param p
     */
    public static void unRegisterScreen(Player p){
        if(screenDatabase.containsKey(p))screenDatabase.get(p).unloadSlide();
        screenDatabase.remove(p);
    }


}
