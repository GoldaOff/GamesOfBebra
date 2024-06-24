package net.golda.gamesofbebra.Commands;

import net.golda.gamesofbebra.Game;
import net.golda.gamesofbebra.GamesOfBebra;
import net.golda.gamesofbebra.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GameCommand implements CommandExecutor {

    private Game currendGame;

    @Override
    public boolean onCommand(CommandSender Sender, Command command, String s, String[] strings) {

        if(!(Sender instanceof Player)){
            return false;
        }
        Player player = ((Player) Sender).getPlayer();
        String type;
        String settingType;
        String settingValue;
        try{
            switch (strings[0].toLowerCase()){
                case "create":
                    //GAME IS NOT STARTED
                    if(!GamesOfBebra.isGameCreated()){
                        GamesOfBebra.setGameCreated(true);
                        currendGame = new Game(player);
                        GamesOfBebra.setCurrentGame(currendGame);
                        Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_CREATED.toString().replace("%player%", Sender.getName()));
                    }
                    //GAME STARTED
                    else {
                        Sender.sendMessage(Lang.TITLE.toString()+Lang.GAME_CREATE_CANCELED.toString());
                    }

                    break;

                case "join":
                    if (GamesOfBebra.isGameCreated()){

                        if (currendGame.canJoin(player)){
                            currendGame.addPlayer(player);
                            Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_PLAYER_JOINED.toString().replace("%player%", player.getName()));
                        }
                        else
                            player.sendMessage(Lang.TITLE.toString()+Lang.GAME_PLAYER_JOIN_CANCELED);
                    }
                    else {
                        player.sendMessage(Lang.TITLE.toString()+Lang.GAME_CANT_JOIN);
                    }

                    break;

                case "end":
                    //GAME IS NOT STARTED
                    if (!GamesOfBebra.isGameCreated()){
                        Sender.sendMessage(Lang.TITLE.toString() +Lang.GAME_END_CANCELED.toString());
                    }
                    //GAME IS STARTED
                    else{
                        if (currendGame.isOwner(player)){
                            if(currendGame.isStarted()) {
                                currendGame.endGame();
                                GamesOfBebra.setGameCreated(false);
                                Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_END_BY_PLAYER.toString().replace("%player%", Sender.getName()));
                            }
                            else {
                                player.sendMessage(Lang.TITLE.toString()+Lang.GAME_END_CANCELED);
                            }

                        }
                        else {
                            player.sendMessage(Lang.TITLE.toString()+Lang.GAME_END_CANCELED_NOT_OWNER);
                        }

                    }

                    break;

                case "start":
                    //GAME IS NOT STARTED
                    if (!GamesOfBebra.isGameCreated()){
                        Sender.sendMessage(Lang.TITLE.toString()+Lang.GAME_START_CANCELED);
                    }
                    //GAME IS CREATED
                    else{
                        if (currendGame.isOwner(player)){
                            if (currendGame.canStart_players()){
                                if (!currendGame.isStarted()){
                                    currendGame.startGame();
                                    Bukkit.broadcastMessage(Lang.TITLE.toString()+Lang.GAME_START);
                                }
                                else {
                                    player.sendMessage(Lang.TITLE.toString()+Lang.GAME_START_CANCELED_ALREADY_STARTED);
                                }

                            }
                            else {
                                player.sendMessage(Lang.TITLE.toString()+Lang.GAME_START_CANCELED_NO_PLAYERS);
                            }
                        }
                        else {
                            player.sendMessage(Lang.TITLE.toString()+Lang.GAME_START_CANCELED_NOT_OWNER);
                        }
                    }

                    break;

                case "settings":
                    type="settings";
                    break;

                default:
                    return false;
            }

            return true;

        }
        catch (Exception e){
            System.out.println(e.toString());
            System.out.print(Arrays.toString(e.getStackTrace()));
            return false;
        }


    }
}
