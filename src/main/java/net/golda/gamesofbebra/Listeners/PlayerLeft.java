package net.golda.gamesofbebra.Listeners;


import net.golda.gamesofbebra.Game;
import net.golda.gamesofbebra.GamesOfBebra;
import net.golda.gamesofbebra.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeft implements Listener
{
    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        if (GamesOfBebra.isGameCreated()) {
            Player player = event.getPlayer();
            Game currentGame = GamesOfBebra.getCurrentGame();
            if (currentGame.isJoined(player)) {
                currentGame.endGame();
                GamesOfBebra.setGameCreated(false);
                if (currentGame.isOwner(player))
                    Bukkit.broadcastMessage(Lang.TITLE.toString() + Lang.GAME_END_OWNER_LEFT.toString().replace("%player%", player.getName()));
                else
                    Bukkit.broadcastMessage(Lang.TITLE.toString() + Lang.GAME_END_PLAYER_LEFT.toString().replace("%player%", player.getName()));
            }
        }
    }
    }


