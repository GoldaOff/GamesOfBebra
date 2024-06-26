package net.golda.gamesofbebra;

import net.golda.gamesofbebra.Commands.GameCommand;
import net.golda.gamesofbebra.Commands.GameComplete;
import net.golda.gamesofbebra.Listeners.PlayerDied;
import net.golda.gamesofbebra.Listeners.PlayerLeft;
import net.golda.gamesofbebra.Listeners.PlayerRespawned;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;


public final class GamesOfBebra extends JavaPlugin {

    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    private Logger log;
    private static boolean gameStatus;

    public static Game currentGame;

    @Override
    public void onEnable() {

        //loading lang.yml
        log = getLogger();
        loadLang();

        //setting commands executors and tab completers
        getCommand("game").setExecutor(new GameCommand());
        getCommand("game").setTabCompleter(new GameComplete());

        //setting event listeners
        getServer().getPluginManager().registerEvents(new PlayerLeft(), this);
        getServer().getPluginManager().registerEvents(new PlayerDied(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawned(), this);

        currentGame = new Game(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    //loading the file
    public void loadLang()
    {
        File lang = new File(getDataFolder(), "lang.yml");
        YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
        if (!lang.exists())
        {
            try
            {
                langConfig.save(lang);
            } catch (IOException e)
            {
                e.printStackTrace();
                log.info("Could not create language file.");
                log.info("Disabling plugin.");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        for (Lang item : Lang.values())
        {
            if (langConfig.getString(item.getPath()) == null)
            {
                langConfig.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(langConfig);
        try
        {
            langConfig.save(lang);
        } catch (IOException e)
        {
            log.info("Could not save language file.");
            log.info("Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    public static boolean isGameCreated(){
        return gameStatus;
    }

    public static void setGameCreated(boolean status) {
        gameStatus = status;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }


}
