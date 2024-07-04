package net.golda.gamesofbebra;

import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
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
        locations.clear();
        playersLocations.clear();
        players.clear();
        currentPlayers.clear();

        for (int x = -25; x < 25; x++){
            for (int z = -25; z < 25; z++){
                for (int y = 180; y > 80; y--){
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }

        for (Entity entity: world.getEntities()){
            if (!(entity instanceof Player) & entity instanceof Damageable) ((Damageable) entity).damage(100000);
        }
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
            player.setWalkSpeed(0);
            player.setHealth(20);
            player.setFoodLevel(20);
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
        world.getWorldBorder().setSize(35);
        world.getWorldBorder().setCenter(new Location(world, 3, 0,0));
        world.setTime(4000);
        world.setStorm(false);
    }

    private void calculateLocations(int players){
        Location defaultLocation = new Location(world, 0, 100, 0);
        locations.add(defaultLocation);
        switch (players){
            case 2:
                Location location2 = new Location(world, 12, 100, 0);
                locations.add(location2);
                break;
            case 3:
                Location location3_1 = new Location(world, 12, 100, 0);
                Location location3_2 = new Location(world, 6, 100, 10);
                locations.add(location3_1);
                locations.add(location3_2);
                break;
            case 4:
                Location location4_1 = new Location(world, 12, 100, 0);
                Location location4_2 = new Location(world, 0, 100, 12);
                Location location4_3 = new Location(world, 12, 100, 12);
                locations.add(location4_1);
                locations.add(location4_2);
                locations.add(location4_3);
                world.getWorldBorder().setCenter(new Location(world, 6, 100, 6));
                break;
            case 5:
                Location location5_1 = new Location(world, 10, 100, -7);
                Location location5_2 = new Location(world, 20, 100, 0);
                Location location5_3 = new Location(world, 16, 100, 11);
                Location location5_4 = new Location(world, 4, 100, 11);
                world.getWorldBorder().setCenter(new Location(world, 10, 100, 3));

                locations.add(location5_1);
                locations.add(location5_2);
                locations.add(location5_3);
                locations.add(location5_4);
                break;
            case 6:
                Location location6_1 = new Location(world, 6, 100, -10);
                Location location6_2 = new Location(world, 18, 100, -10);
                Location location6_3 = new Location(world, 24, 100, 0);
                Location location6_4 = new Location(world, 18, 100, 10);
                Location location6_5 = new Location(world, 6, 100, 10);
                world.getWorldBorder().setCenter(new Location(world, 12, 100, 0));
                locations.add(location6_1);
                locations.add(location6_2);
                locations.add(location6_3);
                locations.add(location6_4);
                locations.add(location6_5);
                break;
            case 7:
                Location location7_1 = new Location(world, 2, 100, -12);
                Location location7_2 = new Location(world, 13, 100, -17);
                Location location7_3 = new Location(world, 24, 100, -12);
                Location location7_4 = new Location(world, 26, 100, 0);
                Location location7_5 = new Location(world, 19, 100, 10);
                Location location7_6 = new Location(world, 7, 100, 10);
                world.getWorldBorder().setCenter(new Location(world, 13, 100, -4));
                world.getWorldBorder().setSize(40);

                locations.add(location7_1);
                locations.add(location7_2);
                locations.add(location7_3);
                locations.add(location7_4);
                locations.add(location7_5);
                locations.add(location7_6);
                break;
            case 8:
                Location location8_1 = new Location(world, 8, 100, -8);
                Location location8_2 = new Location(world, 20, 100, -8);
                Location location8_3 = new Location(world, 28, 100, 0);
                Location location8_4 = new Location(world, 28, 100, 12);
                Location location8_5 = new Location(world, 20, 100, 20);
                Location location8_6 = new Location(world, 8, 100, 20);
                Location location8_7 = new Location(world, 0, 100, 12);
                world.getWorldBorder().setCenter(new Location(world, 14, 100, 6));
                world.getWorldBorder().setSize(40);
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
