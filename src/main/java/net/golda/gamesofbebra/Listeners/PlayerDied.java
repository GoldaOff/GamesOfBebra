package net.golda.gamesofbebra.Listeners;

import net.golda.gamesofbebra.Game;
import net.golda.gamesofbebra.GamesOfBebra;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;


public class PlayerDied implements Listener {
    @EventHandler
    public void onPlayerDies(EntityDeathEvent entityDeathEvent){
            if(GamesOfBebra.isGameCreated()){
                if (entityDeathEvent.getEntity() instanceof Player) {
                    Game game = GamesOfBebra.getCurrentGame();
                    Player player = (Player) entityDeathEvent.getEntity();
                    if (game.isJoined(player)){
                        if (game.isCurrentPlayer(player)) {
                            player.spigot().respawn();
                            game.dropPlayer(player);
                        }
                        }
                    }
                }
            }
    }

