package net.golda.gamesofbebra.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String s, String[] strings) {

        String type;
        String settingType;
        String settingValue;

        try{
            switch (strings[0].toLowerCase()){
                case "create":
                    type="create";

                case "join":
                    type="join";

                case "end":
                    type="end";

                case "start":
                    type="start";

                case "settings":
                    type="settings";
            }


        }
        catch (Exception e){
            return false;
        }


        return false;
    }
}
