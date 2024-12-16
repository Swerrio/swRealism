package me.swerrio.swRealism;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Плагин swRealism успешно запущен");

        Bukkit.getPluginManager().registerEvents(new HungerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Логика при отключении плагина
    }
}


