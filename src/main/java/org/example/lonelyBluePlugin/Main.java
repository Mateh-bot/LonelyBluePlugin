package org.example.lonelyBluePlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.lonelyBluePlugin.commands.AbilityCommand;
import org.example.lonelyBluePlugin.commands.TrustCommand;
import org.example.lonelyBluePlugin.listeners.*;
import org.example.lonelyBluePlugin.managers.AbilityManager;
import org.example.lonelyBluePlugin.managers.DataManager;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;
import org.example.lonelyBluePlugin.managers.TrustManager;
import org.example.lonelyBluePlugin.updaters.ActionBarUpdater;
import org.example.lonelyBluePlugin.updaters.PlayerEffectUpdater;

public final class Main extends JavaPlugin {

    private static Main instance;
    private DataManager dataManager;
    private PlayerLevelManager levelManager;
    private TrustManager trustManager;
    private AbilityManager abilityManager;

    @Override
    public void onEnable() {
        instance = this;

        dataManager = new DataManager(this);
        levelManager = new PlayerLevelManager(dataManager);
        trustManager = new TrustManager(dataManager);
        abilityManager = new AbilityManager(levelManager);

        // Register commands
        registerCommands();

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(levelManager, abilityManager), this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(levelManager), this);
        getServer().getPluginManager().registerEvents(new VillagerTradeRestrictionListener(levelManager), this);
        getServer().getPluginManager().registerEvents(new DamageListener(levelManager), this);
        getServer().getPluginManager().registerEvents(new EnchantingTableRestrictionListener(levelManager), this);

        // Register custom crafting recipes
        registerCraftingRecipes();

        // Schedule repeating task to update player effects (e.g. permanent fire resistance at level 0, and slowness at -3)
        Bukkit.getScheduler().runTaskTimer(this, new PlayerEffectUpdater(levelManager), 0L, 10 * 20); // cada 10 segundos

        // Schedule Action Bar updater to run every second (20 ticks)
        Bukkit.getScheduler().runTaskTimer(this, new ActionBarUpdater(levelManager, abilityManager), 0L, 20L);

    }

    @Override
    public void onDisable() {
        levelManager.saveLevels();
        trustManager.saveTrust();
    }

    public static Main getInstance() {
        return instance;
    }

    private void registerCommands() {
        PluginCommand trustCommand = this.getCommand("trust");
        if (trustCommand != null) {
            trustCommand.setExecutor(new TrustCommand());
        }
        // Register /ability command to trigger the level 3 ability manually.
        PluginCommand abilityCommand = this.getCommand("ability");
        if (abilityCommand != null) {
            abilityCommand.setExecutor(new AbilityCommand(abilityManager));
        }
    }

    private void registerCraftingRecipes() {
        // Revive Token Recipe
        ItemStack reviveToken = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta reviveMeta = reviveToken.getItemMeta();
        reviveMeta.setDisplayName("Revive Token");
        reviveToken.setItemMeta(reviveMeta);

        ShapedRecipe reviveRecipe = new ShapedRecipe(new NamespacedKey(this, "revive_token"), reviveToken);
        reviveRecipe.shape("DWD", "WTW", "DWD");
        reviveRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        reviveRecipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        reviveRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        Bukkit.addRecipe(reviveRecipe);

        // Level Upgrader Recipe (only usable when player's level is negative)
        ItemStack levelUpgrader = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemMeta upgraderMeta = levelUpgrader.getItemMeta();
        upgraderMeta.setDisplayName("Level Upgrader");
        levelUpgrader.setItemMeta(upgraderMeta);

        ShapedRecipe upgraderRecipe = new ShapedRecipe(new NamespacedKey(this, "level_upgrader"), levelUpgrader);
        upgraderRecipe.shape(" N ", "NEN", " N ");
        upgraderRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        upgraderRecipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE);
        Bukkit.addRecipe(upgraderRecipe);
    }
}
