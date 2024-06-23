package net.golda.gamesofbebra;

import net.golda.gamesofbebra.Commands.GameCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class GamesOfBebra extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("game").setExecutor(new GameCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
