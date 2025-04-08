package org.example.lonelyBluePlugin.updaters;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.example.lonelyBluePlugin.managers.AbilityManager;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class ActionBarUpdater implements Runnable {

    private final PlayerLevelManager levelManager;
    private final AbilityManager abilityManager;

    public ActionBarUpdater(PlayerLevelManager levelManager, AbilityManager abilityManager) {
        this.levelManager = levelManager;
        this.abilityManager = abilityManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int level = levelManager.getLevel(player.getUniqueId());
            // Elegir color según el nivel:
            String levelColor;
            if (level > 0) {
                levelColor = ChatColor.GREEN.toString();
            } else if (level < 0) {
                levelColor = ChatColor.RED.toString();
            } else {
                levelColor = ChatColor.GRAY.toString();
            }
            String actionBarMessage = "Level: " + levelColor + level + ChatColor.RESET;
            if (level > 0) {
                String cooldownStatus = abilityManager.getCooldownStatus(player);
                actionBarMessage += " | Ability: " + cooldownStatus;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBarMessage));
        }
    }
}
