package net.rodepanda.holograms.PlaceHolders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import net.rodepanda.holograms.Holo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlaceHolderHandler {

    private static boolean mvdwEnabled = false;
    private static boolean phApiEnabled = false;

    public static void init(){
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            phApiEnabled = true;
            Holo.get().getLogger().log(Level.INFO, "Enabling PlaceholderAPI support!");
        }
        if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            mvdwEnabled = true;
            Holo.get().getLogger().log(Level.INFO, "Enabling MVdWPlaceholderAPI support!");
        }

    }

    public static String parse(String query, Player p){
        if(mvdwEnabled)
            query = PlaceholderAPI.replacePlaceholders(p, query);
        if(phApiEnabled)
            query = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, query);
        return query;
    }



}
