package org.example.lonelyBluePlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.lonelyBluePlugin.managers.AbilityManager;

public class AbilityCommand implements CommandExecutor {

    private final AbilityManager abilityManager;

    public AbilityCommand(AbilityManager abilityManager) {
        this.abilityManager = abilityManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        abilityManager.triggerAbility(player);
        return true;
    }
}
