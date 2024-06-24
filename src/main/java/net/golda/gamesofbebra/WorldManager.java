package net.golda.gamesofbebra;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class WorldManager {

    private World world;
    private World originalWorld;
    private HashMap<Player, Location> playersLocations = new HashMap<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> currentPlayers = new ArrayList<>();

    public void clear() {
        for (Location location: locations){
            location.getBlock().setType(Material.AIR);
        }
        locations.clear();
        playersLocations.clear();
    }


    public void start(ArrayList<Player> players_s){
        generateWorld();
        for (Player player: players_s){
            players.add(player);
            currentPlayers.add(player);
        }
        calculateLocations(players.size());
        placeBedrock();
        randomizeLocations();
        teleportPlayersToLocations();
    }

    private void teleportPlayersToLocations() {
        for (Player player: players){
            player.teleport(playersLocations.get(player));
        }
    }

    private void randomizeLocations() {
        Collections.shuffle(locations);
        for(int i =0; i<players.size(); i++){
            playersLocations.put(players.get(i), locations.get(i).add(0, 1, 0));
        }

    }

    private void generateWorld(){
        WorldCreator worldCreator = new WorldCreator("Game");
        worldCreator.generateStructures(false);
        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("{\"layers\": [{\"block\": \"stone\", \"height\": 0}, {\"block\": \"grass_block\", \"height\": 0}], \"biome\":\"plains\"}");
        world = worldCreator.createWorld();
    }

    private void calculateLocations(int players){
        Location defaultLocation = new Location(world, 0, 100, 0);
        locations.add(defaultLocation);
        switch (players){
            case 2:
                Location location2 = new Location(world, 7, 100, 0);
                locations.add(location2);
                break;
            case 3:
                Location location3_1 = new Location(world, 8, 100, 0);
                Location location3_2 = new Location(world, 4, 100, 6);
                locations.add(location3_1);
                locations.add(location3_2);
                break;
            case 4:
                Location location4_1 = new Location(world, 7, 100, 0);
                Location location4_2 = new Location(world, 0, 100, 7);
                Location location4_3 = new Location(world, 7, 100, 7);
                locations.add(location4_1);
                locations.add(location4_2);
                locations.add(location4_3);
                break;
            case 5:
                Location location5_1 = new Location(world, 2, 100, 7);
                Location location5_2 = new Location(world, 6, 100, -4);
                Location location5_3 = new Location(world, 11, 100, 1);
                Location location5_4 = new Location(world, 9, 100, 7);
                locations.add(location5_1);
                locations.add(location5_2);
                locations.add(location5_3);
                locations.add(location5_4);
                break;
            case 6:
                Location location6_1 = new Location(world, 0, 100, 7);
                Location location6_2 = new Location(world, 6, 100, -3);
                Location location6_3 = new Location(world, 6, 100, 10);
                Location location6_4 = new Location(world, 12, 100, 0);
                Location location6_5 = new Location(world, 12, 100, 7);
                locations.add(location6_1);
                locations.add(location6_2);
                locations.add(location6_3);
                locations.add(location6_4);
                locations.add(location6_5);
                break;
            case 7:
                defaultLocation = new Location(world, -1, 100, 1);
                Location location7_1 = new Location(world, 1, 100, 8);
                Location location7_2 = new Location(world, 4, 100, -5);
                Location location7_3 = new Location(world, 7, 100, 12);
                Location location7_4 = new Location(world, 11, 100, -4);
                Location location7_5 = new Location(world, 13, 100, 8);
                Location location7_6 = new Location(world, 15, 100, 1);
                locations.remove(0);//
                locations.add(defaultLocation);
                locations.add(location7_1);
                locations.add(location7_2);
                locations.add(location7_3);
                locations.add(location7_4);
                locations.add(location7_5);
                locations.add(location7_6);
                break;
            case 8:
                Location location8_1 = new Location(world, 0, 100, 7);
                Location location8_2 = new Location(world, 5, 100, -5);
                Location location8_3 = new Location(world, 5, 100, 12);
                Location location8_4 = new Location(world, 12, 100, -5);
                Location location8_5 = new Location(world, 12, 100, 12);
                Location location8_6 = new Location(world, 17, 100, 0);
                Location location8_7 = new Location(world, 17, 100, 7);
                locations.add(location8_1);
                locations.add(location8_2);
                locations.add(location8_3);
                locations.add(location8_4);
                locations.add(location8_5);
                locations.add(location8_6);
                locations.add(location8_7);
                break;
        }

    }

    private void placeBedrock() {
        for (Location location: locations){
            location.subtract(0, 1, 0).getBlock().setType(Material.BEDROCK);
        }
    }


}
