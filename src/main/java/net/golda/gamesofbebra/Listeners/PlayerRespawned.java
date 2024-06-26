package net.golda.gamesofbebra.Listeners;

import net.golda.gamesofbebra.GamesOfBebra;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawned implements Listener {

    @EventHandler
    public void playerRespawned(PlayerRespawnEvent playerRespawnEvent){
        if (GamesOfBebra.getCurrentGame().wasInGame(playerRespawnEvent.getPlayer())){
            GamesOfBebra.currentGame.restoreInventory(playerRespawnEvent.getPlayer());
            GamesOfBebra.currentGame.removeFromLast(playerRespawnEvent.getPlayer());
        }
    }
}
