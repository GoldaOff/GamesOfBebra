package net.golda.gamesofbebra;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;



public class Game {
    private static Plugin plugin = null;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> currentPlayers = new ArrayList<>();
    private ArrayList<Player> lastPlayers = new ArrayList<>();
    private HashMap<Player, Location[]> playersLocations = new HashMap<>();
    private HashMap<Player, ItemStack[][]> playersInventories = new HashMap<>();
    private HashMap<Player, Double> playersHealth = new HashMap<>();
    private HashMap<Player, Integer> playersHunger = new HashMap<>();
    private HashMap<Player, Integer> playersLevels = new HashMap<>();
    private HashMap<Player, Float> playersXP = new HashMap<>();
    private HashMap<Player, Collection<PotionEffect>> playersEffects = new HashMap<>();

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
        startTimer();


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
        }.runTaskTimer(plugin, 20*3, 20);
    }

    private void startTimer() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if (step==0) cancel();
                makeTimerStep();
            }
        }.runTaskTimer(plugin, 0, 20);
    }
    int step = 3;
    private void makeTimerStep() {
       for (Player player: players){
           player.sendTitle(ChatColor.translateAlternateColorCodes ('&',"&4"+String.valueOf(step)), "", 2, 16, 2);
           if (step==0){
                player.setWalkSpeed(0.2F);
           }
       }
        step--;

    }

    public void endGame(){
        applyPlayersInfo();
        worldManager.clear();
        isStarted = false;
        GamesOfBebra.setGameCreated(false);
        bar.removeAll();
        players.clear();
        currentPlayers.clear();
        step=3;
    }

    private void restoreInventories() {
        for (Map.Entry<Player, ItemStack[][]> entry : playersInventories.entrySet()){
            Player player = entry.getKey();
            ItemStack[][] itemStacks = entry.getValue();
            player.getInventory().setContents(itemStacks[0]);
            player.getInventory().setArmorContents(itemStacks[1]);
        }
        playersInventories.clear();
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
        Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_PLAYER_DIED.toString().replace("%player%", player.getName()));
        restoreInventory(player);

        if (currentPlayers.size()==1){
            isStarted = false;
            bar.setProgress(1);
            bar.setTitle(Lang.BOSS_BAR_ENDGAME.toString());
            Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_END.toString().replace("%player%", currentPlayers.get(0).getName()));
            currentPlayers.get(0).setGameMode(GameMode.SPECTATOR);
            currentPlayers.get(0).teleport(new Location(currentPlayers.get(0).getWorld(), 0, 120, 0));
            new BukkitRunnable(){
                @Override
                public void run() {
                    endGame();
                }
            }.runTaskLater(plugin, 20*5);


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
        saveHH();
        saveEffects();
    }
    private void saveEffects() {
        for (Player player: players){
            Collection<PotionEffect> effects = player.getActivePotionEffects();
            playersEffects.put(player, effects);
            for (PotionEffect potionEffect: effects) player.removePotionEffect(potionEffect.getType());
        }
    }
    private void saveHH() {
        for (Player player: players){
            playersHealth.put(player, player.getHealth());
            playersHunger.put(player, player.getFoodLevel());
        }
    }
    private void saveLocations(){
        for (Player player: players){
            playersLocations.put(player, new Location[]{player.getLocation(), player.getRespawnLocation()});
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
        restoreXP();
        playersXP.clear();
        restoreLevels();
        playersLevels.clear();
        restoreHH();
        playersHealth.clear();
        playersHunger.clear();
        restoreInventories();
        restoreEffects();
        playersEffects.clear();
    }

    private void restoreEffects() {
        for (Map.Entry<Player, Collection<PotionEffect>> entry: playersEffects.entrySet()){
            Player player = entry.getKey();
            Collection<PotionEffect> currentEffects = player.getActivePotionEffects();
            for (PotionEffect potionEffect: currentEffects) player.removePotionEffect(potionEffect.getType());
            Collection<PotionEffect> effects = entry.getValue();
            for (PotionEffect effect: effects) player.addPotionEffect(effect);
        }
    }
    private void restoreHH() {
        for (Map.Entry<Player, Double> entry: playersHealth.entrySet()){
            Player player = entry.getKey();
            Double health = entry.getValue();
            player.setHealth(health);
        }
        for (Map.Entry<Player, Integer> entry: playersHunger.entrySet()){
            Player player = entry.getKey();
            Integer hunger = entry.getValue();
            player.setFoodLevel(hunger);
        }
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
    public void restoreInventory(Player player){
        ItemStack[][] itemStacks = playersInventories.get(player);
        player.getInventory().setContents(itemStacks[0]);
        player.getInventory().setArmorContents(itemStacks[1]);
        playersInventories.remove(player);
    }
    private void backPlayersToLocations() {
        for (Map.Entry<Player, Location[]> entry: playersLocations.entrySet()){
            Player player = entry.getKey();
            Location[] location = entry.getValue();
            player.teleport(location[0]);
            player.setRespawnLocation(location[1]);
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
            try{
                Material material = getRandomMaterial();
                while (!material.isItem()) material = getRandomMaterial();
                player.getInventory().setItem(player.getInventory().firstEmpty(), material.asItemType().createItemStack());
            } catch (Exception ex) {player.sendMessage(Lang.TITLE.toString()+Lang.GAME_CANT_GIVE);}
        }


    }
    private Material getRandomMaterial(){
        return materials[getRandomValue()];
    }
    private int getRandomValue(){
        return (int) (Math.random() * materials.length);
    }


}
