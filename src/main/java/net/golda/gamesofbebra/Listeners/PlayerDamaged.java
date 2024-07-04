package net.golda.gamesofbebra.Listeners;

import net.golda.gamesofbebra.Game;
import net.golda.gamesofbebra.GamesOfBebra;
import net.golda.gamesofbebra.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamaged implements Listener {
    Player player;
    @EventHandler
    public  void onDamageEvent(EntityDamageEvent event){
        if (event.getEntity() instanceof Player) {
            player = (Player) event.getEntity();
            if (GamesOfBebra.getCurrentGame().isCurrentPlayer(player)){
                if (player.getHealth() <= event.getDamage()) {
                    event.setCancelled(true);
                    if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) player.teleport(new Location(player.getWorld(), 0, 110,0));
                    GamesOfBebra.getCurrentGame().dropPlayer(player);
                }
            }

        }
    }
}
