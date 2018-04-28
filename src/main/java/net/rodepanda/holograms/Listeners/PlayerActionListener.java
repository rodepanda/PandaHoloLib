package net.rodepanda.holograms.Listeners;

import net.rodepanda.holograms.Holo;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerActionListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK)return;
        Screen s = Holo.getPlayerScreen(e.getPlayer());
        if(s != null)s.interact();
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e){
        Holo.unRegisterScreen(e.getPlayer());
    }

}
