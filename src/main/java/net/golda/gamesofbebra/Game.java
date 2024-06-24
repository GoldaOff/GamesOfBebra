package net.golda.gamesofbebra;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;


public class Game {
    private boolean isStarted = false;
    private ArrayList<Player> players = new ArrayList<>();
    private Player owner;
    WorldManager worldManager = new WorldManager();

    public Game(Player owner){
        this.owner = owner;
        players.add(owner);
    }

    public void startGame(){
        isStarted = true;
        worldManager.start(players);
    }

    public void endGame(){
        worldManager.clear();
        System.out.println("Заканчиваю игру");
        isStarted = false;

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

    public void setStarted(boolean started) {
        isStarted = started;
    }
}
