package net.golda.gamesofbebra.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class GameComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length==1) return List.of(
          "create",
          "start",
          "join",
          "end",
          "settings"
        );

        return null;
    }
}
