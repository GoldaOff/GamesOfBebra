package net.golda.gamesofbebra;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Game {
    private static Plugin plugin = null;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> currentPlayers = new ArrayList<>();
    private ArrayList<Player> lastPlayers = new ArrayList<>();
    private HashMap<Player, Location> playersLocations = new HashMap<>();
    private HashMap<Player, ItemStack[][]> playersInventories = new HashMap<>();
    private HashMap<Player, Integer> playersLevels = new HashMap<>();
    private HashMap<Player, Float> playersXP = new HashMap<>();
    private final Material[] materials = Material.values();
    private Player owner;
    private WorldManager worldManager = new WorldManager();
    private BossBar bar = Bukkit.getServer().createBossBar(Lang.BOSS_BAR_TITLE.toString(), BarColor.RED, BarStyle.SOLID);
    private int currentSecond = 0;
    private int time = 10;
    private boolean isStarted = false;

    public Game(Plugin plugin1){
        plugin=plugin1;
    }

    public void startGame(int time){
        savePlayersInfo();
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
        applyPlayersInfo();
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


    //ADDING AND DELETING PLAYERS
    public void dropPlayer(Player player){
        currentPlayers.remove(player);
        player.setGameMode(GameMode.SPECTATOR);
        worldManager.teleportToWorld(player);

        if (currentPlayers.size()==1){
            Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_END.toString().replace("%player%", currentPlayers.get(0).getName()));
            endGame();
        }

    }
    public void setOwner(Player player) {
        owner = player;
        players.add(player);
    }
    public void addPlayer(Player player){
        players.add(player);
    }
    public void removeFromLast(Player player) { lastPlayers.remove(player); }

    //PUBLIC BOOLEANS
    public boolean isOwner(Player player) {
        return (player.equals(owner));
    }
    public boolean canJoin(Player player){
        if (isJoined(player) & players.size()<9){
            return false;
        }
        else return true;
    }
    public boolean canStart_players(){
        if (players.size()==1) return false;
        else return true;
    }
    public boolean isStarted() {
        return isStarted;
    }
    public boolean isJoined(Player player){
        return players.contains(player);
    }
    public boolean isCurrentPlayer(Player player){
        return currentPlayers.contains(player);
    }
    public boolean wasInGame(Player player) { return lastPlayers.contains(player); }


    //SAVING PLAYERS DATA
    private void savePlayersInfo(){
        saveLocations();
        saveInventories();
        saveLevels();
        saveXP();
    }
    private void saveLocations(){
        for (Player player: players){
            playersLocations.put(player, player.getLocation());
        }
    }
    private void saveInventories(){
        for (Player player: players){
            ItemStack[] [] store = new ItemStack[2][1];
            store[0] = player.getInventory().getContents();
            store[1] = player.getInventory().getArmorContents();
            playersInventories.put(player, store);
            player.getInventory().clear();
        }
    }
    private void saveLevels(){
        for (Player player: players){
            playersLevels.put(player, player.getLevel());
        }
    }
    private void saveXP(){
        for (Player player: players){
            playersXP.put(player, player.getExp());
        }
    }


    //RESTORING PLAYERS DATA
    private void applyPlayersInfo(){
        backPlayersToLocations();
        playersLocations.clear();
        restoreInventories();
        restoreXP();
        playersXP.clear();
        restoreLevels();
        playersLevels.clear();
    }
    private void restoreLevels() {
        for(Map.Entry<Player, Integer> entry: playersLevels.entrySet()){
            Player player = entry.getKey();
            Integer lvl = entry.getValue();
            player.setLevel(lvl);
        }
    }
    private void restoreXP() {
        for(Map.Entry<Player, Float> entry: playersXP.entrySet()){
            Player player = entry.getKey();
            Float exp = entry.getValue();
            player.setExp(exp);
        }
    }
    private void restoreInventories() {
        for (Player player: players){
            if (!player.isDead()) restoreInventory(player);
            else lastPlayers.add(player);
        }
    }
    public void restoreInventory(Player player){
        ItemStack[][] itemStacks = playersInventories.get(player);
        player.getInventory().setContents(itemStacks[0]);
        player.getInventory().setArmorContents(itemStacks[1]);
        playersInventories.remove(player);
    }
    private void backPlayersToLocations() {
        for (Map.Entry<Player, Location> entry: playersLocations.entrySet()){
            Player player = entry.getKey();
            Location location = entry.getValue();
            player.teleport(location);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }


    //BOSS BAR
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


    //RANDOM ITEM
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
