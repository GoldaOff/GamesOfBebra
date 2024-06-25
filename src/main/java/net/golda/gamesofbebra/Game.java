package net.golda.gamesofbebra;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;


public class Game {
    private static Plugin plugin = null;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> currentPlayers = new ArrayList<>();
    private HashMap<Player, Location> playersLocations = new HashMap<>();
    private final Material[] materials = Material.values();

    private Player owner;
    private WorldManager worldManager = new WorldManager();
    private BossBar bar = Bukkit.getServer().createBossBar(Lang.BOSS_BAR_TITLE.toString(), BarColor.RED, BarStyle.SOLID);

    private int currentSecond = 0;
    private int time = 15;
    private boolean isStarted = false;

    public Game(Plugin plugin1){
        plugin=plugin1;
    }

    public void startGame(int time){
        currentSecond = time-1;
        this.time = time;
        isStarted = true;
        worldManager.start(players);
        GamesOfBebra.currentGame = this;
        updateBossBarText();
        addPlayersToBossBar();
        currentPlayers.addAll(players);
        bar.setProgress(1.0);
        givePlayersRandomItems();

        new BukkitRunnable(){
            @Override
            public void run() {
                if (!isStarted) {
                    cancel();
                } else {
                    timerStep();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void endGame(){

        worldManager.clear();
        isStarted = false;
        GamesOfBebra.setGameCreated(false);
        bar.removeAll();
        players.clear();
        currentPlayers.clear();
    }

    public void timerStep(){
        if (currentSecond!=0){
            currentSecond--;
            updateBossBarText();
            updateBossBarTime();
        } else {
            currentSecond = time-1;
            bar.setProgress(1);
            updateBossBarText();
            givePlayersRandomItems();
        }
    }

    public boolean isOwner(Player player) {
        return (player.equals(owner));
    }

    public boolean isJoined(Player player){
        if (players.contains(player)) return true;
        else return false;
    }

    public boolean canJoin(Player player){
        if (isJoined(player) & players.size()<9){
            return false;
        }
        else return true;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public boolean canStart_players(){
        if (players.size()==1) return false;
        else return true;
    }

    public boolean isStarted() {
        return isStarted;
    }

    private void saveLocations(){
    }


    public void setOwner(Player player) {
        this.owner = player;
        players.add(player);
    }

    private void updateBossBarText(){
        bar.setTitle(Lang.BOSS_BAR_TITLE.toString().replace("%time%", String.valueOf(currentSecond+1)));
    }

    private void updateBossBarTime(){
        bar.setProgress(bar.getProgress()-(1.0/time));
    }

    private void addPlayersToBossBar(){
        for (Player player: players){
            bar.addPlayer(player);
        }
    }


    private void givePlayersRandomItems(){
        for (Player player: currentPlayers){
            Material material = getRandomMaterial();
            while (!material.isItem()) material = getRandomMaterial();
            player.getInventory().setItem(player.getInventory().firstEmpty(), material.asItemType().createItemStack());
        }

    }

    private Material getRandomMaterial(){
        return materials[getRandomValue()];
    }

    private int getRandomValue(){
        return (int) (Math.random() * materials.length);
    }
}
